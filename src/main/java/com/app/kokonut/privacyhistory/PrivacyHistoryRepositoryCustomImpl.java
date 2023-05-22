package com.app.kokonut.privacyhistory;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryCode;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryListDto;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistorySearchDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-05-15
 * Time :
 * Remark : PrivacyHistoryRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PrivacyHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements PrivacyHistoryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PrivacyHistoryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PrivacyHistory.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public Page<PrivacyHistoryListDto> findByPrivacyHistoryList(PrivacyHistorySearchDto privacyHistorySearchDto, Pageable pageable) {

        QPrivacyHistory privacyHistory = QPrivacyHistory.privacyHistory;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<PrivacyHistoryListDto> query = from(privacyHistory)
                .innerJoin(admin).on(admin.adminId.eq(privacyHistory.adminId))
                .select(Projections.constructor(PrivacyHistoryListDto.class,
                        admin.knName,
                        admin.knEmail,
                        admin.knRoleCode,
                        admin.knRoleCode,
                        privacyHistory.privacyHistoryCode,
                        privacyHistory.insert_date,
                        privacyHistory.kphIpAddr
                ));

        // 조회한 기업의 한해서만 조회되야함
        query.where(admin.companyId.eq(privacyHistorySearchDto.getCompanyId())).orderBy(privacyHistory.kphId.desc());;

        if(!privacyHistorySearchDto.getFilterState().equals("")) {
            query.where(privacyHistory.privacyHistoryCode.eq(PrivacyHistoryCode.valueOf(privacyHistorySearchDto.getFilterState())));
        }

        if(!privacyHistorySearchDto.getFilterRole().equals("")) {
            query.where(admin.knRoleCode.eq(AuthorityRole.valueOf(privacyHistorySearchDto.getFilterRole())));
        }

        final List<PrivacyHistoryListDto> privacyHistoryListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(privacyHistoryListDtos, pageable, query.fetchCount());
    }


}
