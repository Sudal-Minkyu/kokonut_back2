package com.app.kokonut.apiKey.dtos;

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
