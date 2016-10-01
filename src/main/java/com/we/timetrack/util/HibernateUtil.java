package com.we.timetrack.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class HibernateUtil {
	
	 private static final SessionFactory sessionFactory;
	 
	 static{
		 try{
			 // Create the SessionFactory from hibernate.cfg.xml
			 StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					 .configure("hibernate.cfg.xml")
					 .build();
			 
			 Metadata metadata = new MetadataSources(serviceRegistry)
					 .addAnnotatedClass(com.we.timetrack.model.Employee.class)
					 .addAnnotatedClass(com.we.timetrack.model.Project.class)
					 .addAnnotatedClass(com.we.timetrack.model.Task.class)
					 .addAnnotatedClass(com.we.timetrack.model.Timesheet.class)
					 .getMetadataBuilder()
					 .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
					 .build();
			 
			 sessionFactory = metadata.getSessionFactoryBuilder()
					 .build();
		 }
	     catch (Exception e){
	         // Make sure you log the exception, as it might be swallowed
	         System.err.println("Initial SessionFactory creation failed." + e);	         
	         throw new ExceptionInInitializerError(e);
	     }
	 }
	 
	 public static SessionFactory getSessionFactory(){
		 return sessionFactory;
	 }
	 
	 public static void shutdown(){
		 // Close caches and connection pools
		 getSessionFactory().close();
	 }
}
