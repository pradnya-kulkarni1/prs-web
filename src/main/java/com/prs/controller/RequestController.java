package com.prs.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.RequestRepo;
import com.prs.model.Request;


@CrossOrigin // This annotation indicates that a specific controller method with this class 
//can be invoked by JavaScript code from a different design
@RestController
@RequestMapping("/api/requests")
public class RequestController {


	@Autowired		//Automatic dependency injection, we inject the class dependencies through spring bean configuration file.
	private RequestRepo requestRepo; // u can add multiple models
	private static final String REVIEW = "Review";
	private static final String APPROVE = "Approved";
	private static final String REJECT = "Reject";
	// you cannot create an instance of interface, creditRepo is a JPA repository that works with entity
	
	@GetMapping("/")// Used for mapping HTTP GET request onto this handler method.
	public List<Request> getAllRequests() // this method returns List 
	{
		
		return requestRepo.findAll();
		
	
	}
	

	@GetMapping("/{id}")
	public Request getAllRequestById(@PathVariable int id) 
	{
		//Optional class is a container object which is used to cotain a value that might or might not be used.
		Optional<Request> r = requestRepo.findById(id);
		
		if(r.isPresent()) {
		return r.get(); //select * from request where id = id
		} else {
			System.err.println("Request["+id+"] does not exist");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found: id ["+id+"]");
		}
	}
	
	@GetMapping("/reviews/{UserId}")
	public List<Request> getAllRequestForReview(@PathVariable int UserId) 
	{
		System.out.println("Status = "+REVIEW+" userid = "+UserId);
		List<Request> req =  requestRepo.findByStatusAndUserIdNot(REVIEW, UserId);
		
		return req;
	}

	@PostMapping("/review/{id}")
	public Request submitRequestForReveiw(@RequestBody Request request,@PathVariable int id) 
	{
	
			if(request.getTotal() <= 50.0)
			{
				request.setStatus(APPROVE);
				request.setSubmitteddate(LocalDateTime.now());
			}else 
			{
				
				request.setStatus(REVIEW);
			}
			
		return requestRepo.save(request);// insert into request from body

	}
	@PostMapping("/approve/{id}")
	public Request approveRequest(@RequestBody Request request,@PathVariable int id) 
	{
				request.setStatus(APPROVE);
			
		return requestRepo.save(request);// insert into request from body

	}
	@PostMapping("/reject/{id}")
	public Request rejectRequest(@RequestBody Request request,@PathVariable int id) 
	{
				if(request.getReasonForRejection() != null) {
				
				request.setStatus(REJECT);}
				else {
					System.err.println("Please give Reason for rejection");
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Request not found");
				}
			
		return requestRepo.save(request);// insert into request from body

	}
	@PostMapping("")
	public Request addRequest(@RequestBody Request request) 
	{
			
			request.setStatus("NEW");
			request.setSubmitteddate(LocalDateTime.now());
			request.setTotal(0);
			
		return requestRepo.save(request);// insert into request from body

	}
	
	@PutMapping("/{id}")
	public Request updateRequest(@PathVariable int id, @RequestBody Request request) 
	{
		Request r = null;
		if(id!= request.getId()) {
			System.err.println("Request Id ["+r.getId()+"] does not match path id["+id+"].");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Request not found");
				}
		else if(!requestRepo.existsById(id)) { //keyword By is equivalent to where in SQL
			System.err.println("Request does not exist for id: "+id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Request does not exist");
			
			
		}
		else 
		try 
			{
				r = requestRepo.save(request); // update request as body where id = id
			}
		catch (Exception e) {System.err.println(e); throw e;}
		
		return r;

	}
	
	@DeleteMapping("/{id}")
	public boolean deleteRequest(@PathVariable int id) {
		boolean success = false;
		
		if(requestRepo.existsById(id)) {
			requestRepo.deleteById(id); // delete request where id = id
			success = true;
		}
		else {
			System.err.println("Delete Error: No request exists for id: "+id);
			success = false;
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Request does not exist");
		}
		
		return success;
	}
	
}