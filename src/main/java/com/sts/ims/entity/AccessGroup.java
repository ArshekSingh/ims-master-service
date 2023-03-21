package com.sts.ims.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "access_groups")
public class AccessGroup extends Auditable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "access_group_id")
	private Long id;

	@Column(name = "access_group_no")
	private String accessGroupNo;

	
	@Column(name = "name",unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "active")
	private Integer active;

	@Column(name = "editable")
	private Integer editable;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "access_group_role", joinColumns = @JoinColumn(name = "access_group_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

}
