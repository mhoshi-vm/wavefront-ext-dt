package com.example.postgreslueth;

import brave.Span;
import brave.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(PostgreSluethApplication.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	Tracer tracer;

	@RequestMapping("/car")
	public String index () {
		String list = performSql("select * from car");
		return list;
	}
	
	String performSql(String SQL) {
		Span newSpan = this.tracer.nextSpan().name("postgres");
		try {
			newSpan.tag("component", "java-jdbc");
			newSpan.tag("db.type", "postgresql");
			newSpan.tag("db.instance", "localDB");		
		    List<Map<String, Object>> list;
		    list = jdbcTemplate.queryForList(SQL);
		    return  list.toString();
		}finally {			
			newSpan.finish();
		}
	}

}
