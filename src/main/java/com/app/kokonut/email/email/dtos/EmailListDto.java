package com.app.kokonut.email.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailListDto {

    // email 테이블
    private Long emId;
    private Long egId;
    private String emTitle;
    private String emContents;
    private LocalDateTime insert_email;

}
