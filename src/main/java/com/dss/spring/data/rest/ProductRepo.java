package com.dss.spring.data.rest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel="tasks",path="tasks")
public interface ProductRepo extends JpaRepository<Product, Long>{
	List<Product> findByNameContainingIgnoreCase(String name);
}
