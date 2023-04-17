package com.app.kokonut.qna;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.AwsS3Util;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.qna.dtos.QnaAnswerSaveDto;
import com.app.kokonut.qna.dtos.QnaDetailDto;
import com.app.kokonut.qna.dtos.QnaListDto;
import com.app.kokonut.qna.dtos.QnaQuestionSaveDto;
import com.app.kokonut.qnaFile.QnaFile;
import com.app.kokonut.qnaFile.QnaFileRepository;
import com.app.kokonut.qnaFile.dto.QnaFileListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class QnaService {

    private final QnaFileRepository qnaFileRepository;

    @Value("${kokonut.aws.s3.qnaS3Folder}")
    private String qnaS3Folder;

    @Value("${kokonut.aws.s3.url}")
    private String AWSURL;

    private final AwsS3Util awsS3Util;

    private final HistoryService historyService;
    private final QnaRepository qnaRepository;
    private final AdminRepository adminRepository;
    private final MailSender mailSender;

    public QnaService(HistoryService historyService, AwsS3Util awsS3Util, QnaRepository qnaRepository, AdminRepository adminRepository,
                      QnaFileRepository qnaFileRepository, MailSender mailSender) {
        this.awsS3Util = awsS3Util;
        this.historyService = historyService;
        this.qnaRepository = qnaRepository;
        this.adminRepository = adminRepository;
        this.qnaFileRepository = qnaFileRepository;
        this.mailSender = mailSender;
    }

    // 1:1 문의 리스트호출
    public ResponseEntity<Map<String, Object>> qnaList(JwtFilterDto jwtFilterDto, Pageable pageable) {
        log.info("qnaList 호출");
        AjaxResponse res = new AjaxResponse();

        log.info("jwtFilterDto : "+jwtFilterDto);

        Page<QnaListDto> qnaListDtos = qnaRepository.findQnaPage(jwtFilterDto, pageable);
        if(qnaListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        } else {
            return ResponseEntity.ok(res.ResponseEntityPage(qnaListDtos));
        }
    }

    // 1:1 문의 등록하기
    @Transactional
    public ResponseEntity<Map<String, Object>> qnaWrite(QnaQuestionSaveDto qnaQuestionSaveDto, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("qnaWrite 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("qnaQuestionSaveDto.getQnaTitle 제목 : "+qnaQuestionSaveDto.getQnaTitle());
//        log.info("qnaQuestionSaveDto.getQnaContent 내용 : "+qnaQuestionSaveDto.getQnaContent());
//        log.info("qnaQuestionSaveDto.getMultipartFiles 파일들 : "+qnaQuestionSaveDto.getMultipartFiles());

        int qnaType = 5;
        if(qnaQuestionSaveDto.getQnaType().equals("기타")) {
            qnaType = 0;
        } else if(qnaQuestionSaveDto.getQnaType().equals("회원정보")) {
            qnaType = 1;
        } else if(qnaQuestionSaveDto.getQnaType().equals("사업자정보")) {
            qnaType = 2;
        } else if(qnaQuestionSaveDto.getQnaType().equals("Kokonut서비스")) {
            qnaType = 3;
        } else if(qnaQuestionSaveDto.getQnaType().equals("결제")) {
            qnaType = 4;
        }

        // 업로들할 파일명 리스트
//        List<String> fileNames = new ArrayList<>();
//
//        if(qnaQuestionSaveDto.getMultipartFiles() != null) {
//            for (MultipartFile file : qnaQuestionSaveDto.getMultipartFiles()) {
//                if (file.isEmpty()) {
//                    continue;
//                }
//                try {
//                    byte[] bytes = file.getBytes();
//                    Path path = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
//                    Files.write(path, bytes);
//                    fileNames.add(file.getOriginalFilename());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        log.info("업로드할 파일들 : "+fileNames);

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 1:1 문의하기등록 코드
        ActivityCode activityCode = ActivityCode.AC_41;
        // 활동이력 저장 -> 비정상 모드
        String ip = CommonUtil.clientIp();

        Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

        // 1:1 문의 등록
        Qna qna = new Qna();
        qna.setAdminId(adminId);
        qna.setQnaTitle(qnaQuestionSaveDto.getQnaTitle());
        qna.setQnaContent(qnaQuestionSaveDto.getQnaContent());
        qna.setQnaType(qnaType);
        qna.setQnaState(0);
        qna.setInsert_email(email);
        qna.setInsert_date(LocalDateTime.now());
        Qna saveQna = qnaRepository.save(qna);
        log.info("1:1 문의 게시글 등록 완료 saveQna : "+saveQna.getQnaId());

        // 파일 업로드 처리
        log.info("파일 업로드 처리 시작 saveQna : "+saveQna.getQnaId());
        List<MultipartFile> multipartFiles = qnaQuestionSaveDto.getMultipartFiles();
        if(multipartFiles == null){
            log.info("첨부파일 없음");
        }else {
            log.info("첨부파일 있음. 파일 업로드 시작. multipartFiles 처리해야할 건 수 : "+multipartFiles.size());
            List<QnaFile> qnaFileList = new ArrayList<>();
            for (MultipartFile multipartFile: multipartFiles) {
                QnaFile qnaFile = new QnaFile();
                qnaFile.setQnaId(saveQna.getQnaId());

                // file original name
                String originalFilename = Normalizer.normalize(Objects.requireNonNull(multipartFile.getOriginalFilename()), Normalizer.Form.NFC);
                log.info("originalFilename : "+originalFilename);
                qnaFile.setQfOriginalFilename(originalFilename);

                // file size
                long fileSize = multipartFile.getSize();
                log.info("fileSize : "+fileSize);

                // file extension (확장자)
                String ext;
                ext = '.'+originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                log.info("ext : "+ext);

                // file name 서버 저장 시 중복 명 처리
                String fileName = UUID.randomUUID().toString().replace("-", "")+ext;
                log.info("fileName : "+fileName);
                qnaFile.setQfFilename(fileName);

                // S3에 저장 할 파일 주소
                SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                String filePath = qnaS3Folder+date.format(new Date());
                qnaFile.setQfBucket(filePath);
                log.info("filePath : "+filePath);

                qnaFile.setQfPath(AWSURL);
                qnaFile.setQfVolume(multipartFile.getSize());
                qnaFile.setInsert_email(email);
                qnaFile.setInsert_date(LocalDateTime.now());

                // S3에 파일 업로드
                String storedFileName = awsS3Util.imageFileUpload(multipartFile, fileName, qnaS3Folder+date.format(new Date()));
                if(storedFileName == null) {
                    log.error("이미지 업로드를 실패했습니다. -관리자에게 문의해주세요-");
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO039.getCode(), ResponseErrorCode.KO039.getDesc()));
                } else {
                    qnaFileList.add(qnaFile);
                }
            }

            // 파일 저장
            qnaFileRepository.saveAll(qnaFileList);
            log.info("첨부 파일 저장에 성공햇습니다.");
        }

//        // 메일 전송 (모든 시스템 관리자에게 알림 메일 전송)
//        log.info("시스템 관리자들에게 문의 등록 안내 메일 전송 시작");
//        List<AdminEmailInfoDto> systemAdminInfos = adminRepository.findSystemAdminEmailInfo();
//        if(!systemAdminInfos.isEmpty()){
//            // 메일 내용 작성
//            String mailData = URLEncoder.encode(email, "UTF-8");
//            String title = "문의하기 등록 알림";
//            // TODO : 답변 내용을 HTML 태그를 붙여서 메일로 전송해준다. 화면단과 개발할 때 추가 개발해야함.
//            String contents = "문의하기 질문이 등록 되었습니다.<br> 등록자 이메일 : "+email;
//
//            for(AdminEmailInfoDto systemAdminInfo : systemAdminInfos){
//                String toEmail = systemAdminInfo.getKnEmail();
//                String toName = systemAdminInfo.getKnName();
//                log.info("toEmail" + toEmail + ", toName" + toName);
//                if (toEmail == null || toName == null ){
//                    log.error("시스템관리자 메일 정보를 찾을 수 없습니다.");
//                }else{
//                    mailSender.sendMail(toEmail, toName, title, contents);
//                }
//            }
//        }else{
//            log.error("시스템관리자를 찾을 수 없습니다.");
//        }

        historyService.updateHistory(activityHistoryId,
                companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

        return ResponseEntity.ok(res.success(data));
    }

    // 1:1 문의 상세보기
    @Transactional
    public ResponseEntity<Map<String, Object>> qnaDetail(Long qnaId, JwtFilterDto jwtFilterDto) {
        log.info("qnaDetail 호출");

        log.info("qnaId : "+qnaId);
        log.info("jwtFilterDto : "+jwtFilterDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(qnaId == null) {
            log.error("1:1문의 ID 값이 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO053.getCode(), ResponseErrorCode.KO053.getDesc()));
        } else {
            log.info("1:1 문의 게시글 상세보기");
            QnaDetailDto qnaDetailDto = qnaRepository.findByQnaDetail(qnaId);

            if(qnaDetailDto == null) {
                log.error("해당 qnaId의 1:1 문의 게시글을 조회할 수 없습니다. 문의 게시글 qnaId : "+qnaId);
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO054.getCode(), ResponseErrorCode.KO054.getDesc()));
            } else {

                // 시스템관리자도 아닌데, 다른사람의 글을 조회할 경우
                if(!qnaDetailDto.getInsert_email().equals(jwtFilterDto.getEmail()) && !jwtFilterDto.getRole().getCode().equals("ROLE_SYSTEM")) {
                    log.error("본인이 작성한 1:1 문의만 확인 가능합니다.");
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO055.getCode(), ResponseErrorCode.KO055.getDesc()));
                }

                // 해당 문의글의 첨부파일 조회
                List<QnaFileListDto> qnaFileListDtoList = qnaFileRepository.findByQnaFileList(qnaId);
                if(qnaFileListDtoList != null) {
                    for(QnaFileListDto qnaFileListDto : qnaFileListDtoList) {
                        String ThumSignUrl = awsS3Util.getPreSignedURL(qnaFileListDto.getQfBucket(), "s_"+qnaFileListDto.getQfFilename(), 1L);
                        String oriSignUrl = awsS3Util.getPreSignedURL(qnaFileListDto.getQfBucket(), qnaFileListDto.getQfFilename(), 1L);
//                        log.info("ThumSignUrl : "+ThumSignUrl);
//                        log.info("oriSignUrl : "+oriSignUrl);
                        qnaFileListDto.setQfBucket(ThumSignUrl);
                        qnaFileListDto.setQfFilename(oriSignUrl);
                    }
                }

                data.put("role", jwtFilterDto.getRole().getCode());
                data.put("qnaDetail", qnaDetailDto);
                data.put("qnaDetailFile", qnaFileListDtoList);

                return ResponseEntity.ok(res.success(data));
            }
        }
    }

    // 1:1 문의 답변하기
    @Transactional
    public ResponseEntity<Map<String, Object>> qnaAnswer(QnaAnswerSaveDto qnaAnswerSaveDto, JwtFilterDto jwtFilterDto) {
        log.info("qnaAnswer 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("qnaAnswerSaveDto.getQnaId() : "+qnaAnswerSaveDto.getQnaId());
        log.info("qnaAnswerSaveDto.getQnaAnswer() : "+qnaAnswerSaveDto.getQnaAnswer());
        log.info("jwtFilterDto : "+jwtFilterDto);

        if(!jwtFilterDto.getRole().getCode().equals("ROLE_SYSTEM")){
            log.error("권한을 확인해주세요. 시스템관리자만 문의 답변 등록이 가능합니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }else{
            // 문의글 답변 내용 등록
            log.info("1:1 문의 게시글 답변 등록 시작");

            // 접속 정보에서 qnaId 가져오기
            Admin admin = adminRepository.findByKnEmail(jwtFilterDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+jwtFilterDto.getEmail()));

            if(qnaAnswerSaveDto.getQnaId() != null){
                //저장내용 세팅
                Optional<Qna> savedQna = qnaRepository.findById(qnaAnswerSaveDto.getQnaId());
                if(savedQna.isEmpty()){
                    log.error("해당 문의글을 찾을 수 없습니다. 문의글 qnaId : " + qnaAnswerSaveDto.getQnaId());
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO054.getCode(), ResponseErrorCode.KO054.getDesc()));
                }else{
                    // 답변 내용
                    savedQna.get().setQnaState(1);
                    savedQna.get().setQnaAnswer(qnaAnswerSaveDto.getQnaAnswer());
                    savedQna.get().setModify_id(admin.getAdminId());
                    savedQna.get().setModify_email(admin.getModify_email());
                    savedQna.get().setModify_date(LocalDateTime.now());

                    qnaRepository.save(savedQna.get());
                    log.info("1:1 문의 게시글 답변 등록이 완료되었습니다.");

                    // 답변 등록 안내 메일 보내기
                    String title = "[Kokonut] 문의글 답변 등록 안내 메일입니다.";
                    String contents = "문의하신 글에 답변이 등록되었습니다.<br> 답변내용 : "+savedQna.get().getQnaAnswer();
                    // TODO : 답변 내용을 조회, 해당 내용을 HTML 태그를 붙여서 메일로 전송해준다. 화면단과 개발할 때 추가 개발해야함.
                    mailSender.sendMail(jwtFilterDto.getEmail(), null, title, contents);
                }
            }else{
                log.error("답변을 등록할 문의글을 확인해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO053.getCode(), ResponseErrorCode.KO053.getDesc()));
            }
            return ResponseEntity.ok(res.success(data));
        }
    };
}
