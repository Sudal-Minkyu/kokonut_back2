package com.app.kokonutapi.auth;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonutapi.auth.dtos.AuthApiLoginDto;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-04-27
 *
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class AuthApiService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    private final KokonutUserService kokonutUserService;

    @Autowired
    public AuthApiService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, KokonutUserService kokonutUserService){
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.kokonutUserService = kokonutUserService;
    }

    // API용 개인정보(고객의 고객) 로그인
    public ResponseEntity<Map<String, Object>> apiLogin(AuthApiLoginDto authApiLoginDto, JwtFilterDto jwtFilterDto) {
        log.info("apiLogin 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("authApiLoginDto : "+authApiLoginDto);

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

//        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();
//        log.info("adminId : "+adminId);
//        log.info("companyCode : "+companyCode);

        String basicTable = companyCode+"_1";

        if(authApiLoginDto.getKokonutId() == null) {
            return ResponseEntity.ok(res.apifail("KOKONUT_01", "아이디를 입력해주세요."));
        }
        Long result = kokonutUserService.passwordConfirm(basicTable, authApiLoginDto.getKokonutId(), authApiLoginDto.getKokonutPw());
        log.info("result : "+result);

        data.put("IDX", result);
        if(0 < result) {
            return ResponseEntity.ok(res.apisuccess(data));
        } else if(0 == result) {
            return ResponseEntity.ok(res.apifail("KOKONUT_02", "존재하지 않은 아이디 입니다."));
        } else {
            return ResponseEntity.ok(res.apifail("KOKONUT_03", "입력한 비밀번호가 맞지 않습니다."));
        }

    }

    // API용 개인정보(고객의 고객) 회원가입
    public ResponseEntity<Map<String, Object>> apiRegister(JwtFilterDto jwtFilterDto) {
        log.info("apiRegister 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        return ResponseEntity.ok(res.success(data));
    }




}
