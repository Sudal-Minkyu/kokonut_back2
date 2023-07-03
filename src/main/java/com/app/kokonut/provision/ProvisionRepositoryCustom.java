package com.app.kokonut.provision;

import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark : Provision Sql 쿼리호출
 */
public interface ProvisionRepositoryCustom {

    Page<ProvisionListDto> findByProvisionList(ProvisionSearchDto provisionSearchDto, Pageable pageable);

    Long findByProvisionIndexTodayCount(String cpCode, Integer type, LocalDate now);

    Long findByProvisionIndexOfferCount(String cpCode, Integer type, String dateType, LocalDate now, LocalDate filterDate);

}