package com.app.kokonut.company.company.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyValidStartDto {

    private LocalDate cpValidStart; // 자동결제 부과 시작(빌링테이블 주키의 자동결제 부과 시작일 필드와 같아야함)

}
