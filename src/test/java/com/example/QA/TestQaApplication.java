package com.example.QA;

import org.springframework.boot.SpringApplication;

public class TestQaApplication {

	public static void main(String[] args) {
		SpringApplication.from(QaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
