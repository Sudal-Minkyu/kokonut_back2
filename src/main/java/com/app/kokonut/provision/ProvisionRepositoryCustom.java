package com.app.kokonut.provision;

import com.app.kokonut.provision.dtos.ProvisionDownloadCheckDto;
import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionPageDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark : Provision Sql 쿼리호출
 */
public interface ProvisionRepositoryCustom {

    Page<ProvisionPageDto> findByProvisionPage(ProvisionSearchDto provisionSearchDto, Pageable pageable);

    List<ProvisionListDto> findByProvisionList(ProvisionSearchDto provisionSearchDto);

    Long findByProvisionIndexTodayCount(String cpCode, Integer type, LocalDate now);

    Long findByProvisionIndexOfferCountType1(String cpCode, Integer type, long adminId, String dateType, LocalDate now, LocalDate filterDate);

    Long findByProvisionIndexOfferCountType2(String cpCode, Integer type, String dateType, LocalDate now, LocalDate filterDate);

    ProvisionDownloadCheckDto findByProvisionDownloadCheck(String cpCode, String proCode, Integer proDownloadYn);

}