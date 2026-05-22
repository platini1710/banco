package com.bci.tareas;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SpringBootRest2Application    {

	public static void main(String[] args) {
		   SpringApplication.run(SpringBootRest2Application.class, args);
	}

}