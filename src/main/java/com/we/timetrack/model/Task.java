package com.we.timetrack.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.we.timetrack.converter.TaskStatusAttributeConverter;

@Entity
@Table(name = "task")
public class Task {

	private Integer taskId;
	private String name;
	private String comment;
	private TaskStatus status;
	private LocalDateTime createDateTime;
	private LocalDateTime updateDateTime;
	private Set<Project> projects = new HashSet<>(0);
	private Set<Department> departments = new HashSet<>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq_gen")
	@SequenceGenerator(name = "task_seq_gen", sequenceName = "task_taskid_seq", allocationSize = 1)
	@Column(name = "taskid")
	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Convert(converter = TaskStatusAttributeConverter.class)
	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	@ManyToMany(mappedBy = "tasks")
	@JsonIgnore
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(taskId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Task))
			return false;
		Task other = (Task) obj;
		if (taskId == other.taskId)
			return true;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (projects == null) {
			if (other.projects != null)
				return false;
		} else if (!projects.equals(other.projects))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	@ManyToMany
	@JoinTable(
			name = "departmentstasks",
			joinColumns = @JoinColumn(
					name = "taskid", referencedColumnName = "taskid"),
			inverseJoinColumns = @JoinColumn(
					name = "departmentid", referencedColumnName = "departmentid"))
	@JsonIgnore
	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	@CreationTimestamp
	@Column(name = "creationtimestamp")
	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}
	@UpdateTimestamp
	@Column(name = "updatetimestamp")
	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}
	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	
}
