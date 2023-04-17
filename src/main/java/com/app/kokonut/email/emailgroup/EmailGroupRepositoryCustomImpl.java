package com.app.kokonut.email.emailgroup;

import com.app.kokonut.email.emailgroup.dtos.EmailGroupAdminInfoDto;
import com.app.kokonut.email.emailgroup.dtos.EmailGroupListDto;
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
 * @author joy
 * Date : 2022-12-22
 * Time :
 * Remark : EmailGroupRepositoryCustom 쿼리문 선언부
 */
@Repository
public class EmailGroupRepositoryCustomImpl extends QuerydslRepositorySupport implements EmailGroupRepositoryCustom {
    public final JpaResultMapper jpaResultMapper;

    public EmailGroupRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(EmailGroup.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // EmailGroup 단일 조회
    // param : idx emailGroupIdx
    @Override
    public EmailGroupAdminInfoDto findEmailGroupAdminInfoByIdx(Long egId) {
        /*
           SELECT `ADMIN_IDX_LIST`
		    FROM `email_group`
		   WHERE 1 = 1
			AND `USE_YN` = 'Y'
			AND `IDX` = #{idx}
		*/
        QEmailGroup emailGroup = QEmailGroup.emailGroup;

        JPQLQuery<EmailGroupAdminInfoDto> query = from(emailGroup)
                .where(emailGroup.egId.eq(egId),emailGroup.egUseYn.eq("Y"))
                .select(Projections.constructor(EmailGroupAdminInfoDto.class,
                        emailGroup.egAdminIdList
                ));

        return query.fetchOne();
    }

    @Override
    public Page<EmailGroupListDto> findEmailGroupPage(Pageable pageable) {
        /* SELECT 'IDX'
                , 'ADMIN_IDX_LIST'
                , 'NAME'
                , 'DESC'
             FROM 'email_group'
            WHERE 1 = 1
              AND `USE_YN` = 'Y'
            ORDER BY `REGDATE` DESC
         */

        QEmailGroup emailGroup  = QEmailGroup.emailGroup;

        JPQLQuery<EmailGroupListDto> query = from(emailGroup)
                .where(emailGroup.egUseYn.eq("Y"))
                .select(Projections.constructor(EmailGroupListDto.class,
                                emailGroup.egId,
                                emailGroup.egAdminIdList,
                                emailGroup.egName,
                                emailGroup.egDesc
                ));
        query.orderBy(emailGroup.insert_date.desc());

        final List<EmailGroupListDto> emailGroupListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(emailGroupListDtos, pageable, query.fetchCount());
    }

    // EmailGroupList 조회 페이징 처리를 위한 pageable를 포함하지 않는다.
    @Override
    public List<EmailGroupListDto> findEmailGroupDetails() {
        QEmailGroup emailGroup  = QEmailGroup.emailGroup;
        JPQLQuery<EmailGroupListDto> query = from(emailGroup)
                .where(emailGroup.egUseYn.eq("Y"))
                .select(Projections.constructor(EmailGroupListDto.class,
                        emailGroup.egId,
                        emailGroup.egAdminIdList,
                        emailGroup.egName,
                        emailGroup.egDesc
                ));
        query.orderBy(emailGroup.insert_date.desc());

        return query.fetch();
    }

}
