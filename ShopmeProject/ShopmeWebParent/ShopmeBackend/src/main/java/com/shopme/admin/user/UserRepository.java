package com.shopme.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	
	@Query(value = "SELECT u from User u WHERE u.email=:email")
	public User getUserByEmail(@Param("email") String email);
	
	@Query(value = "SELECT u from User u WHERE u.id=:id")
	public User getUserById(@Param("id") Integer id);
	
	public Long countById(Integer id);
	
	@Query(value = "SELECT u from User u where CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1% ")
	public Page<User> findAll(String keyword, Pageable pageable);
	 
	
	@Query(value = "UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
}
