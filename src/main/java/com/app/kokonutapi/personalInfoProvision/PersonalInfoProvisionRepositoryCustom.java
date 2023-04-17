package com.app.kokonutapi.personalInfoProvision;

import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionListDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionMapperDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionNumberDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2022-11-01
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PersonalInfoProvision Sql 쿼리호출
 */
@Repository
public interface PersonalInfoProvisionRepositoryCustom {

    PersonalInfoProvisionNumberDto findByPersonalInfoProvisionNumber(String prefix); // selectProvisionLatestNumber -> 변경후

    PersonalInfoProvisionDto findByNumberProvision(String number); // selectProvision -> 변경후

    List<PersonalInfoProvisionListDto> findByProvisionList(PersonalInfoProvisionMapperDto personalInfoProvisionMapperDto); // selectProvisionList -> 변경후


}