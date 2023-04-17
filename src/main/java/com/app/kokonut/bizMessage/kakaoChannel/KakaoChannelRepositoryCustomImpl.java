package com.app.kokonut.bizMessage.kakaoChannel;

import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelByChannelIdListDto;
import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelListDto;
import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelSearchDto;
import com.app.kokonut.company.company.QCompany;
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
 * Date : 2022-12-15
 * Time :
 * Remark : KakaoChannelRepositoryCustom 쿼리문 선언부
 */
@Repository
public class KakaoChannelRepositoryCustomImpl extends QuerydslRepositorySupport implements KakaoChannelRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public KakaoChannelRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(KakaoChannel.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Page<KakaoChannelListDto> findByKakaoChannelPage(KakaoChannelSearchDto kakaoChannelSearchDto, Long companyId, Pageable pageable) {

        QKakaoChannel kakaoChannel  = QKakaoChannel.kakaoChannel;
        QCompany company  = QCompany.company;

        JPQLQuery<KakaoChannelListDto> query = from(kakaoChannel)
                .innerJoin(company).on(company.companyId.eq(companyId))
                .select(Projections.constructor(KakaoChannelListDto.class,
                        kakaoChannel.kcId,
                        kakaoChannel.companyId,
                        kakaoChannel.kcChannelId,
                        kakaoChannel.kcChannelName,
                        kakaoChannel.kcStatus,
                        kakaoChannel.insert_date,
                        company.cpName
                ));

        if(kakaoChannelSearchDto.getKcChannelName() != null){
            query.where(kakaoChannel.kcChannelName.containsIgnoreCase(kakaoChannelSearchDto.getKcChannelName()));
        }

        if(kakaoChannelSearchDto.getKcStatus() != null){
            query.where(kakaoChannel.kcStatus.eq(kakaoChannelSearchDto.getKcStatus()));
        }

        if(kakaoChannelSearchDto.getStimeStart() != null){
            query.where(kakaoChannel.insert_date.goe(kakaoChannelSearchDto.getStimeStart()));
        }

        if(kakaoChannelSearchDto.getStimeEnd() != null){
            query.where(kakaoChannel.insert_date.loe(kakaoChannelSearchDto.getStimeEnd()));
        }

        query.orderBy(kakaoChannel.insert_date.desc());

        final List<KakaoChannelListDto> kakaoChannelListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(kakaoChannelListDtos, pageable, query.fetchCount());
    }

    @Override
    public List<KakaoChannelByChannelIdListDto> findByKakaoChannelIdList(Long companyId) {

        QKakaoChannel kakaoChannel = QKakaoChannel.kakaoChannel;
        QCompany company = QCompany.company;

        JPQLQuery<KakaoChannelByChannelIdListDto> query = from(kakaoChannel)
                .innerJoin(company).on(company.companyId.eq(companyId))
                .where(kakaoChannel.companyId.eq(company.companyId))
                .select(Projections.constructor(KakaoChannelByChannelIdListDto.class,
                        kakaoChannel.kcChannelId
                ));

        return query.fetch();
    }





}
