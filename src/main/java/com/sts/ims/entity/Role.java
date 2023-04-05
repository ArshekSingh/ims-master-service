package com.sts.ims.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Long id;

	@Column(name = "role", unique = true, nullable = false)
	private String role;

	@Column(name = "role_group", nullable = false)
	private String roleGroup;
}