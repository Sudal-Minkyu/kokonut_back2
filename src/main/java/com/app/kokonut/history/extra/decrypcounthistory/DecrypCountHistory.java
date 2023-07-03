package com.app.kokonut.history.extra.decrypcounthistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-30
 * Time :
 * Remark : DecrypCountHistory Table Entity
 */
@Entity
@EqualsAndHashCode(of = "dchId")
@Data
@NoArgsConstructor
@Table(name="kn_decryp_count_history")
public class DecrypCountHistory {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "dch_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dchId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "dch_count")
    @ApiModelProperty("복호화 실행 횟수 카운트")
    private Integer dchCount;

    @ApiModelProperty("복호화 실행 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
