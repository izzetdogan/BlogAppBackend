package com.blog;

import com.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BlogApplicationTests {
	@Autowired
	private  UserRepository userRepository;



	@Test
	void contextLoads() {
	}

	@Test
	public void  repoTest(){
		String className = userRepository.getClass().getName();
		String packName = userRepository.getClass().getPackageName();
		System.out.println(className);
		System.out.println(packName);

	}



}
