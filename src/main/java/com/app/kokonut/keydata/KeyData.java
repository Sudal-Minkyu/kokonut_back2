package com.app.kokonut.keydata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(of = "kdKeyName")
@Data
@NoArgsConstructor
@Table(name="kn_key_data")
public class KeyData {

    @Id
    @Column(name = "kd_key_name")
    private String kdKeyName;

    @Column(name = "kd_key_value")
    private String kdKeyValue;

    @Column(name = "kd_key_group")
    private String kdKeyGroup;

    @Column(name = "kd_key_description")
    private String kdKeyDescription;

}
