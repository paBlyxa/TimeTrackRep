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
@Table(name = "employeeproperty")
public class EmployeeProperty {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeproperty_seq_gen")
	@SequenceGenerator(name = "employeeproperty_seq_gen", sequenceName = "employeeproperty_propertyid_seq", allocationSize = 1)
	@Column(name = "propertyid")
	private int propertyId;
	private String name;
	private String value;

	@ManyToMany(mappedBy = "properties")
	private Collection<Employee> employees;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EmployeeProperty) {
			EmployeeProperty ep = (EmployeeProperty)obj;
			return this.propertyId == ep.propertyId;
		}
		return false;
	}
}

