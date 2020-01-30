package com.we.timetrack.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.we.timetrack.model.EmployeeProperty.PROPERTIES;
import com.we.timetrack.service.EmployeeManager;
import com.we.timetrack.service.model.EmployeePropertyForm;
import com.we.timetrack.service.model.ScheduleType;

@Controller
@RequestMapping("/properties")
public class PropertyController {
	
	private final static Logger logger = LoggerFactory.getLogger(PropertyController.class);

	@Autowired
	private EmployeeManager employeeManager;
	
	@RequestMapping(method = RequestMethod.GET)
	public String get(Model model) {
		//List<EmployeePropertyForm> employeeList = employeeManager.getEmployeesProperties();
		
		//model.addAttribute("employeeList", employeeList);
		return "properties";
	}
	
	@RequestMapping(value="/users", method = RequestMethod.GET)
	public String getUsers(Model model) {
		
		model.addAttribute("rememberList", ScheduleType.values());
		return "users";
	}
	
	@RequestMapping(value="saveListRemembers", method = RequestMethod.POST)
	public @ResponseBody String saveListRemembers(@RequestBody String[] employeeList) {
		return "success";
	}
	
	@RequestMapping("/getEmployees")
	public @ResponseBody List<EmployeePropertyForm> getEmployees() {
		List<EmployeePropertyForm> employeeList = employeeManager.getEmployeesWithProperties();
		return employeeList;
	}
	
	@RequestMapping("/getRoles")
	public @ResponseBody List<String> getRoles() {
		return employeeManager.getRoles();
	}
	
	@RequestMapping("/getPrivileges")
	public @ResponseBody List<String> getPrivilege() {
		return employeeManager.getPrivileges();
	}
	
	@RequestMapping(value = "/saveRoles", method = RequestMethod.POST)
	public @ResponseBody String saveRoles(@RequestBody EmployeePropertyForm employee) {
		logger.info("Get request save roles for employee \"{}\"", employee.getViewName());
		employeeManager.saveRoles(employee);
		logger.debug("Saving roles for employee \"{}\" is done", employee.getViewName());
		return "{\"msg\":\"success\"}";
	}
	
	@RequestMapping(value = "/addPrivilege", method = RequestMethod.POST)
	public @ResponseBody String addPrivilege(@RequestBody String privilege) {
		logger.info("Get request add privilege \"{}\"", privilege);
		employeeManager.addPrivilege(privilege);
		return "{\"msg\":\"success\"}";
	}
	
	@RequestMapping(value = "/deletePrivilege", method = RequestMethod.POST)
	public @ResponseBody List<String>  deletePrivilege(@RequestBody String[] privileges) {
		logger.info("Get request delete privileges \"{}\"", Arrays.toString(privileges));
		return employeeManager.deletePrivilege(privileges);
	}
	
	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	public @ResponseBody String addRole(@RequestBody String role) {
		logger.info("Get request add role \"{}\"", role);
		employeeManager.addRole(role);
		return "{\"msg\":\"success\"}";
	}
	
	@RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
	public @ResponseBody List<String>  deleteRole(@RequestBody String[] roles) {
		logger.info("Get request delete roles \"{}\"", Arrays.toString(roles));
		return employeeManager.deleteRoles(roles);
	}
	
	@RequestMapping(value = "/getRolePrivileges", method = RequestMethod.POST)
	public @ResponseBody List<String> getRolePrivileges(@RequestBody String role){
		logger.debug("Get request privileges for role \"{}\"", role);
		return employeeManager.getPrivileges(role);
	}
	
	@RequestMapping(value = "/saveRolePrivileges", method = RequestMethod.POST)
	public @ResponseBody String saveRolePrivileges(@RequestBody RolePrivileges rolePrivileges){
		logger.debug("Get request save privileges for role \"{}\"", rolePrivileges.role);
		String msg = employeeManager.saveRolePrivileges(rolePrivileges.role, rolePrivileges.privileges);
		return "{\"msg\":\"" + msg + "\"}";
	}

	@RequestMapping(value = "/saveRemember", method = RequestMethod.POST)
	public @ResponseBody String saveRemember(@RequestBody EmployeePropertyForm employee) {
		logger.info("Get request save property remeber {} for employee \"{}\"", employee.getScheduleType(), employee.getEmployeeId());
		
		employeeManager.saveProperty(employee.getEmployeeId(), PROPERTIES.remember.name(), employee.getScheduleType().name());
		
		return "{\"msg\":\"success\"}";
	}

	@RequestMapping(value = "/saveAutoSave", method = RequestMethod.POST)
	public @ResponseBody String saveAutoSave(@RequestBody EmployeePropertyForm employee) {
		logger.info("Get request save property saveAutoSave {} for employee \"{}\"", employee.isAutoSave(), employee.getEmployeeId());
		
		employeeManager.saveProperty(employee.getEmployeeId(), PROPERTIES.autoSave.name(), Boolean.toString(employee.isAutoSave()));
		
		return "{\"msg\":\"success\"}";
	}
	
	public static class RolePrivileges {
	
		private String role;
		private String[] privileges;
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public String[] getPrivileges() {
			return privileges;
		}
		public void setPrivileges(String[] privileges) {
			this.privileges = privileges;
		}
		
		
	}
}
