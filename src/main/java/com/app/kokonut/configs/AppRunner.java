package com.app.kokonut.configs;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companysetting.CompanySetting;
import com.app.kokonut.company.companysetting.CompanySettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2022-10-26
 * Time :
 * Remark : 실행전 필수 데이터 생성
 */
@Component
public class AppRunner implements ApplicationRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeyGenerateService keyGenerateService;
    private final CompanyRepository companyRepository;
    private final CompanySettingRepository companySettingRepository;

    @Autowired
    public AppRunner(AdminRepository adminRepository,
                     PasswordEncoder passwordEncoder, KeyGenerateService keyGenerateService,
                     CompanyRepository companyRepository, CompanySettingRepository companySettingRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.keyGenerateService = keyGenerateService;
        this.companyRepository = companyRepository;
        this.companySettingRepository = companySettingRepository;
    }

    @Override
    public void run(ApplicationArguments args) {

        // 시스템관리자 계정생성
        String system_knEmail = "system@kokonut.me";
        if(!adminRepository.existsByKnEmail(system_knEmail)) { // 시스템계정이 등록되어있지 않았으면 실행

            String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            String cpCode = keyGenerateService.keyGenerate("kn_company", "kokonut"+nowDate, "KokonutSystem");

            Company systemCompany = new Company();
            systemCompany.setCpCode(cpCode);
            systemCompany.setCpName("2월대개봉 시스템");
            systemCompany.setCpTableCount(1);
            systemCompany.setCpSubscribe("0");
            systemCompany.setInsert_email("KokonutSystem");
            systemCompany.setInsert_date(LocalDateTime.now());
            Company saveCompany = companyRepository.save(systemCompany);

            CompanySetting companySetting = new CompanySetting();
            companySetting.setCpCode(cpCode);
            companySetting.setCsOverseasBlockSetting("0");
            companySetting.setCsAccessSetting("0");
            companySetting.setCsPasswordChangeSetting("12");
            companySetting.setCsPasswordErrorCountSetting("5");
            companySetting.setCsAutoLogoutSetting("30");
            companySetting.setCsLongDisconnectionSetting("0");
            companySetting.setInsert_email("KokonutSystem");
            companySetting.setInsert_date(LocalDateTime.now());
            companySettingRepository.save(companySetting);

            Admin systemAdmin = new Admin();
            systemAdmin.setCompanyId(saveCompany.getCompanyId());
            systemAdmin.setKnName("코코넛시스템");
            systemAdmin.setKnRoleCode(AuthorityRole.ROLE_SYSTEM);
            systemAdmin.setMasterId(0L);
            systemAdmin.setKnUserType(0);
            systemAdmin.setKnRegType(1);
            systemAdmin.setKnOtpKey("system");
            systemAdmin.setKnPhoneNumber("01000001111");
            systemAdmin.setKnEmail(system_knEmail);
            systemAdmin.setKnPassword(passwordEncoder.encode("kokonut2!")); //->개발완료후 비밀번호는 환경변수로셋팅할 것.
            systemAdmin.setInsert_email("KokonutSystem");
            systemAdmin.setInsert_date(LocalDateTime.now());
            adminRepository.save(systemAdmin);

        }





        // 체험하기용 계정생성

    }

}
