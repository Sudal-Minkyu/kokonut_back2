package com.app.kokonut.totalDBDownload;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.company.company.QCompany;
import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadListDto;
import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadSearchDto;
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
 * Date : 2023-01-13
 * Time :
 * Remark : TotalDbDownloadRepositoryCustom 쿼리문 선언부
 */
@Repository
public class TotalDbDownloadRepositoryCustomImpl extends QuerydslRepositorySupport implements TotalDbDownloadRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public TotalDbDownloadRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(TotalDbDownload.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Page<TotalDbDownloadListDto> findByTotalDbDownloadList(TotalDbDownloadSearchDto totalDbDownloadSearchDto, String businessNumber, Pageable pageable) {

        QTotalDbDownload totalDbDownload  = QTotalDbDownload.totalDbDownload;
        QAdmin admin  = QAdmin.admin;
        QCompany company  = QCompany.company;

        JPQLQuery<TotalDbDownloadListDto> query = from(totalDbDownload)
                .innerJoin(admin).on(totalDbDownload.adminId.eq(admin.adminId))
                .innerJoin(company).on(company.companyId.eq(admin.companyId))
                .select(Projections.constructor(TotalDbDownloadListDto.class,
                        totalDbDownload.tdId,
                        admin.knName,
                        totalDbDownload.tdReason,
                        totalDbDownload.tdApplyDate,
                        totalDbDownload.tdState,
                        totalDbDownload.tdReturnReason,
                        totalDbDownload.tdDownloadDate
                ));

        if(totalDbDownloadSearchDto.getTdState() != null){
            query.where(totalDbDownload.tdState.eq(totalDbDownloadSearchDto.getTdState()));
        }

        if(totalDbDownloadSearchDto.getStimeStart() != null){
            query.where(totalDbDownload.insert_date.goe(totalDbDownloadSearchDto.getStimeStart()));
        }

        if(totalDbDownloadSearchDto.getStimeEnd() != null){
            query.where(totalDbDownload.insert_date.loe(totalDbDownloadSearchDto.getStimeEnd()));
        }

        query.orderBy(totalDbDownload.tdId.desc());

        final List<TotalDbDownloadListDto> totalDbDownloadListDtoList = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(totalDbDownloadListDtoList, pageable, query.fetchCount());
    }

}
