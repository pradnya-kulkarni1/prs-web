package com.prs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.LineitemRepo;
import com.prs.db.RequestRepo;
import com.prs.model.Lineitem;
import com.prs.model.Product;
import com.prs.model.Request;

@CrossOrigin // This annotation indicates that a specific controller method with this class
//can be invoked by JavaScript code from a different design
@RestController
@RequestMapping("/api/lineitems")
public class LineitemController {

	@Autowired // Automatic dependency injection, we inject the class dependencies through
				// spring bean configuration file.
	private LineitemRepo lineitemRepo; // u can add multiple models
	@Autowired
	private RequestRepo requestRepo;

	// you cannot create an instance of interface, LineitemRepo is a JPA repository
	// that works with entity

	@GetMapping("/") // Used for mapping HTTP GET lineitem onto this handler method.
	public List<Lineitem> getAllLineitems() // this method returns List
	{
		return lineitemRepo.findAll();

	}

	@GetMapping("/{id}")
	public Lineitem getAllLineitemById(@PathVariable int id) {
		// Optional class is a container object which is used to contain a value that
		// might or might not be used.
		System.out.println("getAllLineitemById "+id);
		Optional<Lineitem> l = lineitemRepo.findById(id);

		if (l.isPresent()) {
			
			return l.get();
			// select * from Lineitem where id = id
		} else {
			System.err.println("Lineitem[" + id + "] does not exist");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lineitem not found: id [" + id + "]");
		}

	}

//	@GetMapping("/getlinesforrequest/{id}")
//	public List<Lineitem> getLineitemsForRequest(@PathVariable int id) {
//		Optional<Lineitem> l = lineitemRepo.findById(id);
//		List<Lineitem> linesByRequest = new ArrayList<Lineitem>();
//
//		if (l.isPresent()) {
//			Lineitem l2 = l.get();
//			Request r = l2.getRequest();
//			linesByRequest = lineitemRepo.findByRequest(r);
//		} else
//			System.out.println("LineItem does not exist");
//
//		return linesByRequest;
//
//	}
	@GetMapping("/getlinesforrequest/{id}")
	public List<Lineitem> getLineitemsForRequest1(@PathVariable int id) {
		System.out.println("getlinesforrequest : "+id);
		Optional<Request> r = requestRepo.findById(id);
		List<Lineitem> linesByRequest = new ArrayList<Lineitem>();

		if (r.isPresent()) {
			Request req = r.get();
			linesByRequest = lineitemRepo.findByRequest(req);
			System.out.println("linesByRequest:");
			for (Lineitem li: linesByRequest) {
				System.out.println(li);
			}
		} else
			System.out.println("LineItem does not exist");

		return linesByRequest;

	}

	@PostMapping("")
	public Lineitem addLineitem(@RequestBody Lineitem lineitem) {
		Lineitem l1 = null;
		Request r = null;
		System.out.println("saving line item: "+lineitem);
		l1 = lineitemRepo.save(lineitem);// insert into lineitem from body
		r = requestRepo.getById(l1.getRequest().getId());
		recalculateRequestTotal2(r);
		lineitemRepo.save(l1);
		return l1;
	}

	@PutMapping("/{id}")
	public Lineitem updateLineitem(@PathVariable int id, @RequestBody Lineitem lineitem) {
		Lineitem l = null;
		if (id != lineitem.getId()) {
			System.err.println("Lineitem Id does not match path id.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lineitem not found");
		} else if (!lineitemRepo.existsById(id)) {
			System.err.println("Lineitem does not exist for id: " + id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lineitem does not exist");
		} else
			try {
				Request r1 = lineitem.getRequest();
				l = lineitemRepo.save(lineitem);
				recalculateRequestTotal2(r1);
				// update lineitem as body where id = id
			} catch (Exception e) {
				System.err.println(e);
				throw e;
			}
		System.out.println("LineitemId after update "+l.getId());
		return l;

	}

	@DeleteMapping("/{id}")
	public boolean deleteLineitem(@PathVariable int id) {
		boolean success = false;

		Lineitem l = null;
		if (lineitemRepo.existsById(id)) {
			l = lineitemRepo.getById(id);
			Request request = l.getRequest();
			lineitemRepo.deleteById(id);
			recalculateRequestTotal2(request);
			success = true;
		}

		else {
			System.err.println("Delete Error: No lineitem exists for id: " + id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lineitem does not exist");
		}

		return success;
	}

	private void recalculateRequestTotal2(Request r1) {
		float sum = 0;

		for (Lineitem l1 : lineitemRepo.findByRequest(r1)) {

			int qty = l1.getQuantity();

			Product product = l1.getProduct();

			float priceOfReqProduct = product.getPrice();
			System.out.println("Quantity = " + l1.getQuantity() + "Price = " + product.getPrice());
			sum += (qty * priceOfReqProduct);
			System.out.println("Total= " + sum);
		}

		r1.setTotal(sum);
		requestRepo.save(r1);

	}

}
