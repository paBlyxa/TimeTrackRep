package com.we.timetrack.service.schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.we.timetrack.model.Employee;
import com.we.timetrack.model.EmployeeProperty.PROPERTIES;
import com.we.timetrack.service.DownloadService;
import com.we.timetrack.service.EmployeeManager;
import com.we.timetrack.service.mail.MailManager;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.Message;
import com.we.timetrack.service.model.ScheduleType;

@Component
public class RememberJob implements Job {

	private final static Logger logger = LoggerFactory.getLogger(RememberJob.class);
	
	@Autowired
	private EmployeeManager employeeManager;
	@Autowired
	private MailManager mailManager;
	@Autowired
	private DownloadService downloadService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		ScheduleType scheduleType = (ScheduleType) dataMap.get("scheduleType");
		
		logger.info("Execute remeberJob with schedule {}", scheduleType);

		LocalDate begin, end;

		switch (scheduleType) {
		case OFF:
			return;
		case WEEK:
			begin = LocalDate.now().minusWeeks(1);
			end = LocalDate.now().minusDays(1);
			break;
		case MONTH:
			begin = LocalDate.now().minusMonths(1).withDayOfMonth(1);
			end = LocalDate.now().withDayOfMonth(1).minusDays(1);
			break;
		case QUARTER:
			begin = LocalDate.now().minusMonths(3).withDayOfMonth(1);
			end = LocalDate.now().withDayOfMonth(1).minusDays(1);
			break;
		default:
			return;
		}

		Map<Employee, List<LocalDate>> employees = employeeManager.getNotReadyEmployees(new DateRange(begin, end));

		int notReadyCount = employees.size();
		int sendMessageCount = 0;
		logger.debug("Get list of {} not ready employees", notReadyCount);
		
		for (Employee employee : employees.keySet()) {
			// Check if employee isn't ready
			if (!employees.get(employee).isEmpty()) {
				// Check if employee has remember property or autosave property (and remember in quarter)
				if (scheduleType.name().equals(employeeManager.getProperty(employee, PROPERTIES.remember))
						|| (ScheduleType.QUARTER.equals(scheduleType) && "true".equals(employeeManager.getProperty(employee, PROPERTIES.autoSave)))) {
					logger.info("Employee {} has empty days, send remember", employee.getShortName());
					List<LocalDate> dates = employees.get(employee);
					StringBuilder text = new StringBuilder();
					text.append("Здравствуйте, ").append(employee.getName()).append("!\r\n\r\n")
							.append("У вас отсутствуют записи:\r\n");
					int count = 0;
					for (LocalDate date : dates) {
						if (count > 0) {
							text.append(", ");
						}
						text.append(date.format(DateRange.dateFormat));
						count++;
						if (count >= 5) {
							text.append("\r\n");
							count = 0;
						}
					}
					// Check if remember in quarter and has property autosave
					if (ScheduleType.QUARTER.equals(scheduleType) && "true".equals(employeeManager.getProperty(employee, PROPERTIES.autoSave))) {
						text.append("\r\n\r\nАвтоэкспорт отменен!!!");
					}
					
					text.append("\r\n\r\nПроверьте, пожалуйста, ваш табель!\r\n\r\nСпасибо за понимание!"
							+ "\r\n\r\nПисьмо создано автоматически программой TimeX.\r\n\r\nЕсли вы не хотите получать данное письмо,\r\n"
							+ "вы можете отключить напоминания в вашем аккаунте!");

					mailManager.send(employee.getMail(), text.toString());
					sendMessageCount++;
				}
			} else {
				// Check if remember in quarter and has property autosave
				if (ScheduleType.QUARTER.equals(scheduleType) && "true".equals(employeeManager.getProperty(employee, PROPERTIES.autoSave))) {
					logger.info("Execute autosave for {} ", employee.getShortName());
					Message message = downloadService.exportXLS(employee, new DateRange(begin, end));
					StringBuilder text = new StringBuilder();
					text.append("Здравствуйте, ").append(employee.getName()).append("!\r\n\r\n");
					if (message.getType().equals("OK")) {
						text.append("Файл \"").append(message.getInfo()).append("\" успешно сохранен!");
					} else {
						text.append("Ошибка. Не удалось выполнить экспорт квартального отчета!");
						sendMessageCount++;
					}
					mailManager.send(employee.getMail(), text.toString());
					
				}
			}
		}
		logger.info("Execute remeberJob with schedule {} has completed. Not ready employees {}. Send message {}.", scheduleType, notReadyCount, sendMessageCount);
		
	}

}
