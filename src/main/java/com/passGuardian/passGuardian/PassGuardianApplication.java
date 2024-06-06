package com.passGuardian.passGuardian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PassGuardianApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassGuardianApplication.class, args);
	}

}
