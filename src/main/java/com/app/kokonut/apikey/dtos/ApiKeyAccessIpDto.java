package com.app.kokonut.apikey.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyAccessIpDto {

    private String accessIp;

    private String memo;

}
