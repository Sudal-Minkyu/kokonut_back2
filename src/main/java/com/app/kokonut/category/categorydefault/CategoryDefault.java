package com.app.kokonut.category.categorydefault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="cdId")
@Data
@NoArgsConstructor
@Table(name="kn_category_default")
public class CategoryDefault implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty("주키")
    @Column(name = "cd_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdId;

    @ApiModelProperty("카테고리명")
    @Column(name = "cd_name")
    private String cdName;

    @ApiModelProperty("추가한자의 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("추가된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
