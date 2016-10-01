drop table if exists employee;
drop table if exists project;
drop table if exists task;
drop table if exists timesheet;


create table employee (
	employeeId		identity			,
	surname			VARCHAR(64)	NOT NULL,
	name			VARCHAR(32)	NOT NULL,
	mail			VARCHAR(64)		,
	chief			integer			,
	post			VARCHAR(64)	NOT NULL,
	department		VARCHAR(255)	NOT NULL,
	username		VARCHAR(64)	NOT NULL,
	password		VARCHAR(32)	NOT NULL,
);


create table project (
	projectId		identity primary key,
	name			VARCHAR(255)	NOT NULL,
	comment			VARCHAR(255)	
);

CREATE TABLE Task
(
	taskId			identity primary key			,
	name			VARCHAR(255)	NOT NULL,
	comment			VARCHAR(255)
);

CREATE TABLE Timesheet
(
	id			identity primary key		,
	projectId		integer		NOT NULL,
	taskId			integer		NOT NULL,
	employeeId		integer		NOT NULL,
	dateTask		date		NOT NULL,
	countTime		numeric(7,2)	NOT NULL,
	comment			VARCHAR(255)
);