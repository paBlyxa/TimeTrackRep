package com.we.timetrack.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.service.EmployeeManager;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeManager employeeManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getEmployeeList(Model model){
		
		List<Employee> employeeList = employeeRepository.getEmployees();
		Employee employee = new Employee();
		model.addAttribute(employee);
		model.addAttribute(employeeList);
		
		return "employees";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String saveEmployee(Employee employee, Model model){
		
		if (employee != null){
			
			//Сохраняем новую запись
			employeeRepository.saveEmployee(employee);
		}
		return "redirect:/employees";
	}
	
	@RequestMapping(value="/employee", method=RequestMethod.GET)
	public String getSummary(@RequestParam(value="id") int employeeId, Model model){
				
		//Запрос на сводную информацию по проектам пользователя с соответствующим employeeId
		Employee employee = employeeRepository.getEmployee(employeeId);
		
		employeeManager.makeEmployeeSummary(employee);
		Map<String, Float> summaryByTasks = employeeManager.getSummaryByTasks();
		Map<String, Float> summaryByProjects = employeeManager.getSummaryByProjects();
		model.addAttribute("employee", employee);
		model.addAttribute("summaryByTasks", summaryByTasks);
		model.addAttribute("summaryByProjects", summaryByProjects);
		
		return "employee";
	}
}
