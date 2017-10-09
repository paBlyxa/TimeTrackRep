package com.we.timetrack.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.we.timetrack.converter.LocalDateAttributeConverter;
import com.we.timetrack.util.LocalDateUtil.LocalDateSerializer;
import com.we.timetrack.util.LocalDateUtil.LocalDateDeserializer;

@Entity
@Table(name = "vacation")
public class Vacation {

	private Integer id;
	private UUID employeeId;
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate startDate;
	private int duration;
	private LocalDate changeDate;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacation_seq_gen")
	@SequenceGenerator(name = "vacation_seq_gen", sequenceName = "vacation_id_seq", allocationSize = 1)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "employeeid")
	public UUID getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(UUID employeeId) {
		this.employeeId = employeeId;
	}

	@Column(name = "startdate")
	@DateTimeFormat(pattern = "dd.MM.YYYY")
	@Convert(converter = LocalDateAttributeConverter.class)
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Column(name = "changedate")
	@DateTimeFormat(pattern = "dd.MM.YYYY")
	@Convert(converter = LocalDateAttributeConverter.class)
	public LocalDate getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(LocalDate changeDate) {
		this.changeDate = changeDate;
	}

	@Override
	public String toString() {
		return "Отпуск: id=" + id + ", employeeId=" + employeeId + ", startDate=" + startDate + ", duration=" + duration
				+ ", changeDate=" + changeDate;
	}
}
