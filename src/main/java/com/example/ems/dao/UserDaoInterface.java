package com.example.ems.dao;

import com.example.ems.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDaoInterface extends JpaRepository<User,Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

}
