package com.we.timetrack.test;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.Transaction;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.Timesheet;

public class HibernateTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args){
		
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
				.configure("hibernate.cfg.xml")
				.build();
		
		Metadata metadata = new MetadataSources(standardRegistry)
				.addAnnotatedClass(Employee.class)
				.addAnnotatedClass(Project.class)
				.addAnnotatedClass(Task.class)
				.addAnnotatedClass(Timesheet.class)
				.getMetadataBuilder()
				//.applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
				.build();
		
		SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
				.build();
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		Employee employee;
		
		//Запрос одной записи
		employee = session.get(Employee.class, 1);
		System.out.println("Сотрудник: " + employee.getSurname() + " " + employee.getName());
		
		//Запрос всех записей сотрудников
		List<Employee> employeeList = session.createCriteria(Employee.class).list();
		for (Employee empl : employeeList){
			System.out.println("EmployeeId = " + empl.getEmployeeId() + " Фамилия: " + empl.getSurname());			
		}
		
		//Запрос всех записей проектов
		List<Project> projectList = session.createQuery("from Project").list();
		for (Project prj : projectList){
			System.out.println("ProjectId = " + prj.getProjectId() + " Название проекта: " + prj.getName());
		}
		
		//Запрос всех записей задач
		List<Task> taskList = session.createQuery("from Task").list();
		for (Task task : taskList){
			System.out.println("TaskId = " + task.getTaskId() + " Название задачи: " + task.getName());
		}
		
		//Запрос всех записей событий
		List<Timesheet> timesheetList = session.createQuery("from Timesheet").list();
		for (Timesheet timesheet : timesheetList){
			System.out.println("Id = " + timesheet.getId() + " Проект: " + timesheet.getProject().getName() +
					" Работа: " + timesheet.getTask().getName() + " Сотрудник: " + timesheet.getEmployee().getSurname());
		}
		
		Query query = session.createQuery("select p.name as project, t.name as task, SUM(s.countTime) as sumHours"
				+ 			" from Project p, Task t, Timesheet s"
				+ 			" where p.projectId = s.project.projectId AND t.taskId = s.task.taskId"
				+ 			" AND s.employee.employeeId = :employeeId "
				+ 			" GROUP BY p.name, t.name")
				.setParameter("employeeId", 3);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		
		List<Map> result = query.list();
		
		for (Map map : result){
			String projectName = (String)map.get("project");
			String taskName = (String)map.get("task");
			double count = (double)map.get("sumHours");
			System.out.println(">>>>>>>>>Результаты: Проект - " + projectName + ", Задача - " + taskName + ", Часы - " + count);
		}
		
		tx.commit();
		sessionFactory.close();
	}
}
