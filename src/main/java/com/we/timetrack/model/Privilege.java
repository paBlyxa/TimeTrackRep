package com.we.timetrack.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "privilege")
public class Privilege {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_seq_gen")
	@SequenceGenerator(name = "privilege_seq_gen", sequenceName = "privilege_privilegeid_seq", allocationSize = 1)
	@Column(name = "privilegeid")
	private int privilegeId;
	
	private String name;
	
	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && (obj instanceof Privilege)) {
			Privilege privilege = (Privilege) obj;
			return (privilegeId == privilege.privilegeId) && name.equals(privilege.name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (name + privilegeId).hashCode();
	}
}
