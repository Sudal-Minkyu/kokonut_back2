package com.app.kokonut.history.extra.errorhistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-06-30
 * Time :
 * Remark : Kokonut 에러모음 테이블
 */
@Entity
@Data
@EqualsAndHashCode(of = "etId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="kn_er_table")
public class ErrorHistory {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "et_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long etId;

    @ApiModelProperty("에러제목")
    @Column(name="et_title")
    private String etTitle;

    @ApiModelProperty("에러내용")
    @Column(length = 100000, name="et_msg")
    private String etMsg;

    @ApiModelProperty("회사코드")
    @Column(name="insert_email")
    private String insert_email;

    @ApiModelProperty("실행날짜")
    @Column(name="insert_date")
    private LocalDateTime insert_date;

}
