/**
 * 
 */
package com.we.timetrack.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 * @author fakadey
 *
 */
@Entity
@Table(name = "employee")
public class Employee{

	private int employeeId;	
	@NotNull(message = "Фамилия должна быть задана")
	@Size(max = 64, message = "Длина фамилии не более 64 символа")
	private String surname;
	@NotNull(message = "Имя должно быть задана")
	@Size(max = 64, message = "Длина имени не более 32 символа")	
	private String name;
	@NotNull(message = "Email должен быть задан")
	@Size(max = 64, message = "Длина email-а не более 64 символа")
	private String mail;
	private Integer chief;
	@NotNull(message = "Должность должна быть задана")
	@Size(max = 64, message = "Длина должности не более 64 символа")
	private String post;
	@NotNull(message = "Отдел должен быть задан")
	@Size(max = 64, message = "Длина отдела не более 255 символов")
	private String department;
	@NotNull(message = "Имя пользователя должно быть задано")
	@Size(max = 64, message = "Длина имени пользователя не более 64 символа")
	private String username;
	@NotNull(message = "Пароль должен быть задан")
	@Size(max = 64, message = "Длина пароля не более 32 символа")
	private String password;
	
	@Id
	@Column(name = "employeeid")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq_gen")
	@SequenceGenerator(name = "employee_seq_gen", sequenceName = "employee_employeeid_seq", allocationSize = 1)
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
