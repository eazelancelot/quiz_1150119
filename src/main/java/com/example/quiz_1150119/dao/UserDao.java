package com.example.quiz_1150119.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz_1150119.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, String> {

	@Modifying
	@Transactional
	@Query(value = "insert ignore into user (email, name, phone, age) values (?1, ?2, ?3, ?4)", //
			nativeQuery = true)
	public void insert(String email, String name, String phone, int age);
	
	@Query(value = "select count(email) from user where email = ?1", //
			nativeQuery = true)
	public int selectCount(String email);
	
	@Query(value = "select * from user where email = ?1", nativeQuery = true)
	public User getByEmail(String email);
	
}
