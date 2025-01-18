package com.recipebook.recipebook;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//@EnableSwagger2
@SpringBootApplication
public class RecipebookV1Application {

	public static void main(String[] args) {
		SpringApplication.run(RecipebookV1Application.class, args);
	}

	/*@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}*/
}
