package com.example.postgreslueth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import brave.Span;
import brave.Tracer;


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
	
	@Autowired
	Tracer tracer;
	
	@RequestMapping("/car")
	public String index() {
		String list = performSql("select * from car");
		return list;
	}
	
	
/*
	Tracer createWavefrontTracer(String application, String service) throws IOException {
		// Step 1. Create ApplicationTags.
		ApplicationTags applicationTags = new ApplicationTags.Builder(application, service).build();

		// Step 2. Create a WavefrontSender and configure it to send data via a
		// Wavefront proxy. WavefrontClientFactory is used to send data to multiple
		// Wavefront services, such as the metrics port and the distribution port.
		// Assume you have installed and started the proxy on <proxyHostname>.
		WavefrontClientFactory wavefrontClientFactory = new WavefrontClientFactory();
		wavefrontClientFactory.addClient("https://d2702db1-483b-431f-83cc-c6ca0f5262d1@wavefront.surf");
		WavefrontSender wavefrontSender = wavefrontClientFactory.getClient();

		// Step 3. Create a WavefrontSpanReporter for reporting trace data that
		// originates on <sourceName>.
		Reporter wfSpanReporter = new WavefrontSpanReporter.Builder().withSource("test").build(wavefrontSender);

		// Step 4. Create the WavefrontTracer.
		return new WavefrontTracer.Builder(wfSpanReporter, applicationTags).build();
	}*/
	

	String performSql(String SQL) {
		Span newSpan = this.tracer.nextSpan().name("postgres").start();
		try {
			newSpan.tag("component", "java-jdbc");
			newSpan.tag("span.kind", "client");
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
