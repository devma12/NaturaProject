package com.natura.web.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@SpringBootApplication
@ComponentScan("com.natura.web.server")
class ServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
