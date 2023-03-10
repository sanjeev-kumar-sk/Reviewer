package com.reviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reviewer.pojos.UserDetails;
import com.reviewer.services.LoginService;

import jakarta.servlet.http.HttpSession;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ResponseEntity<UserDetails> login(HttpSession httpsession, @RequestParam String email,
			@RequestParam String password) {

		try {

			UserDetails user = loginService.authenticateUser(email, password);

			if (user != null) {
				return ResponseEntity.ok(user);
			} else {
				user = new UserDetails();
				user.setEmail("Invalid Email | Password");
				return ResponseEntity.badRequest().body(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
			UserDetails user = new UserDetails();
			user.setEmail("Unexpected Error occured during Login!!");
			return ResponseEntity.internalServerError().body(user);
		}

	}

	@RequestMapping(path = "/signup", method = RequestMethod.POST)
	public ResponseEntity<String> signUp(@RequestBody UserDetails user) {

		try {
			user.setRole("User");
			String res = loginService.validateUserDetails(user);
			if (res.equals("success")) {
				loginService.createUser(user);
				return ResponseEntity.ok("SignUp success");
			} else {
				return ResponseEntity.badRequest().body(res);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Unexpected Error occured during SignUp");
		}

	}
}
