package org.example.coviddashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CovidDashBoardApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		System.out.println("************************************************SpringApplicationBuilder");
		return application.sources(CovidDashBoardApplication.class);
	}

	public static void main(String[] args) {
		System.out.println("************************************************main");
		SpringApplication.run(CovidDashBoardApplication.class, args);
	}


}
