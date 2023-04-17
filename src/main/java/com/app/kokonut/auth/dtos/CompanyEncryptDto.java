package com.app.kokonut.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-06
 * Time :
 * Remark : companyId를 통해 DataKey를 가져오는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEncryptDto {

    private String cpCode;

    private String dataKey;

}
