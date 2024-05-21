package com.umb.fileshare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.umb.fileshare.model.EntityRole;
import com.umb.fileshare.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(EntityRole name);

    Optional<Role> findById(@NonNull Long id);

}
