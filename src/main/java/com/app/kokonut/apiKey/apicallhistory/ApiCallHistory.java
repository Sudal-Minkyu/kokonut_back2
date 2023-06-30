package com.app.kokonut.apiKey.apicallhistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-29
 * Time :
 * Remark : kn_api_call_history Table Entity
 */
@Entity
@EqualsAndHashCode(of = "akhId")
@Data
@NoArgsConstructor
@Table(name="kn_api_call_history")
public class ApiCallHistory {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "akh_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long akhId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cp_code;

    @ApiModelProperty("호출 URL")
    @Column(name = "akh_url")
    private String akhUrl;

    @ApiModelProperty("호출 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
