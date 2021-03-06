/**
 * 
 */
package com.we.timetrack.model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
	@ManyToOne(cascade= {CascadeType.ALL})
	@JoinColumn(name="chief")
	private Employee manager;
	@OneToMany(mappedBy="manager")
	private Set<Employee> subordinates = new HashSet<Employee>();
	//@NotNull(message = "Должность должна быть задана")
	//@Size(max = 64, message = "Должность не более 64 символа")
	private String post;
	//@NotNull(message = "Отдел должен быть задан�")
	//@Size(max = 64, message = "Отдел не более 255 символов")
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="departmentid")
	private Department department;
	@NotNull(message = "Имя пользователя должно быть задано")
	@Size(max = 64, message = "Имя пользователя не более 64 символа")
	private String username;
/*	@NotNull(message = "Пароль должен быть задан")
	@Size(max = 64, message = "Пароль не более 64 символа")*/
	@Transient
	private String password;
	@ManyToMany
	@JoinTable(
			name = "employeesroles",
			joinColumns = @JoinColumn(
					name = "employeeid", referencedColumnName = "employeeid"),
			inverseJoinColumns = @JoinColumn(
					name = "roleid", referencedColumnName = "roleid"))
	private Collection<Role> roles = new ArrayList<>();
	@ManyToMany
	@JoinTable(
			name = "employeesproperty",
			joinColumns = @JoinColumn(
					name = "employeeid", referencedColumnName = "employeeid"),
			inverseJoinColumns = @JoinColumn(
					name = "propertyid", referencedColumnName = "propertyid"))
	private Collection<EmployeeProperty> properties;

	private Boolean active;
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
		if (surname != null) {
			this.surname = surname;
		} else {
			this.surname = "";
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (name != null) {
			this.name = name;
		} else {
			this.name = "";
		}
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Employee getManager() {
		return manager;
	}
	public void setManager(Employee manager) {
		this.manager = manager;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
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
	public String getShortName() {
		String[] str;
		if (name != null && !name.isEmpty() && name.contains(" ")){
			str = name.split(" ");
			return (surname != null ? surname : "NoName") + " " + str[0].charAt(0) + ". " + (str.length > 1 ? str[1].charAt(0) : "") + ".";
		}
		return (surname != null ? surname : "NoName");
		
	}
	
	/**
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
				(manager != null) && manager.equals(employee.getManager()) &&
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
	
	public Collection<Role> getRoles(){
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			for (Privilege privilege : role.getPrivileges()) {
				authorities.add(new SimpleGrantedAuthority(privilege.getName()));
			}
		}
		return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return active;
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

	public Collection<EmployeeProperty> getProperties(){
		return properties;
	}
	public void setProperties(Collection<EmployeeProperty> properties) {
		this.properties = properties;
	}
	public Set<Employee> getSubordinates() {
		return subordinates;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
}
