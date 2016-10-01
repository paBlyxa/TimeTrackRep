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
	@NotNull(message = "������� ������ ���� ������")
	@Size(max = 64, message = "����� ������� �� ����� 64 �������")
	private String surname;
	@NotNull(message = "��� ������ ���� ������")
	@Size(max = 64, message = "����� ����� �� ����� 32 �������")	
	private String name;
	@NotNull(message = "Email ������ ���� �����")
	@Size(max = 64, message = "����� email-� �� ����� 64 �������")
	private String mail;
	private Integer chief;
	@NotNull(message = "��������� ������ ���� ������")
	@Size(max = 64, message = "����� ��������� �� ����� 64 �������")
	private String post;
	@NotNull(message = "����� ������ ���� �����")
	@Size(max = 64, message = "����� ������ �� ����� 255 ��������")
	private String department;
	@NotNull(message = "��� ������������ ������ ���� ������")
	@Size(max = 64, message = "����� ����� ������������ �� ����� 64 �������")
	private String username;
	@NotNull(message = "������ ������ ���� �����")
	@Size(max = 64, message = "����� ������ �� ����� 32 �������")
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
