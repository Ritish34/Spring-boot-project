package com.example.ems.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ems.model.Address;

public interface AddressDaoInterface extends JpaRepository<Address,Integer>{

}
