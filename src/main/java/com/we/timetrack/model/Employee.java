/**
 * 
 */
package com.we.timetrack.model;


import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
/**
 * @author fakadey
 *
 */
//@Entity
//@Table(name = "employee")
public class Employee implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6300796441923012473L;
	
	private UUID employeeId;	
	@NotNull(message = "������� ������ ���� ������")
	@Size(max = 64, message = "����� ������� �� ����� 64 �������")
	private String surname;
	@NotNull(message = "��� ������ ���� ������")
	@Size(max = 64, message = "����� ����� �� ����� 32 �������")	
	private String name;
	//@NotNull(message = "Email ������ ���� �����")
	//@Size(max = 64, message = "����� email-� �� ����� 64 �������")
	private String mail;
	private Integer chief;
	//@NotNull(message = "��������� ������ ���� ������")
	//@Size(max = 64, message = "����� ��������� �� ����� 64 �������")
	private String post;
	//@NotNull(message = "����� ������ ���� �����")
	//@Size(max = 64, message = "����� ������ �� ����� 255 ��������")
	private String department;
	@NotNull(message = "��� ������������ ������ ���� ������")
	@Size(max = 64, message = "����� ����� ������������ �� ����� 64 �������")
	private String username;
	@NotNull(message = "������ ������ ���� �����")
	@Size(max = 64, message = "����� ������ �� ����� 64 �������")
	private String password;
	@Transient
	private Collection<? extends GrantedAuthority> authorities;
	@Transient
	private List<Employee> directReports;
	
	@Id
	@Column(name = "employeeid")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq_gen")
//	@SequenceGenerator(name = "employee_seq_gen", sequenceName = "employee_employeeid_seq", allocationSize = 1)
	public UUID getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(UUID employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Integer getChief() {
		return chief;
	}
	public void setChief(Integer chief) {
		this.chief = chief;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/*
	 * Compare two Employee objects by fields
	 * without field employeeId
	 */
	public boolean isEqual(Employee employee){
		return (surname !=  null) && surname.equals(employee.getSurname()) &&
				(name != null) && name.equals(employee.getName()) &&
				(mail != null) && mail.equals(employee.getMail()) &&
				(chief != null) && chief.equals(employee.getChief()) &&
				(post != null) && post.equals(employee.getPost()) &&
				(department != null) && department.equals(employee.getDepartment()) &&
				(username != null) && username.equals(employee.getUsername());
				
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	
	public List<Employee> getDirectReports() {
		return directReports;
	}
	
	public void setDirectReports(List<Employee> directReports) {
		this.directReports = directReports;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	
}
