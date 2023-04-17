package com.app.kokonut.revisedDocument;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.Admin;
import com.app.kokonut.keydata.KeyDataService;
import com.app.kokonut.revisedDocument.dtos.RevDocSaveDto;
import com.app.kokonut.revisedDocument.dtos.RevDocListDto;
import com.app.kokonut.revisedDocument.dtos.RevDocSearchDto;
import com.app.kokonut.revisedDocumentFile.RevisedDocumentFile;
import com.app.kokonut.revisedDocumentFile.RevisedDocumentFileRepository;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.AwsS3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Joy
 * Date : 2023-01-04
 * Time :
 * Remark : RevisedDocumentService 개인정보 처리방침 - 개정문서 서비스
 */
@Slf4j
@Service
public class RevisedDocumentService {
    private final AjaxResponse res = new AjaxResponse();
    private final HashMap<String, Object> data = new HashMap<>();

    // 파일 등록
    // @Value("/revise-docs/")
    private final String revDocS3Folder = "/revise-docs/"; // TODO keyData 방식으로 추후 변경

    @Value("${kokonut.aws.s3.url}")
    private String AWSURL;

    private final AwsS3Util awsS3Util;

    private final RevisedDocumentRepository revisedDocumentRepository;
    private final AdminRepository adminRepository;
    private final RevisedDocumentFileRepository revisedDocumentFileRepository;

    public RevisedDocumentService(KeyDataService keyDataService,
                                  AwsS3Util awsS3Util,
                                  RevisedDocumentRepository revisedDocumentRepository,
                                  AdminRepository adminRepository,
                                  RevisedDocumentFileRepository revisedDocumentFileRepository) {
//        this.AWSURL = keyDataService.findByKeyValue("aws_s3_url");
        this.awsS3Util = awsS3Util;
        this.adminRepository = adminRepository;
        this.revisedDocumentRepository = revisedDocumentRepository;
        this.revisedDocumentFileRepository = revisedDocumentFileRepository;
    }

    public ResponseEntity<Map<String, Object>> revDocList(String userRole, String email, RevDocSearchDto revDocSearchDto, Pageable pageable) {
        log.info("revDocList 호출, userRole : " + userRole);
        // 접속 정보에서 idx 가져오기
        Admin admin = adminRepository.findByKnEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));
        // TODO 메뉴 권한에 대한 체크 부분
        if ("[MASTER]".equals(userRole) || "[ADMIN]".equals(userRole)) {
            Long companyId = admin.getCompanyId();
            log.info("개정문서 목록 조회");
            Page<RevDocListDto> revDocListDtos = revisedDocumentRepository.findRevDocPage(companyId, revDocSearchDto, pageable);
            return ResponseEntity.ok(res.ResponseEntityPage(revDocListDtos));
        } else {
            log.error("접근권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> revDocSave(String userRole, String email, RevDocSaveDto revDocSaveDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("revDocSave 호출, userRole : " + userRole);
        // TODO 메뉴 권한에 대한 체크 부분
        if ("[MASTER]".equals(userRole) || "[ADMIN]".equals(userRole)) {
            log.info("개정문서 등록 시작");
            RevisedDocument revDoc = new RevisedDocument();

            // 접속 정보에서 idx 가져오기
            Admin admin = adminRepository.findByKnEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));
            Long adminId = admin.getAdminId();

            revDoc.setAdminId(adminId);
            revDoc.setInsert_email(email);
            revDoc.setInsert_date(LocalDateTime.now());
            revDoc.setRdEnforceStartDate(revDocSaveDto.getRdEnforceStartDate());
            revDoc.setRdEnforceEndDate(revDocSaveDto.getRdEnforceEndDate());

            Long savedId = revisedDocumentRepository.save(revDoc).getRdId();
            log.info("개정문서 등록 완료, idx : "+ savedId);

            if(savedId != null){
                log.info("개정문서 파일 업로드 처리 시작, idx : "+savedId);
                // 파일 업로드 처리
                MultipartFile multipartFile = revDocSaveDto.getMultipartFile();
                if(multipartFile.isEmpty()){
                    log.info("첨부파일 없음.");
                }else{
                    log.info("첨부파일 있음. 파일 업로드 시작.");
                    RevisedDocumentFile revDocFile = new RevisedDocumentFile();
                    revDocFile.setInsert_email(email);
                    revDocFile.setRdfId(savedId);

                    // file original name
                    String originalFilename = Normalizer.normalize(Objects.requireNonNull(multipartFile.getOriginalFilename()), Normalizer.Form.NFC);
                    log.info("originalFilename : "+originalFilename);
                    revDocFile.setRdfOriginalFilename(originalFilename);

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
                    revDocFile.setRdfFilename(fileName);

                    // S3에 저장 할 파일 주소
                    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                    String filePath = AWSURL+revDocS3Folder+date.format(new Date());
                    log.info("filePath : "+filePath);
                    revDocFile.setRdfPath(filePath);

                    // S3에 파일 업로드
                    awsS3Util.nomalFileUpload(multipartFile, fileName, revDocS3Folder+date.format(new Date()));
                    // 파일 저장
                    Long fileIdx = revisedDocumentFileRepository.save(revDocFile).getRdId();
                    log.info("첨부 파일 저장에 성공햇습니다. idx : " + fileIdx);
                }
            }else{
                log.error("개정문서 등록 처리 중 문제가 생겼습니다. 관리자에게 문의하세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO056.getCode(), ResponseErrorCode.KO056.getDesc()));
            }
                return ResponseEntity.ok(res.success(data));
        } else {
            log.error("접근권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }

//    @Transactional
//    public ResponseEntity<Map<String, Object>> revDocDelete(String userRole, String email, Integer idx){
//        log.info("revDocDelete 호출, userRole : " +userRole);
//        // TODO 메뉴 권한에 대한 체크 부분
//        if("[MASTER]".equals(userRole) || "[ADMIN]".equals(userRole)){
//            if(idx != null){
//                log.info("처리방침 개정문서 삭제 시작.");
//                revisedDocumentRepository.deleteById(idx);
//                log.info("처리방침 개정문서 삭제 완료. idx : "+idx);
//                // TODO 파일 삭제
//                return ResponseEntity.ok(res.success(data));
//            }else{
//                log.error("idx 값을 확인 할 수 없습니다.");
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
//            }
//        }else {
//            log.error("접근권한이 없습니다. userRole : " + userRole);
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
//        }
//    }
}
