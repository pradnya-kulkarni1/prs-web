package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.UserRepo;
import com.prs.model.User;
import com.prs.model.UserLogin;

@CrossOrigin // This annotation indicates that a specific controller method with this class
				// can be invoked by JavaScript code from a different design
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired // Automatic dependency injection, we inject the class dependencies through
				// spring bean configuration file.
	private UserRepo userRepo; // u can add multiple models

	// you cannot create an instance of interface, creditRepo is a JPA repository
	// that works with entity

	@GetMapping("/") // Used for mapping HTTP GET request onto this handler method.
	public List<User> getAllUsers() // this method returns List
	{
		return userRepo.findAll();
	}

	@GetMapping("/{id}")
	public User getAllUserById(@PathVariable int id) {
		Optional<User> u = userRepo.findById(id); // Optional is a data type in Java

		if (u.isPresent()) {
			return u.get(); // select * from User where id = id
		} else {
			System.err.println("User[" + id + "] does not exist");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: id [" + id + "]");
		}

	}

	@PostMapping("")
	public User addUser(@RequestBody User user) {
		// TODO Check for existence by user.getId() before save?

		return userRepo.save(user);

	}

	@PutMapping("/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User user) {
		User u = null;
		if (id != user.getId()) {
			System.err.println("User Id does not match path id.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
		} else if (!userRepo.existsById(id)) {
			System.err.println("Vendor does not exist for id: " + id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
		} else
			try {
				u = userRepo.save(user); // update user as body where id = id
			} catch (Exception e) {
				System.err.println(e);
				throw e;
			}
		return u;

	}

	@DeleteMapping("/{id}")
	public boolean deleteUser(@PathVariable int id) {
		boolean success = false;

		if (userRepo.existsById(id)) {
			userRepo.deleteById(id);
			success = true;
		} else {
			System.err.println("Delete Error: No User exists for id: " + id);
			success = false;
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		return success;
	}

	@PostMapping("/login")
	public User login(@RequestBody UserLogin ul) {
		User user = userRepo.findByUsernameAndPassword(ul.getUsername(), ul.getPassword());

		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username and password not found");
		}
		return user;
	}

}
