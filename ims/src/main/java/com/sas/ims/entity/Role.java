package com.sas.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "role_master")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Long id;

	@Column(name = "role_name", unique = true, nullable = false)
	private String roleName;

	@Column(name = "role_group", nullable = false)
	private String roleGroup;
}