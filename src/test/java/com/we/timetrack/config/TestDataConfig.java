package com.we.timetrack.config;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Profile("dataSourceDevelopment")
@Configuration
@EnableTransactionManagement
@ComponentScan
public class TestDataConfig implements TransactionManagementConfigurer {
	
	@Inject
	private SessionFactory sessionFactory;


	@Profile("dataSourceDevelopment")
	@Bean
	public DataSource dataSource() {

		EmbeddedDatabaseBuilder edb = new EmbeddedDatabaseBuilder();
	    edb.setType(EmbeddedDatabaseType.H2);
	    edb.addScript("schema.sql");
	    edb.addScript("test-data.sql");
	    
	    EmbeddedDatabase embeddedDatabase = edb.build();

	    return embeddedDatabase;
	}

	@Profile("dataSourceDevelopment")
	public PlatformTransactionManager annotationDrivenTransactionManager() {

		System.out.println(sessionFactory);
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}

	@Profile("dataSourceDevelopment")
	@Bean
	public SessionFactory sessionFactoryBean() {
		try {
			LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
			lsfb.setDataSource(dataSource());
			lsfb.setPackagesToScan("com.we.timetrack.model");
			Properties props = new Properties();
			props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
			lsfb.setHibernateProperties(props);
			lsfb.afterPropertiesSet();
			SessionFactory object = lsfb.getObject();
			return object;
		} catch (IOException e) {
			return null;

	    }
	  }

}
