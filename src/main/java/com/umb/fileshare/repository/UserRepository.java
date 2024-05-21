package com.umb.fileshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.umb.fileshare.model.User;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(@NonNull Long id);

    Optional<User> findByUsername(String username);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);
}
