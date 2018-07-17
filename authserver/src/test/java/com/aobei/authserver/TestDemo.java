package com.aobei.authserver;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.aobei.authserver.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDemo {

	@Autowired
	private UserRepository userRepository;
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
	}
	
	/**
	 * 测试
	 */
	@Test
	public void test1() {
		Assert.isTrue(userRepository.findByUsername("user") != null, "ERROR MESSAGE");
	}
}
