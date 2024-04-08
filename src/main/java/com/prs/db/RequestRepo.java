package com.prs.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Request;

public interface RequestRepo extends JpaRepository<Request, Integer>{
	List<Request> findByStatusAndUserIdNot(String status, int id);
	Request getById(int id);
	Request save(Optional<Request> request);
	
}
