package com.we.timetrack.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq_gen")
	@SequenceGenerator(name = "role_seq_gen", sequenceName = "role_roleid_seq", allocationSize = 1)
	@Column(name = "roleid")
	private int roleId;
	
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	private Collection<Employee> employees;
	
	@ManyToMany
	@JoinTable(
			name = "rolesprivileges",
			joinColumns = @JoinColumn(
					name = "roleid", referencedColumnName = "roleid"),
			inverseJoinColumns = @JoinColumn(
					name = "privilegeid", referencedColumnName = "privilegeid"))
	private Collection<Privilege> privileges;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<Privilege> getPrivileges(){
		return privileges;
	}
	public void setPrivileges(Collection<Privilege> privileges) {
		this.privileges = privileges;
	}
}
