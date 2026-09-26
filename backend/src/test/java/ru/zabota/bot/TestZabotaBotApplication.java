package ru.zabota.bot;

import org.springframework.boot.SpringApplication;

public class TestZabotaBotApplication {

	public static void main(String[] args) {
		SpringApplication.from(ZabotaBotApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
