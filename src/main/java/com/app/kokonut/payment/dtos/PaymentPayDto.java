package com.app.kokonut.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-06-20
 * Time :
 * Remark : 결제금액 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPayDto {

    private Integer awsBillingCloudPayAmount; // AWS RDS(클라우드) 결제금액

    private Integer awsBillingS3PayAmount; // AWS S3 결제금액

    private Integer kokonutServicePayAmount; // 코코넛 결제금액(월결제일 경우 '0'원)

}
