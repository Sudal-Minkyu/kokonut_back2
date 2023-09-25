package com.app.kokonut.provision;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.provision.dtos.ProvisionDownloadCheckDto;
import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import com.app.kokonut.provision.provisiondownloadhistory.QProvisionDownloadHistory;
import com.app.kokonut.provision.provisionroster.QProvisionRoster;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark : ProvisionRepositoryCustom 쿼리문 선언부
 */
@Repository
public class ProvisionRepositoryCustomImpl extends QuerydslRepositorySupport implements ProvisionRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public ProvisionRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Provision.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 리스트 조회
    // 6월29일 woody 기록
    // 나에게 제공받은 제공건만 리스트로 호출함.
    // 내가 제공해준거까지 리스트로 나오게되면 문제점 : 내가 제공을해줬지만 내가포함이 아닐수도있다.
    // 만약 위처럼 하게된다면 상세보기를 볼 수 없게하는 작업을 따로 해야된다.
    @Override
    public Page<ProvisionListDto> findByProvisionList(ProvisionSearchDto provisionSearchDto, Pageable pageable) {

        QProvision provision = QProvision.provision;
        QProvisionRoster provisionRoster = new QProvisionRoster("provisionRoster");
        QProvisionRoster provisionRosterCnt = new QProvisionRoster("provisionRosterCnt");

        QProvisionDownloadHistory provisionDownloadHistory = QProvisionDownloadHistory.provisionDownloadHistory;

//        QAdmin admin = QAdmin.admin;

        QAdmin admin = new QAdmin("admin");
        QAdmin InsertAdmin = new QAdmin("InsertAdmin");

        LocalDate today = LocalDate.now();

        Expression<String> proState = Expressions.cases()
                .when(provision.proExitState.eq(1)).then("3")
                .when(provision.proStartDate.gt(today)).then("0")
                .when(provision.proStartDate.loe(today).and(provision.proExpDate.goe(today))).then("1")
                .otherwise("2");

        JPQLQuery<Long> downloadHistoryCountSubQuery = JPAExpressions
                .select(provisionDownloadHistory.count())
                .from(provisionDownloadHistory)
                .where(provisionDownloadHistory.proCode.eq(provision.proCode));

        JPQLQuery<ProvisionListDto> query = from(provision)
                .where(provision.cpCode.eq(provisionSearchDto.getCpCode())).orderBy(provision.proId.desc())
                .where(provision.insert_date.goe(provisionSearchDto.getStimeStart()).and(provision.insert_date.loe(provisionSearchDto.getStimeEnd())))
                .innerJoin(admin).on(admin.knEmail.eq(provision.insert_email))
                .innerJoin(InsertAdmin).on(InsertAdmin.adminId.eq(admin.adminId))

                .leftJoin(provisionRoster).on(provisionRoster.proCode.eq(provision.proCode).and(provisionRoster.adminId.eq(provisionSearchDto.getAdminId())))

//                .innerJoin(provisionRoster).on(provisionRoster.proCode.eq(provision.proCode))
//                .innerJoin(provisionRoster).on(provisionRoster.proCode.eq(provision.proCode).and(provisionRoster.adminId.eq(provisionSearchDto.getAdminId())))
                .innerJoin(provisionRosterCnt).on(provisionRosterCnt.proCode.eq(provision.proCode)).groupBy(provisionRosterCnt.proCode)
                .select(Projections.constructor(ProvisionListDto.class,
//                        provision.proId,
                        provision.proCode,
                        proState,
                        admin.knName,
                        provision.insert_date,
                        provision.proStartDate,
                        provision.proExpDate,
                        provision.proDownloadYn,
                        provisionRosterCnt.count(),
                        downloadHistoryCountSubQuery,
                        new CaseBuilder()
                                .when(provisionRoster.isNotNull().and(admin.adminId.eq(provisionSearchDto.getAdminId()))).then("3")
                                .when(provisionRoster.isNotNull()).then("1")
                                .otherwise("2"), // 다운로드 가능 "1", 제공종료 가능 "2", 다운로드 또는 제공종료 가능 "3"
                        new CaseBuilder()
                                .when(InsertAdmin.adminId.eq(provisionSearchDto.getAdminId())).then("1")
                                .otherwise("2") // 자신이 제공한건이면 "1", 받은건이면 "2"로 반환
                ));

        if(!provisionSearchDto.getSearchText().equals("")) {
            query.where(admin.knName.like("%"+ provisionSearchDto.getSearchText() +"%"));
        }

        if(!provisionSearchDto.getFilterState().equals("")) {
            BooleanBuilder statePredicate = new BooleanBuilder();

            if(provisionSearchDto.getFilterState().equals("0")) {
                statePredicate.and(provision.proStartDate.gt(today));
            } else if(provisionSearchDto.getFilterState().equals("1")) {
                statePredicate.and(provision.proStartDate.loe(today)).and(provision.proExpDate.goe(today));
            } else if(provisionSearchDto.getFilterState().equals("2")) {
                statePredicate.and(provision.proExpDate.lt(today));
            } else {
                statePredicate.and(provision.proExitState.eq(1));
            }

            query.where(statePredicate);
        }

        if(!provisionSearchDto.getFilterOfferType().equals("")) {
            if(provisionSearchDto.getFilterOfferType().equals("1")) {
                query.where(InsertAdmin.adminId.ne(provisionSearchDto.getAdminId()));
            } else {
                query.where(InsertAdmin.adminId.eq(provisionSearchDto.getAdminId()));
            }
        }

        final List<ProvisionListDto>  provisionListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(provisionListDtos, pageable, query.fetchCount());
    }

    public Long findByProvisionIndexTodayCount(String cpCode, Integer type, LocalDate now) {

        QProvision provision = QProvision.provision;

        JPQLQuery<Long> query = from(provision)
                .where(provision.proProvide.eq(type).and(provision.cpCode.eq(cpCode)))
                .where(provision.insert_date.year().eq(now.getYear())
                        .and(provision.insert_date.month().eq(now.getMonthValue())
                                .and(provision.insert_date.dayOfMonth().eq(now.getDayOfMonth()))))
                .select(Projections.constructor(Long.class,
                        provision.count()
                ));

        return query.fetchOne();
    }

    // 현재 나의 제공 카운팅가져오기
    public Long findByProvisionIndexOfferCountType1(String cpCode, Integer type, long adminId, String dateType, LocalDate now, LocalDate filterDate) {

        QProvision provision = QProvision.provision;
        QProvisionRoster provisionRoster = QProvisionRoster.provisionRoster;

        JPQLQuery<Long> query = from(provision)
                .where(provision.proProvide.eq(type).and(provision.cpCode.eq(cpCode)))
                .innerJoin(provisionRoster).on(provisionRoster.proCode.eq(provision.proCode).and(provisionRoster.adminId.eq(adminId)))
                .select(Projections.constructor(Long.class,
                        provision.count()
                ));

        if(dateType.equals("1")) {
            // 오늘 모두 속하면 조회
            query.where(provision.proStartDate.loe(now).and(provision.proExpDate.goe(filterDate)));
        }else if(dateType.equals("2")) {
            // 이번주 조회
            query.where(provision.proStartDate.loe(now).and(provision.proExpDate.goe(filterDate))); // 날짜 사이값 정의 filterDate < now
        } else {
            // 이번달 조회
            query.where(
                    provision.proStartDate.year().eq(filterDate.getYear()).and(provision.proStartDate.month().eq(filterDate.getMonthValue()))
                            .or(provision.proExpDate.year().eq(filterDate.getYear()).and(provision.proExpDate.month().eq(filterDate.getMonthValue())))
                            .or(provision.proStartDate.loe(filterDate).and(provision.proExpDate.goe(filterDate)))
            );
        }

        return query.fetchOne();
    }

    // 현재 회사의 제공 카운팅가져오기
    public Long findByProvisionIndexOfferCountType2(String cpCode, Integer type, String dateType, LocalDate now, LocalDate filterDate) {

        QProvision provision = QProvision.provision;

        JPQLQuery<Long> query = from(provision)
                .where(provision.proProvide.eq(type).and(provision.cpCode.eq(cpCode)))
                .select(Projections.constructor(Long.class,
                        provision.count()
                ));

        if(dateType.equals("1")) {
            // 오늘 모두 속하면 조회
            query.where(provision.proStartDate.loe(now).and(provision.proExpDate.goe(filterDate)));
        }else if(dateType.equals("2")) {
            // 이번주 조회
            query.where(provision.proStartDate.loe(now).and(provision.proExpDate.goe(filterDate))); // 날짜 사이값 정의 filterDate < now
        } else {
            // 이번달 조회
            query.where(
                    provision.proStartDate.year().eq(filterDate.getYear()).and(provision.proStartDate.month().eq(filterDate.getMonthValue()))
                            .or(provision.proExpDate.year().eq(filterDate.getYear()).and(provision.proExpDate.month().eq(filterDate.getMonthValue())))
                            .or(provision.proStartDate.loe(filterDate).and(provision.proExpDate.goe(filterDate)))
            );
        }

        return query.fetchOne();
    }

    public ProvisionDownloadCheckDto findByProvisionDownloadCheck(String cpCode, String proCode, Integer proDownloadYn) {

        QProvision provision = QProvision.provision;

        JPQLQuery<ProvisionDownloadCheckDto> query = from(provision)
                .where(provision.proCode.eq(proCode).and(provision.cpCode.eq(cpCode).and(provision.proDownloadYn.eq(proDownloadYn))))
                .select(Projections.constructor(ProvisionDownloadCheckDto.class,
                        provision.proCode,
                        provision.proProvide,
                        provision.proStartDate,
                        provision.proExpDate,
                        provision.proTargetType,
                        provision.proExitState
                ));

        return query.fetchOne();
    }


}
