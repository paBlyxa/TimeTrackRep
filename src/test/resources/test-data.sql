INSERT INTO Project (name) 
        VALUES ('GHK IGK-6');
INSERT INTO Project (name) 
        VALUES ('GHK ARM RTK');
INSERT INTO Project (name) 
        VALUES ('LAES2 SAU DG');

INSERT INTO Employee (surname, name, mail, post, department, username, password)
       VALUES ('Idolov', 'Michael', 'm.idolov@west-e.ru', 'Leading engineer', 'Scheme', 'm.idolov', '123idolov');
INSERT INTO Employee (surname, name, mail, post, department, username, password)
       VALUES ('Muhutdinov', 'Roman', 'r.muhutdinov@west-e.ru', 'Menedger', 'Menedgers', 'r.muhutdinov', '123muhutdinov');
INSERT INTO Employee (surname, name, mail, post, department, username, password)
       VALUES ('Fakadey', 'Pavel', 'p.fakadey@west-e.ru', 'Leading programmer', 'Programmeers', 'p.fakadey', '123fakadey');
 

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