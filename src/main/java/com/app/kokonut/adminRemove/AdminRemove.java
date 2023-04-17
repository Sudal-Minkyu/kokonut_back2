package com.app.kokonut.adminRemove;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "adminId")
@Data
@NoArgsConstructor
@Table(name="kn_admin_remove")
public class AdminRemove implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ADMIN IDX
     */
    @Id
    @ApiModelProperty("ADMIN IDX")
    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    /**
     * 이메일
     */
    @Column(name = "ar_email")
    @ApiModelProperty("이메일")
    private String arEmail;

    /**
     * 탈퇴 사유(1:계정변경, 2:서비스 이용 불만, 3:사용하지 않음, 4:기타)
     */
    @Column(name = "ar_reason")
    @ApiModelProperty("탈퇴 사유(1:계정변경, 2:서비스 이용 불만, 3:사용하지 않음, 4:기타)")
    private Integer arReason;

    /**
     * 탈퇴 사유
     */
    @ApiModelProperty("탈퇴 사유")
    @Column(name = "ar_reason_detail")
    private String ar_reason_detail;

    /**
     * 탈퇴일시
     */
    @ApiModelProperty("탈퇴일시")
    @Column(name = "ar_withdrawal_date", nullable = false)
    private Date arWithdrawalDate;

}
