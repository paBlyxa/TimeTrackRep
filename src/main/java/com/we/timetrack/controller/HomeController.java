package com.we.timetrack.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller							// Declared to be a controller
@RequestMapping({"/", "/home"})
public class HomeController {

	@RequestMapping(method=GET)		// Handle GET requests for /
	public String home(){
		return "home";							// View name is home
	}
	
	@RequestMapping(value="/help", method=GET)		// Handle GET requests for /
	public String help(){
		return "help";							// View name is help
	}
}
