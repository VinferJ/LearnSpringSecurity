package me.vinfer.jwtsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class JwtsecurityApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void testPasswordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String pass = "123456";
		PasswordEncoder encoder2 = new BCryptPasswordEncoder();

		for (int i = 1; i <= 5; i++) {
			String encodePass = encoder.encode(pass);
			System.out.println("第"+i+"次加密："+encodePass);
			System.out.println("第"+i+"次匹配："+encoder2.matches(pass,encodePass));
		}

	}

}
