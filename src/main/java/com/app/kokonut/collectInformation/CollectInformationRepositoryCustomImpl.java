package com.app.kokonut.collectInformation;

import com.app.kokonut.collectInformation.dtos.CollectInfoDetailDto;
import com.app.kokonut.collectInformation.dtos.CollectInfoListDto;
import com.app.kokonut.collectInformation.dtos.CollectInfoSearchDto;
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
 * @author Joy
 * Date : 2023-01-04
 * Time :
 * Remark : CollectInformationRepository 개인정보 처리방침 - 개인정보 수집 및 이용 안내 쿼리 선언부
 */
@Repository
public class CollectInformationRepositoryCustomImpl extends QuerydslRepositorySupport implements CollectInformationRepositoryCustom {
    public final JpaResultMapper jpaResultMapper;

    public CollectInformationRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CollectInformation.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Page<CollectInfoListDto> findCollectInfoPage(Long companyId, CollectInfoSearchDto collectInfoSearchDto, Pageable pageable) {
        /*
           SELECT `IDX`
	            , `TITLE`
	            , `REGISTER_NAME`
	            , `REGDATE`
             FROM `collect_information`
            WHERE 1 = 1
	          AND ( `TITLE` LIKE CONCAT('%', #{searchText}, '%') )
	          AND `COMPANY_IDX` = #{companyId}
            ORDER BY `REGDATE` DESC
         */
        QCollectInformation collectInfo = QCollectInformation.collectInformation;
        JPQLQuery<CollectInfoListDto> query = from(collectInfo)
                .select(Projections.constructor(CollectInfoListDto.class,
                        collectInfo.ciId,
                        collectInfo.ciTitle,
                        collectInfo.insert_email,
                        collectInfo.insert_date
                        ));
        query.where(collectInfo.companyId.eq(companyId));
        // 조건에 따른 where 절 추가
        if(collectInfoSearchDto.getSearchText() != null){
            query.where(collectInfo.ciTitle.contains(collectInfoSearchDto.getSearchText()));
        }
        query.orderBy(collectInfo.insert_date.desc());

        final List<CollectInfoListDto> collectInfoListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(collectInfoListDtos, pageable, query.fetchCount());
    }

    @Override
    public CollectInfoDetailDto findCollectInfoByIdx(Long ciId) {
        /*
           SELECT `IDX`
                , `TITLE`
	            , `CONTENT`
             FROM `collect_information`
            WHERE 1 = 1
	          AND `IDX` = #{idx}
         */
        QCollectInformation collectInfo = QCollectInformation.collectInformation;
        JPQLQuery<CollectInfoDetailDto> query = from(collectInfo)
                .select(Projections.constructor(CollectInfoDetailDto.class,
                        collectInfo.ciId,
                        collectInfo.ciTitle,
                        collectInfo.ciContent
                ));
        query.where(collectInfo.ciId.eq(ciId));
        return query.fetchOne();
    }

//    @Override
//    public List findCollectInfoIdxByCompayId(Long companyId) {
//        /*
//           SELECT `IDX`
//             FROM `collect_information`
//            WHERE 1 = 1
//	          AND `COMPANY_IDX` = #{companyId}
//         */
//        QCollectInformation collectInfo = QCollectInformation.collectInformation;
//        JPQLQuery<Long> query = from(collectInfo)
//                .select(Projections.constructor(Integer.class,
//                        collectInfo.idx
//                ));
//        query.where(collectInfo.companyId.eq(companyId));
//        return query.fetchAll().fetch();
//    }
}
