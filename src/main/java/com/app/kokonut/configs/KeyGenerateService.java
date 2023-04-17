package com.app.kokonut.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Woody
 * Date : 2023-01-17
 * Remark : 키값을 생성해주는 서비스
 */
@Slf4j
@Service
public class KeyGenerateService {

    @PersistenceContext
    private EntityManager em;

    // 사용법
    // keyGenerateService.keyGenerate("선언된 auto_key(사용할 테이블명)", 어떤형태인지 키생성(DB remark에 기록할 것), 사용자IDX 또는 아이디);
    // 반환 값은 고유번호(문자형)을 반환한다.
    @Transactional
    public String keyGenerate(String autokey,String prefix,String userid){
        StoredProcedureQuery proc = em.createStoredProcedureQuery("proc_autonum"); // DB내의 "proc_autonum"라는 프로시저 실행

        proc.registerStoredProcedureParameter("autokey", String.class, ParameterMode.IN);
        proc.registerStoredProcedureParameter("prefix", String.class, ParameterMode.IN);
        proc.registerStoredProcedureParameter("userid", String.class, ParameterMode.IN);
        proc.registerStoredProcedureParameter("keycode", String.class, ParameterMode.OUT);

        proc.setParameter("autokey",autokey);
        proc.setParameter("prefix",prefix);
        proc.setParameter("userid",userid);
        proc.execute();

        String keycode = (String) proc.getOutputParameterValue("keycode");
        log.info("키생성호출 autokey = '" + autokey + "' prefix = '" + prefix + "' userid ='" + userid + " 생성된키 : =" + keycode + "' ");
        return keycode;
    }

}
