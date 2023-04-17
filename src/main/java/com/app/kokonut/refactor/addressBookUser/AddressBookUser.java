package com.app.kokonut.refactor.addressBookUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "abuId")
@Data
@NoArgsConstructor
@Table(name="kn_address_book_user")
public class AddressBookUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "abu_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long abuId;

    /**
     * 주소록 키
     */
    @ApiModelProperty("주소록 키")
    @Column(name = "ADDRESS_BOOK_IDX")
    private Long addressBookIdx;

    /**
     * 아이디
     */
    @Column(name = "abuUserId")
    @ApiModelProperty("아이디")
    private String abuUserId;

    /**
     * 등록자 email
     */
    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    /**
     * 등록 날짜
     */
    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    /**
     * 수정자 이름
     */
    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    /**
     * 수정 날짜
     */
    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
