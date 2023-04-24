package com.app.kokonut.category.categoryitem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="cddId")
@Data
@NoArgsConstructor
@Table(name="kn_category_default_item")
public class CategoryItem {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "cdd_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cddId;

    @ApiModelProperty("카테고리명의 ID")
    @Column(name = "cd_id", nullable = false)
    private Long cdId;

    @ApiModelProperty("'항목명'")
    @Column(name = "cdd_name")
    private String cddName;

    @ApiModelProperty("암호화여부 -> 0: 비암호화, 1 : 암호화")
    @Column(name = "cdd_security")
    private Integer cddSecurity;

    @ApiModelProperty("뒤에 표시될 이름")
    @Column(name = "cdd_sub_name")
    private String cddSubName;

    @ApiModelProperty("뒤에 표시될 이름의 색상 클래스명")
    @Column(name = "cdd_class_name")
    private String cddClassName;

    @ApiModelProperty("추가한자의 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("추가된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
