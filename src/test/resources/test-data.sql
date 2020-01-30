INSERT INTO Project (name) 
        VALUES ('GHK IGK-6');
INSERT INTO Project (name) 
        VALUES ('GHK ARM RTK');
INSERT INTO Project (name) 
        VALUES ('LAES2 SAU DG');

INSERT INTO Employee (surname, name, mail, post, department, username, password)
       VALUES ('Sidorov', 'Michael', 'm.sidorov@mail.com', 'Leading engineer', 'Scheme', 'm.sidorov', '123sidorov');
INSERT INTO Employee (surname, name, mail, post, department, username, password)
       VALUES ('Petrov', 'Roman', 'r.petrov@mail.ru', 'Manager', 'Managers', 'r.petrov', '123petrov');
INSERT INTO Employee (surname, name, mail, post, department, username, password)
       VALUES ('Sobolev', 'Pavel', 'p.sobolev@mail.ru', 'Leading programmer', 'Programmers', 'p.sobolev', '123sobolev');
 

INSERT INTO Task (name)
	VALUES ('Make scheme');
INSERT INTO Task (name)
	VALUES ('Make document');
INSERT INTO Task (name)
	VALUES ('PO');

INSERT INTO Timesheet(projectId, taskId, employeeId, dateTask, countTime)
       VALUES (1, 1, 1, '2016-01-12', 8);

INSERT INTO Timesheet(projectId, taskId, employeeId, dateTask, countTime)
       VALUES (2, 3, 3, '2016-01-12', 8);

INSERT INTO Timesheet(projectId, taskId, employeeId, dateTask, countTime)
       VALUES (3, 2, 2, '2016-01-12', 8);