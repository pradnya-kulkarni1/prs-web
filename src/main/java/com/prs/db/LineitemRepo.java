package com.prs.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Lineitem;
import com.prs.model.Request;

public interface LineitemRepo extends JpaRepository<Lineitem, Integer>{
 List<Lineitem> findByRequest(Optional<Request> r1);
 List<Lineitem> findByRequest(Request r1);

 
 Lineitem getById(int id);
}
