package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@LastModifiedBy
	@Column(name = "UPDATED_BY")
	protected String updatedBy;
	@CreatedBy
	@Column(name = "INSERTED_BY")
	private String insertedBy;
	@CreatedDate
	@Column(name = "INSERTED_ON")
	private LocalDateTime insertedOn;
	@LastModifiedDate
	@Column(name = "UPDATED_ON")
	private LocalDateTime updatedOn;
}