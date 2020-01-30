package com.we.timetrack.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author fakadey
 *
 */
@Entity
@Table(name = "department")
public class Department implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6705119888571253066L;
	
	private Integer departmentId;
	private String name;
	private Collection<Employee> employees;
	private Set<Task> tasks = new HashSet<>(0);
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq_gen")
	@SequenceGenerator(name = "department_seq_gen", sequenceName = "department_departmentid_seq", allocationSize = 1)
	@Column(name = "departmentid")
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + departmentId;
		result = prime * result + ((name == null) ? 0 : name.toLowerCase().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Department))
			return false;
		Department other = (Department) obj;
		if ((departmentId != null) && (other.departmentId != null)) {
			if (departmentId != other.departmentId)
				return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equalsIgnoreCase(other.name))
			return false;
		return true;
	}
	
	@OneToMany(mappedBy="department", fetch=FetchType.LAZY)
	@Transient
	public Collection<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(Collection<Employee> employees) {
		this.employees = employees;
	}

	@ManyToMany(mappedBy = "departments")
	@JsonIgnore
	@Transient
	public Set<Task> getTasks() {
		return tasks;
	}
	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	@Override
	public String toString() {
		return "Department [name=" + name + "]";
	}
}
