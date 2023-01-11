package com.fss.saber.adapter.repository;

import org.springframework.data.repository.CrudRepository;

import com.fss.saber.adapter.jpa.UserAthnMstr;

public interface AgentRepository extends CrudRepository<UserAthnMstr, String> {

}
