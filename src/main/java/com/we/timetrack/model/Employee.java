/**
 * 
 */
package com.we.timetrack.model;


import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
/**
 * @author fakadey
 *
 */
@Entity
@Table(name = "employee")
public class Employee implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6300796441923012473L;
	
	@Id
	@Column(name = "employeeid")
	//@GenericGenerator(name = "uuid-gen", strategy = "uuid2")
	//@GeneratedValue(generator = "uuid-gen")
	@Type(type="pg-uuid")
	private UUID employeeId;	
	@NotNull(message = "Фамилия должна быть задана")
	@Size(max = 64, message = "Фамилия не более 64 символа")
	private String surname;
	@NotNull(message = "Имя должно быть задано")
	@Size(max = 64, message = "Имя не более 32 символа")	
	private String name;
	//@NotNull(message = "Адрес почты должен быть задан")
	//@Size(max = 64, message = "Адрес почты не более 64 символа")
	private String mail;
	@Type(type="pg-uuid")
	private UUID chief;
	//@NotNull(message = "Должность должна быть задана")
	//@Size(max = 64, message = "Должность не более 64 символа")
	private String post;
	//@NotNull(message = "Отдел должен быть задан�")
	//@Size(max = 64, message = "Отдел не более 255 символов")
	private String department;
	@NotNull(message = "Имя пользователя должно быть задано")
	@Size(max = 64, message = "Имя пользователя не более 64 символа")
	private String username;
	@NotNull(message = "Пароль должен быть задан")
	@Size(max = 64, message = "Пароль не более 64 символа")
	private String password;
	@Transient
	private Collection<? extends GrantedAuthority> authorities;
	@Transient
	private List<Employee> directReports;
	

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
	public UUID getChief() {
		return chief;
	}
	public void setChief(UUID chief) {
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
	public String getShortName() {
		String[] str = {"", ""};
		if (name != null){
			str = name.split(" ");
		}
		return (surname != null ? surname : "NoName") + " " + str[0].charAt(0) + ". " + (str.length > 1 ? str[1].charAt(0) : "") + ".";
	}
	
	/*
	 * Compare two Employee objects by fields
	 * without field employeeId
	 */
	@Override
	public boolean equals(Object object){
		if ((object == null) || !(object instanceof Employee)){
			return false;
		}
		Employee employee = (Employee) object;
		if ((employeeId != null) && employeeId.equals(employee.getEmployeeId())){
			return true;
		}
		return (surname !=  null) && surname.equals(employee.getSurname()) &&
				(name != null) && name.equals(employee.getName()) &&
				(mail != null) && mail.equals(employee.getMail()) &&
				(chief != null) && chief.equals(employee.getChief()) &&
				(post != null) && post.equals(employee.getPost()) &&
				(department != null) && department.equals(employee.getDepartment()) &&
				(username != null) && username.equals(employee.getUsername());
	}
	
	/**
	 * Returns a hash code for this Employee
	 * (hash code for emloyeeId).
	 */
	@Override
	public int hashCode(){
		return employeeId.hashCode();
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
