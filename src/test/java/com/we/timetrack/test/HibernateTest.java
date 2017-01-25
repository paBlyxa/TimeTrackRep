package com.we.timetrack.test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.Transaction;

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
				//.addAnnotatedClass(Employee.class)
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
//		Employee employee;
//		
//		//Get employee
//		employee = session.get(Employee.class, 1);
//		System.out.println("Employee: " + employee.getSurname() + " " + employee.getName());
//		
//		
//		//Get employee's list
//		List<Employee> employeeList = session.createCriteria(Employee.class).list();
//		for (Employee empl : employeeList){
//			System.out.println("EmployeeId = " + empl.getEmployeeId() + " surname: " + empl.getSurname());			
//		}
		
		//Get project's list
		List<Project> projectList = session.createQuery("from Project").list();
		for (Project prj : projectList){
			System.out.println("ProjectId = " + prj.getProjectId() + " project.name: " + prj.getName());
			Set<UUID> leaders = prj.getProjectLeaders();
			for (UUID lead : leaders){
				System.out.println(lead);
			}
		}
		
		//Get task's list
		List<Task> taskList = session.createQuery("from Task").list();
		for (Task task : taskList){
			System.out.println("TaskId = " + task.getTaskId() + " task.name: " + task.getName());
		}
		
		//Get timesheet's list
		List<Timesheet> timesheetList = session.createQuery("from Timesheet").list();
		for (Timesheet timesheet : timesheetList){
			print(timesheet);
		}
		
		Query query = session.createQuery("select p.name as project, t.name as task, SUM(s.countTime) as sumHours"
				+ 			" from Project p, Task t, Timesheet s"
				+ 			" where p.projectId = s.project.projectId AND t.taskId = s.task.taskId"
				+ 			" AND employeeId = :employeeId "
				+ 			" GROUP BY p.name, t.name")
				.setParameter("employeeId", new UUID(1, 1));
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		
		List<Map> result = query.list();
		
		for (Map map : result){
			String projectName = (String)map.get("project");
			String taskName = (String)map.get("task");
			double count = (double)map.get("sumHours");
			System.out.println(">>>>>>>>>Timesheet: prjoect - " + projectName + ", task - " + taskName + ", hours - " + count);
		}
				
		tx.commit();
		sessionFactory.close();
	}
	
	private static void print(Timesheet timesheet){
		System.out.println("Id = " + timesheet.getId() + " project: " + timesheet.getProject().getName() +
				" task: " + timesheet.getTask().getName() + " Employee: " + timesheet.getEmployeeId());
	}
}
