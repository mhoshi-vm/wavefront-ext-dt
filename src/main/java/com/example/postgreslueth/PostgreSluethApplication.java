package com.example.postgreslueth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class PostgreSluethApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostgreSluethApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/car")
	public String index() {
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("select * from car");
		return list.toString();
	}

}
