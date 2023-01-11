package com.fss.saber.adapter.repository;

import org.springframework.data.repository.CrudRepository;

import com.fss.saber.adapter.jpa.TransactionDetails;

public interface TransactionRepository extends CrudRepository<TransactionDetails, Long>{
	
	TransactionDetails findById(String rrn);
}
