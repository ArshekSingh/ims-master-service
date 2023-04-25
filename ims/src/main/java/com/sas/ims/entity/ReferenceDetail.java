package com.sas.ims.entity;

import com.sas.ims.constant.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Audited
@AuditTable(value = Constant.REFERENCE_DETAIL_H)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "REFERENCE_DETAIL")
public class ReferenceDetail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    @EmbeddedId
    private ReferenceDetailPK referenceDetailPK;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "VALUE_1")
    private String value1;

    @Column(name = "VALUE_2")
    private String value2;

    @Column(name = "VALUE_3")
    private String value3;

    @Column(name = "VALUE_4")
    private String value4;
}
