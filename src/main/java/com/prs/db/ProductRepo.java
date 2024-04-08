package com.prs.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{

}
// By implementing JpaRepository interface, we can use methods written in repository, they are abstract
// JpaRepository has all CRUD methods