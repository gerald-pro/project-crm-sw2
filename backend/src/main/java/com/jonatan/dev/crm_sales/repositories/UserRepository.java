package com.jonatan.dev.crm_sales.repositories;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jonatan.dev.crm_sales.domains.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> 
{
    Optional<User> findByName(String username);
    User findByUsername(String username);
}