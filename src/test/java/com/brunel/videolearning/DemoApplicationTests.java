package com.brunel.videolearning;

import com.brunel.videolearning.model.User;
import com.brunel.videolearning.repository.UserRepository;
import com.brunel.videolearning.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {

		//check userService instance
		assertNotNull(userService);
		assertNotNull(userRepository);


	}


	//try to read from table , uses email jiayuan@brunel.ac.uk , this user was added by ProjectInit class
	@Test
	void testUser() {
		//create a temp user
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String firstName = "nina";
		String lastName = "nico";
		String email = "nina@nico.com";
		String encryptedPassword = encoder.encode("nina_pass");
		User newUser = User.createANewUser(firstName, lastName, email, encryptedPassword);
		userRepository.save(newUser);

		//read this user from database table
		Optional<User> opUser = userService.findByEmail(email);
		User user = null;
		if (opUser.isPresent()) {
			user = opUser.get();
		}

        assert user != null;
        assertEquals("nina", user.getFirstName());
        assertEquals("nico", user.getLastName());
        assertEquals(1, user.getHeadImageNo());
        assertEquals(1, user.getUserType());
        assertEquals(0, user.getBanStatus());

		//update this user from database table
		firstName = "nina1";
		lastName = "nico1";
		User updatedUser = userService.updateName(user.getId(), firstName, lastName);
        assertEquals("nina1", updatedUser.getFirstName());
        assertEquals("nico1", updatedUser.getLastName());

		//delete the temp user
		userRepository.deleteById(user.getId());
		opUser = userService.findByEmail(email);
		assertTrue(opUser.isEmpty());

	}

}
