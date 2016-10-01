package com.we.timetrack.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "timesheet")
public class Timesheet {

	private int id;
	private Project project;
	private Task task;
	private Employee employee;
	private Date dateTask;
	private float countTime;
	private String comment;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_seq_gen")
	@SequenceGenerator(name = "timesheet_seq_gen", sequenceName = "timesheet_id_seq", allocationSize = 1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectid", referencedColumnName="projectid")
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "taskid", referencedColumnName="taskid")
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employeeid")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	@Column(name = "datetask")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getDateTask() {
		return dateTask;
	}
	public void setDateTask(Date dateTask) {
		this.dateTask = dateTask;
	}
	@Column(name = "counttime")
	public float getCountTime() {
		return countTime;
	}
	public void setCountTime(float countTime) {
		this.countTime = countTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
