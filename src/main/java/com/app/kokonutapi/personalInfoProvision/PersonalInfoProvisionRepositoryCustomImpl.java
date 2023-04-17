package com.app.kokonutapi.personalInfoProvision;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionListDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionMapperDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionNumberDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2022-10-25
 * Time :
 * Remark : PersonalInfoProvisionRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PersonalInfoProvisionRepositoryCustomImpl extends QuerydslRepositorySupport implements PersonalInfoProvisionRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PersonalInfoProvisionRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PersonalInfoProvision.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 관리번호 조회
    @Override
    public PersonalInfoProvisionNumberDto findByPersonalInfoProvisionNumber(String prefix) {

        QPersonalInfoProvision personalInfoProvision = QPersonalInfoProvision.personalInfoProvision;

        JPQLQuery<PersonalInfoProvisionNumberDto> query = from(personalInfoProvision)
                .where(personalInfoProvision.piNumber.like("%"+ prefix +"%")).limit(1)
                .select(Projections.constructor(PersonalInfoProvisionNumberDto.class,
                        personalInfoProvision.piNumber
                ));

        return query.fetchOne();
    }

    // PersonalInfoProvision 조회
    @Override
    public PersonalInfoProvisionDto findByNumberProvision(String piNumber) {

        QPersonalInfoProvision personalInfoProvision = QPersonalInfoProvision.personalInfoProvision;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<PersonalInfoProvisionDto> query = from(personalInfoProvision)
                .innerJoin(admin).on(admin.adminId.eq(personalInfoProvision.adminId))
                .where(personalInfoProvision.piNumber.eq(piNumber))
                .select(Projections.constructor(PersonalInfoProvisionDto.class,
                        personalInfoProvision.piId,
                        personalInfoProvision.adminId,
                        personalInfoProvision.piNumber,
                        personalInfoProvision.piReason,
                        personalInfoProvision.piType,
                        personalInfoProvision.piRecipientType,
                        personalInfoProvision.piAgreeYn,
                        personalInfoProvision.piAgreeType,
                        personalInfoProvision.piPurpose,
                        personalInfoProvision.piTag,
                        personalInfoProvision.piStartDate,
                        personalInfoProvision.piExpDate,
                        personalInfoProvision.piRetentionPeriod,
                        personalInfoProvision.piColumns,
                        personalInfoProvision.piRecipientEmail,
                        personalInfoProvision.piTargets,
                        personalInfoProvision.piTargetStatus,
                        personalInfoProvision.insert_date,
                        admin.knName
                ));

        return query.fetchOne();
    }

    // PersonalInfoProvision 리스트 조회
    @Override
    public List<PersonalInfoProvisionListDto> findByProvisionList(PersonalInfoProvisionMapperDto personalInfoProvisionMapperDto) {

        // SQL Query
//        select * from (
//                select
//                a.IDX as idx,
//                a.COMPANY_IDX as companyId,
//                a.ADMIN_IDX as adminId,
//                a.NUMBER as number,
//                a.REASON as reason,
//                a.TYPE as type,
//                a.RECIPIENT_TYPE as recipientType,
//                a.AGREE_YN as agreeYn,
//                a.AGREE_TYPE as agreeType,
//                a.REGDATE as regdate,
//                a.PURPOSE as purpose,
//                a.TAG as tag,
//                a.START_DATE as startDate,
//                a.EXP_DATE as expDate,
//                a.PERIOD as period,
//                a.RETENTION_PERIOD as retentionPeriod,
//                a.COLUMNS as columns,
//                a.RECIPIENT_EMAIL as recipientEmail,
//                a.TARGETS as targets,
//                a.TARGET_STATUS as targetStatus,
//
//                b.NAME as adminName,
//
//                c.IDX as downloadHistoryIdx,
//                c.REGDATE as downloadHistoryRegdate,
//                c.RETENTION_DATE as downloadHistoryRetentionDate,
//                c.EMAIL as downloadHistoryEmail,
//                c.FILE_NAME as downloadHistoryFileName,
//                c.AGREE_YN as downloadHistoryAgreeYn,
//                c.DESTRUCTION_FILE_GROUP_ID as destructionFileGroupId,
//                c.DESTRUCTION_AGREE_YN as destructionAgreeYn,
//                c.DESTRUCTION_DATE as destructionDate,
//                c.DESTRUNCTION_REGISTER_NAME as destrunctionRegisterName,
//
//                CASE
//                WHEN NOW() < a.START_DATE  THEN 1
//                WHEN c.REGDATE IS NULL AND NOW() >= a.START_DATE AND NOW() <= a.EXP_DATE THEN 2
//                WHEN c.REGDATE IS NULL AND NOW() > EXP_DATE THEN 3
//                WHEN c.REGDATE IS NOT NULL AND NOW() < c.RETENTION_DATE THEN 4
//                WHEN c.REGDATE IS NOT NULL AND NOW() >= c.RETENTION_DATE THEN 5
//                ELSE 0
//                END as state
//
//                from personal_info_provision a
//                LEFT JOIN admin b on b.IDX = a.ADMIN_IDX
//                LEFT JOIN personal_info_download_history c ON c.NUMBER= a.NUMBER
//
//                where 1=1
//                AND a.COMPANY_IDX = 13
//
//        ) as sub
//        where 1=1
//        AND sub.state = 1
//        ORDER BY sub.regdate DESC

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        // 네이티브 쿼리문
        sb.append("SELECT * FROM ( \n");
        sb.append("SELECT \n");
        sb.append("a.IDX as idx, a.COMPANY_IDX as companyId, a.ADMIN_IDX as adminId, \n");
        sb.append("a.NUMBER as number, a.REASON as reason, a.TYPE as type, a.RECIPIENT_TYPE as recipientType, \n");
        sb.append("a.AGREE_YN as agreeYn, a.AGREE_TYPE as agreeType, a.REGDATE as regdate, \n");
        sb.append("a.PURPOSE as purpose, a.TAG as tag, a.START_DATE as startDate, a.EXP_DATE as expDate, \n");
        sb.append("a.PERIOD as period, a.RETENTION_PERIOD as retentionPeriod, a.COLUMNS as columns, \n");
        sb.append("a.RECIPIENT_EMAIL as recipientEmail, a.TARGETS as targets, a.TARGET_STATUS as targetStatus, \n");
        sb.append("b.NAME as adminName, \n");
        sb.append("c.IDX as downloadHistoryIdx, c.REGDATE as downloadHistoryRegdate, c.RETENTION_DATE as downloadHistoryRetentionDate, \n");
        sb.append("c.EMAIL as downloadHistoryEmail, c.FILE_NAME as downloadHistoryFileName, c.AGREE_YN as downloadHistoryAgreeYn, \n");
        sb.append("c.DESTRUCTION_FILE_GROUP_ID as destructionFileGroupId, c.DESTRUCTION_AGREE_YN as destructionAgreeYn, \n");
        sb.append("c.DESTRUCTION_DATE as destructionDate, c.DESTRUNCTION_REGISTER_NAME as destrunctionRegisterName, \n");

        sb.append("CASE \n");
        sb.append("WHEN NOW() < a.START_DATE THEN 1 \n");
        sb.append("WHEN c.REGDATE IS NULL AND NOW() >= a.START_DATE AND NOW() <= a.EXP_DATE THEN 2 \n");
        sb.append("WHEN c.REGDATE IS NULL AND NOW() > EXP_DATE THEN 3 \n");
        sb.append("WHEN c.REGDATE IS NOT NULL AND NOW() < c.RETENTION_DATE THEN 4 \n");
        sb.append("WHEN c.REGDATE IS NOT NULL AND NOW() >= c.RETENTION_DATE THEN 5 \n");
        sb.append("ELSE 0 END as state \n");

        sb.append("FROM personal_info_provision a \n");
        sb.append("LEFT JOIN admin b on b.IDX = a.ADMIN_IDX \n");
        sb.append("LEFT JOIN personal_info_download_history c ON c.NUMBER= a.NUMBER \n");
        sb.append("WHERE 1=1 \n");

        sb.append("AND a.COMPANY_IDX = :companyId \n");

        if(personalInfoProvisionMapperDto.getReason() != 0 && personalInfoProvisionMapperDto.getReason() != null){
            sb.append("AND a.REASON = :reason \n");
        }

        if(personalInfoProvisionMapperDto.getAdminId() != 0 && personalInfoProvisionMapperDto.getAdminId() != null){
            sb.append("AND a.ADMIN_IDX = :adminId \n");
        }

        if(personalInfoProvisionMapperDto.getRecipientType() != 0 && personalInfoProvisionMapperDto.getRecipientType() != null){
            sb.append("AND a.RECIPIENT_TYPE = :recipientType \n");
        }

        if(!Objects.equals(personalInfoProvisionMapperDto.getRecipientEmail(), "") && personalInfoProvisionMapperDto.getRecipientEmail() != null){
            sb.append("AND a.RECIPIENT_EMAIL = :recipientEmail \n");
        }

        if(!personalInfoProvisionMapperDto.getAgreeYn().equals("") && personalInfoProvisionMapperDto.getAgreeYn() != null){
            sb.append("AND a.AGREE_YN = :agreeYn \n");
        }

        if(personalInfoProvisionMapperDto.getAgreeType() != 0 && personalInfoProvisionMapperDto.getAgreeType() != null){
            sb.append("AND a.AGREE_TYPE = :agreeType \n");
        }

        if(personalInfoProvisionMapperDto.getStimeStart() != null && personalInfoProvisionMapperDto.getStimeEnd() != null){
            sb.append("AND a.REGDATE BETWEEN :stimeStart AND :stimeEnd \n");
        }

        if(!personalInfoProvisionMapperDto.getSearchText().equals("") && personalInfoProvisionMapperDto.getSearchText() != null){
            if(personalInfoProvisionMapperDto.getSearchType().equals("AGREE") && personalInfoProvisionMapperDto.getSearchType() != null){
                sb.append("AND a.NUMBER LIKE CONCAT('%',:searchText,'%') \n");
            }else{
                sb.append("AND a.PURPOSE LIKE CONCAT('%',:searchText,'%') OR a.TAG LIKE CONCAT('%',:searchText,'%') \n");
            }
        }

        sb.append(") as sub \n");
        sb.append("WHERE 1=1 \n");
        if(personalInfoProvisionMapperDto.getState() != 0 && personalInfoProvisionMapperDto.getState() != null) {
            sb.append("AND sub.state = :state \n");
        }
        sb.append("ORDER BY sub.regdate DESC; \n");

        // 쿼리조건 선언부
        Query query = em.createNativeQuery(sb.toString());

        query.setParameter("companyId", personalInfoProvisionMapperDto.getCompanyId());

        if(personalInfoProvisionMapperDto.getState() != 0 && personalInfoProvisionMapperDto.getState() != null){
            query.setParameter("state", personalInfoProvisionMapperDto.getState());
        }

        if(personalInfoProvisionMapperDto.getReason() != 0 && personalInfoProvisionMapperDto.getReason() != null){
            query.setParameter("reason", personalInfoProvisionMapperDto.getReason());
        }

        if(personalInfoProvisionMapperDto.getAdminId() != 0 && personalInfoProvisionMapperDto.getAdminId() != null){
            query.setParameter("adminId", personalInfoProvisionMapperDto.getAdminId());
        }

        if(personalInfoProvisionMapperDto.getRecipientType() != 0 && personalInfoProvisionMapperDto.getRecipientType() != null){
            query.setParameter("recipientType", personalInfoProvisionMapperDto.getRecipientType());
        }

        if(!personalInfoProvisionMapperDto.getRecipientEmail().equals("") && personalInfoProvisionMapperDto.getRecipientEmail() != null){
            query.setParameter("recipientEmail", personalInfoProvisionMapperDto.getRecipientEmail());
        }

        if(!personalInfoProvisionMapperDto.getAgreeYn().equals("") && personalInfoProvisionMapperDto.getAgreeYn() != null){
            query.setParameter("agreeYn", personalInfoProvisionMapperDto.getAgreeYn());
        }

        if(personalInfoProvisionMapperDto.getAgreeType() != 0 && personalInfoProvisionMapperDto.getAgreeType() != null){
            query.setParameter("agreeType", personalInfoProvisionMapperDto.getAgreeType());
        }

        if(personalInfoProvisionMapperDto.getStimeStart() != null && personalInfoProvisionMapperDto.getStimeEnd() != null){
            query.setParameter("stimeStart", personalInfoProvisionMapperDto.getStimeStart());
            query.setParameter("stimeEnd", personalInfoProvisionMapperDto.getStimeEnd());
        }

        if(!personalInfoProvisionMapperDto.getSearchText().equals("") && personalInfoProvisionMapperDto.getSearchText() != null){
            query.setParameter("searchText", personalInfoProvisionMapperDto.getSearchText());
        }

        return jpaResultMapper.list(query, PersonalInfoProvisionListDto.class);
    }

}
