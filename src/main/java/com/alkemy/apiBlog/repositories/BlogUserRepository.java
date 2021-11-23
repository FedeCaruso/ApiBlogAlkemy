/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alkemy.apiBlog.repositories;

import com.alkemy.apiBlog.models.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Fede
 */
@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser, String>{
    
    @Query("SELECT u FROM BlogUser u WHERE u.username = :username")
    BlogUser findByUsername(@Param("username") String username);
}
