package com.app.kokonut.faq.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqDetailDto {

    private Long faqId;
    private Long adminId;

    private String faqQuestion;
    private String faqAnswer;

    private Integer faqType;

    private LocalDateTime faqRegistStartDate;

    private Integer faqViewCount;

    private String insert_email;
    private LocalDateTime insert_date;

    private Long modify_id;
    private String modify_email;
    private LocalDateTime modify_date;


}
