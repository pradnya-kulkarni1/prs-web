package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.VendorRepo;
import com.prs.model.Vendor;

@CrossOrigin // This annotation indicates that a specific controller method with this class 
			//can be invoked by JavaScript code from a different design
@RestController
@RequestMapping("/api/vendors")
public class VendorController {
	
	@Autowired		//Automatic dependency injection, we inject the class dependencies through spring bean configuration file.
	private VendorRepo vendorRepo; // u can add multiple models
	
	// you cannot create an instance of interface, creditRepo is a JPA repository that works with entity
	
	@GetMapping("/")// Used for mapping HTTP GET request onto this handler method.
	public List<Vendor> getAllVendors() // this method returns List 
	{
		return vendorRepo.findAll(); //select * from vendor is findAll() method
	}
	
	@GetMapping("/{id}")
	public Vendor getAllVendorById(@PathVariable int id) 
	{
		//Optional class is a container object which is used to cotain a value that might or might not be used.
		Optional<Vendor> v = vendorRepo.findById(id); 
		
		if(v.isPresent())
			{
				return v.get(); //select * from Vendor where id = id
			} else 
				{
					System.err.println("Vendor["+id+"] does not exist");
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found: id ["+id+"]");
				}
		 
	}
	

	@PostMapping("")
	public Vendor addVendor(@RequestBody Vendor vendor) 
	{
		//TODO Check for existence by vendor.getId() before save?
		
		return vendorRepo.save(vendor);// insert into vendor from body

	}
	

	@PutMapping("/{id}")
	public Vendor updateVendor(@PathVariable int id, @RequestBody Vendor vendor) 
	{
		Vendor v = null;
		if(id!= vendor.getId())
		{
			System.err.println("Vendor Id does not match path id.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Vendor not found");
		}
	else if(!vendorRepo.existsById(id))
			{ 
				System.err.println("Vendor does not exist for id: "+id);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Vendor does not exist");
			}
	else 
	try 
		{
			v = vendorRepo.save(vendor); // update vendor as body where id = id
		}
	catch (Exception e) {System.err.println(e); throw e;}
		
		return v;

	}
	
	@DeleteMapping("/{id}")
	public boolean deleteVendor(@PathVariable int id) {
		boolean success = false;
		
		if(vendorRepo.existsById(id)) {
			vendorRepo.deleteById(id); // delete vendor where id = id
			success = true;
		}
		else {
			System.err.println("Delete Error: No vendor exists for id: "+id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Vendor does not exist");
		}
		
		return success;
	}

}
