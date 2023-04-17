package com.app.kokonutapi.personalInfoProvision.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2022-11-01
 * Time :
 * Remark : PersonalInfoProvision 리스트 조회하는 MapperDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoProvisionMapperDto {

    @NotNull(message = "기업 ID가 존재하지 않습니다.")
    private Long companyId;

    private Integer reason;

    private Long adminId;

    private Integer recipientType;
    private String recipientEmail;

    private String agreeYn;
    private Integer agreeType;

    private Date stimeStart;
    private Date stimeEnd;

    private String searchText;
    private String searchType;

    @NotNull(message = "state 값은 필수입니다.")
    private Integer state;

//    private Integer offset;
//    private Integer limit;

    public Integer getReason() {
        return Objects.requireNonNullElse(reason, 0);
    }

//    public Long getadminId() {
//        return Objects.requireNonNullElse(adminId, 0L);
//    }

    public Integer getRecipientType() {
        return Objects.requireNonNullElse(recipientType, 0);
    }

    public String getRecipientEmail() {
        return Objects.requireNonNullElse(recipientEmail, "");
    }

    public String getAgreeYn() {
        return Objects.requireNonNullElse(agreeYn, "");
    }

    public Integer getAgreeType() {
        return Objects.requireNonNullElse(agreeType, 0);
    }

    public String getSearchText() {
        return Objects.requireNonNullElse(searchText, "");
    }

    public String getSearchType() {
        return Objects.requireNonNullElse(searchType, "");
    }

    public Integer getState() {
        return Objects.requireNonNullElse(state, 0);
    }
}
