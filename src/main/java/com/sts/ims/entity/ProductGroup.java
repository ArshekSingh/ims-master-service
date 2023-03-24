package com.sts.ims.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="product_group")
public class ProductGroup extends BaseEntity {

	@Id
	@Column(name = "PRODUCT_GROUP_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long productGroupId;

	@Column(name = "ORG_ID")
	private Long orgId;

	@Column(name = "PARENT_GROUP_ID")
	private Long parentGroupId;

	@Column(name = "GROUP_CODE")
	private String groupCode;

	@Column(name = "GROUP_NAME")
	private String groupName;
	
	@Column(name="GROUP_TYPE")
	private String groupType;

	@Column(name="ACTIVE")
	private String active;

}
