package com.app.kokonut.commonfield;

import com.app.kokonut.commonfield.dtos.CommonFieldDto;

import java.util.List;

public interface CommonFieldRepositoryCustom {

    List<CommonFieldDto> selectCommonUserTable();

}
