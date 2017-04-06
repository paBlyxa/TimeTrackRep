package com.we.timetrack.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.we.timetrack.converter.TaskStatusAttributeConverter;

@Entity
@Table(name = "task")
public class Task {

	private int taskId;
	private String name;
	private String comment;
	private TaskStatus status;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq_gen")
	@SequenceGenerator(name = "task_seq_gen", sequenceName = "task_taskid_seq", allocationSize = 1)
	@Column(name = "taskid")
	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
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

}
