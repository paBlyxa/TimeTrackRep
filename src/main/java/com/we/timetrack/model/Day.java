package com.we.timetrack.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


import com.we.timetrack.converter.DayStatusAttributeConverter;
import com.we.timetrack.converter.LocalDateAttributeConverter;

/**
 * @author fakadey
 *
 */
@Entity
@Table(name = "workcalendar")
public class Day {

	private int id;
	private LocalDate dateDay;
	private DayStatus status;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workcalendar_seq_gen")
	@SequenceGenerator(name = "workcalendar_seq_gen", sequenceName = "workcalendar_id_seq", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "dateday")
	@Convert(converter = LocalDateAttributeConverter.class)
	public LocalDate getDateDay() {
		return dateDay;
	}

	public void setDateDay(LocalDate dateDay) {
		this.dateDay = dateDay;
	}

	@NotNull
	@Column(name = "status")
	@Convert(converter = DayStatusAttributeConverter.class)
	public DayStatus getStatus() {
		return status;
	}

	public void setStatus(DayStatus status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Day){
			return dateDay.equals(((Day)obj).getDateDay());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Id: " + getId() + " Date: " + getDateDay() + " Status: " + getStatus();
	}

}
