package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.ProductRepo;
import com.prs.model.Product;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {

	//Dependency injection is a fundamental aspect of the Spring framework, 
	//through which the Spring container "injects" objects into other objects or "dependencies".
	//Dependency is nothing but a 'Library' that provides specific functionality that we can use in our application.
	
	
	@Autowired		//Automatic dependency injection, we inject the class dependencies through spring bean configuration file.
	private ProductRepo productRepo; 
	
	
	@GetMapping("/")
	public List<Product> getAllProducts()
	{
		return productRepo.findAll();
	}
	
	@GetMapping("/{id}")
	public Product getAllProductById(@PathVariable int id)
	{
		Optional<Product> p = productRepo.findById(id);
		
		if(p.isPresent())
			{
				return p.get(); //select * from request where id = id
			} else {
					System.err.println("Product["+id+"] does not exist");
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: id ["+id+"]");
					}
		
	}
	
	@PostMapping("")
	public Product addProduct(@RequestBody Product product) 
	{
		return productRepo.save(product);
	}
	
	@PutMapping("/{id}")
	public Product UpdateProduct(@PathVariable int id, @RequestBody Product product)
	{
		//PathVariable annotation is used to handle template variables in the request URL mapping
		//RequestBody annotation indicates that Spring should deserialize a request into an object.
		Product p = null;
		if(id!= product.getId()) 
			{
				System.err.println("Request Id ["+p.getId()+"] does not match path id["+id+"].");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product not found");
			}
		else if(!productRepo.existsById(id))
				{ //keyword By is equivalent to where in SQL
					System.err.println("Product does not exist for id: "+id);
					throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product does not exist");
				}
		else 
		try 
			{
				p = productRepo.save(product); // update request as body where id = id
			}
		catch (Exception e) {System.err.println(e); throw e;}
		
		return p;

	}
	@DeleteMapping("/{id}")
	public boolean deleteProduct(@PathVariable int id) {
		boolean success = false;
		
		if(productRepo.existsById(id)) {
			productRepo.deleteById(id);
			success = true;
		}
		else {
			System.err.println("Delete Error: No product exists for id: "+id);
			success = false;
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product does not exist");
		}
		
		return success;
	}

	
}
