package com.app.kokonut.index.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallHistoryCountDto {

    private BigInteger count;

    public Integer getCount() {
        return count.intValue();
    }

}
