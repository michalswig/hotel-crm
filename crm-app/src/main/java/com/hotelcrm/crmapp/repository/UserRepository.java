package com.hotelcrm.crmapp.repository;

import com.hotelcrm.crmapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    @Query("""
    SELECT u FROM User u
    LEFT JOIN FETCH u.hotel
    LEFT JOIN FETCH u.role
    WHERE u.username = :username
""")
    Optional<User> findByUsernameWithAllRelations(@Param("username") String username);


}
