package com.app.kokonut.bizMessage.alimtalkTemplate;

import com.app.kokonut.bizMessage.alimtalkTemplate.dto.AlimtalkMessageTemplateInfoListDto;
import com.app.kokonut.bizMessage.alimtalkTemplate.dto.AlimtalkTemplateInfoListDto;
import com.app.kokonut.bizMessage.alimtalkTemplate.dto.AlimtalkTemplateListDto;
import com.app.kokonut.bizMessage.alimtalkTemplate.dto.AlimtalkTemplateSearchDto;
import com.app.kokonut.bizMessage.kakaoChannel.QKakaoChannel;
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
 * Remark : AlimtalkTemplateRepositoryCustomImpl 쿼리문 선언부
 */
@Repository
public class AlimtalkTemplateRepositoryCustomImpl extends QuerydslRepositorySupport implements AlimtalkTemplateRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public AlimtalkTemplateRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(AlimtalkTemplate.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Page<AlimtalkTemplateListDto> findByAlimtalkTemplatePage(AlimtalkTemplateSearchDto alimtalkTemplateSearchDto, Long companyId, Pageable pageable) {

        QAlimtalkTemplate alimtalkTemplate  = QAlimtalkTemplate.alimtalkTemplate;
        QKakaoChannel kakaoChannel = QKakaoChannel.kakaoChannel;

        JPQLQuery<AlimtalkTemplateListDto> query = from(alimtalkTemplate)
                .innerJoin(kakaoChannel).on(kakaoChannel.kcChannelId.eq(alimtalkTemplate.kcChannelId))
                .where(kakaoChannel.companyId.eq(companyId))
                .select(Projections.constructor(AlimtalkTemplateListDto.class,
                        alimtalkTemplate.kcChannelId,
                        alimtalkTemplate.atTemplateCode,
                        alimtalkTemplate.atTemplateName,
                        alimtalkTemplate.insert_date,
                        alimtalkTemplate.atStatus
                ));

        if(alimtalkTemplateSearchDto.getAtTemplateName() != null){
            query.where(alimtalkTemplate.atTemplateName.containsIgnoreCase(alimtalkTemplateSearchDto.getAtTemplateName()));
        }

        if(alimtalkTemplateSearchDto.getAtStatus() != null){
            query.where(alimtalkTemplate.atStatus.eq(alimtalkTemplateSearchDto.getAtStatus()));
        }

        if(alimtalkTemplateSearchDto.getStimeStart() != null){
            query.where(alimtalkTemplate.insert_date.goe(alimtalkTemplateSearchDto.getStimeStart()));
        }

        if(alimtalkTemplateSearchDto.getStimeEnd() != null){
            query.where(alimtalkTemplate.insert_date.loe(alimtalkTemplateSearchDto.getStimeEnd()));
        }

        query.orderBy(alimtalkTemplate.insert_date.desc());

        final List<AlimtalkTemplateListDto> alimtalkTemplateListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(alimtalkTemplateListDtos, pageable, query.fetchCount());
    }

    @Override
    public List<AlimtalkTemplateInfoListDto> findByAlimtalkTemplateInfoList(Long companyId, String channelId, String state) {

        QAlimtalkTemplate alimtalkTemplate = QAlimtalkTemplate.alimtalkTemplate;
        QKakaoChannel kakaoChannel = QKakaoChannel.kakaoChannel;

        JPQLQuery<AlimtalkTemplateInfoListDto> query = from(alimtalkTemplate)
                .innerJoin(kakaoChannel).on(kakaoChannel.kcChannelId.eq(alimtalkTemplate.kcChannelId))
                .where(alimtalkTemplate.kcChannelId.eq(channelId).and(kakaoChannel.companyId.eq(companyId)))
                .select(Projections.constructor(AlimtalkTemplateInfoListDto.class,
                        alimtalkTemplate.atTemplateCode,
                        alimtalkTemplate.atStatus
                ));

        if(state.equals("1")){
            query.where(alimtalkTemplate.atStatus.eq("ACCEPT").or(alimtalkTemplate.atStatus.eq("REGISTER")).or(alimtalkTemplate.atStatus.eq("INSPECT")));
        }

        return query.fetch();
    }

    @Override
    public List<AlimtalkMessageTemplateInfoListDto> findByAlimtalkMessageTemplateInfoList(Long companyId, String channelId) {

        QAlimtalkTemplate alimtalkTemplate = QAlimtalkTemplate.alimtalkTemplate;
        QKakaoChannel kakaoChannel = QKakaoChannel.kakaoChannel;

        JPQLQuery<AlimtalkMessageTemplateInfoListDto> query = from(alimtalkTemplate)
                .innerJoin(kakaoChannel).on(kakaoChannel.kcChannelId.eq(alimtalkTemplate.kcChannelId))
                .where(alimtalkTemplate.kcChannelId.eq(channelId).and(kakaoChannel.companyId.eq(companyId)))
                .select(Projections.constructor(AlimtalkMessageTemplateInfoListDto.class,
                        alimtalkTemplate.atTemplateCode,
                        alimtalkTemplate.atMessageType,
                        alimtalkTemplate.atExtraContent,
                        alimtalkTemplate.atAdContent,
                        alimtalkTemplate.atEmphasizeType,
                        alimtalkTemplate.atEmphasizeTitle,
                        alimtalkTemplate.atEmphasizeSubTitle
                ));

        return query.fetch();
    }



}
