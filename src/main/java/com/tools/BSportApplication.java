package com.tools;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class BSportApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BSportApplication.class, args);
	}

	@Override//为了打包springboot项目
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}
	
//	@Bean
//	public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
//		return new CommandLineRunner(){
//
//			@Override
//			public void run(String... args) throws Exception {
//				System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//	            String[] beanNames = ctx.getBeanDefinitionNames();
//	            Arrays.sort(beanNames);
//	            for (String beanName : beanNames) {
//	                System.out.println(beanName);
//	            }
//			}
//
//		};
//
//	}

}
