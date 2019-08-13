package com.pt.osm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class OsmApplication {

	public static void main(String[] args) {
		SpringApplication.run(OsmApplication.class, args);
	}
	
	public static ApplicationContext ctx;

	@Autowired
	private void setApplicationContext(ApplicationContext applicationContext) {
		ctx = applicationContext;
		
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}
	

	@GetMapping("/home")
	public String home() {
		return "home";
	}

	@GetMapping("/upload")
	public String upload() {
		return "upload";
	}


	@GetMapping("/groupleader")
	public String groupleader() {
		return "groupleader";
	}

	@GetMapping("/teamleader")
	public String teamleader() {
		return "teamleader";
	}

	@GetMapping("/ordermanager")
	public String ordermanager() {
		return "ordermanager";
	}

	@GetMapping("/requesttrainingmanager")
	public String requesttrainingmanager() {
		return "requesttrainingmanager";
	}

	@GetMapping("/ceo")
	public String ceo() {
		return "ceo";
	}
	
	@GetMapping("/germanside")
	public String germanside() {
		return "germanside";
	}

	@GetMapping("/usertype")
	public String usertype() {
		return "usertype";
	}

	@GetMapping("/user")
	public String user() {
		return "user";
	}

}
