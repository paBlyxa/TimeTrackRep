package com.we.timetrack.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.we.timetrack.converter.ProjectStatusAttributeConverter;

import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "project")
public class Project {

	private int projectId;
	private String name;
	private String comment;
	private ProjectStatus status;
	private String contract;
	private Set<UUID> projectLeaders = new HashSet<>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq_gen")
	@SequenceGenerator(name = "project_seq_gen", sequenceName = "project_projectid_seq", allocationSize = 1)
	@Column(name = "projectid")
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
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

	@Convert(converter = ProjectStatusAttributeConverter.class)
	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	// @OneToMany(cascade = CascadeType.ALL)
	// @JoinTable(name = "projectleader", joinColumns = { @JoinColumn(name =
	// "projectid") },
	// inverseJoinColumns = { @JoinColumn(name = "employeeid")})
	@ElementCollection
	@CollectionTable(name = "projectleader", joinColumns = @JoinColumn(name = "projectid"))
	@Column(name = "employeeid")
	public Set<UUID> getProjectLeaders() {
		return projectLeaders;
	}

	public void setProjectLeaders(Set<UUID> projectLeaders) {
		this.projectLeaders = projectLeaders;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj == null){
			return false;
		}
		if (obj instanceof Project){
			Project pr = (Project) obj;
			if (this.projectId == pr.projectId){
				return true;
			}
			if ((this.name == pr.name)
				&& (this.comment == pr.comment) 
				&& (this.status == pr.status)
				&& (this.contract == pr.contract)){
				return true;
			}
		}
		return false;
	}

}
