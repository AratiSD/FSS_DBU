package com.fss.saber.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fss.saber.adapter.jpa.AadhaarBankBin;

@Repository
public interface AadharBankBinRepoitory extends JpaRepository<AadhaarBankBin, String> {
	
	

}
