package com.app.kokonut.bizMessage.alimtalkMessage;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageInfoListDto;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageListDto;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageResultDetailDto;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageSearchDto;
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
 * Date : 2022-11-29
 * Time :
 * Remark : AlimtalkMessageRepositoryCustom 쿼리문 선언부
 */
@Repository
public class AlimtalkMessageRepositoryCustomImpl extends QuerydslRepositorySupport implements AlimtalkMessageRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public AlimtalkMessageRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(AlimtalkMessage.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Page<AlimtalkMessageListDto> findByAlimtalkMessagePage(AlimtalkMessageSearchDto alimtalkMessageSearchDto, Long companyId, Pageable pageable) {

        QAlimtalkMessage alimtalkMessage  = QAlimtalkMessage.alimtalkMessage;
        QAdmin admin = QAdmin.admin;
        
        JPQLQuery<AlimtalkMessageListDto> query = from(alimtalkMessage)
                .innerJoin(admin).on(admin.adminId.eq(alimtalkMessage.adminId))
                .where(admin.companyId.eq(companyId))
                .select(Projections.constructor(AlimtalkMessageListDto.class,
                        alimtalkMessage.kcChannelId,
                        alimtalkMessage.atTemplateCode,
                        alimtalkMessage.amRequestId,
                        alimtalkMessage.amStatus,
                        alimtalkMessage.insert_date
                ));

        if(alimtalkMessageSearchDto.getSearchText() != null){
            query.where(alimtalkMessage.atTemplateCode.containsIgnoreCase(alimtalkMessageSearchDto.getSearchText())
                    .or(alimtalkMessage.amRequestId.containsIgnoreCase(alimtalkMessageSearchDto.getSearchText())));
        }

        if(alimtalkMessageSearchDto.getAmStatus() != null){
            query.where(alimtalkMessage.amStatus.eq(alimtalkMessageSearchDto.getAmStatus()));
        }

        if(alimtalkMessageSearchDto.getStimeStart() != null){
            query.where(alimtalkMessage.insert_date.goe(alimtalkMessageSearchDto.getStimeStart()));
        }

        if(alimtalkMessageSearchDto.getStimeEnd() != null){
            query.where(alimtalkMessage.insert_date.loe(alimtalkMessageSearchDto.getStimeEnd()));
        }

        query.orderBy(alimtalkMessage.insert_date.desc());

        final List<AlimtalkMessageListDto> alimtalkMessageListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(alimtalkMessageListDtos, pageable, query.fetchCount());
    }

    @Override
    public List<AlimtalkMessageInfoListDto> findByAlimtalkMessageInfoList(Long companyId, String state) {

        QAlimtalkMessage alimtalkMessage = QAlimtalkMessage.alimtalkMessage;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<AlimtalkMessageInfoListDto> query = from(alimtalkMessage)
                .innerJoin(admin).on(admin.adminId.eq(alimtalkMessage.adminId))
                .where(admin.companyId.eq(companyId))
                .select(Projections.constructor(AlimtalkMessageInfoListDto.class,
                        alimtalkMessage.amId,
                        alimtalkMessage.amRequestId,
                        alimtalkMessage.amTransmitType,
                        alimtalkMessage.amStatus
                ));

        if(state.equals("1")){
            // state 값이 "1"일 경우 : statue 값이 success, fail, canceled, done, stale 은 제외한다.
            query.where(alimtalkMessage.amStatus.in("init", "processing", "reserved", "scheduled", "ready"));
        }

        return query.fetch();
    }

    @Override
    public AlimtalkMessageResultDetailDto findByAlimtalkMessageResultDetail(String amRequestId) {

        QAlimtalkMessage alimtalkMessage = QAlimtalkMessage.alimtalkMessage;

        JPQLQuery<AlimtalkMessageResultDetailDto> query = from(alimtalkMessage)
                .where(alimtalkMessage.amRequestId.eq(amRequestId))
                .select(Projections.constructor(AlimtalkMessageResultDetailDto.class,
                        alimtalkMessage.amRequestId,
                        alimtalkMessage.kcChannelId,
                        alimtalkMessage.amTransmitType,
                        alimtalkMessage.amStatus
                ));

        return query.fetchOne();
    }

}
