package com.app.kokonut.history.extra.encrypcounthistory;

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
 * Remark : EncrypCountHistory Table Entity
 */
@Entity
@EqualsAndHashCode(of = "echId")
@Data
@NoArgsConstructor
@Table(name="kn_encryp_count_history")
public class EncrypCountHistory {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ech_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long echId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "ech_count")
    @ApiModelProperty("암호화 실행 횟수 카운트")
    private Integer echCount;

    @ApiModelProperty("암호화 실행 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
