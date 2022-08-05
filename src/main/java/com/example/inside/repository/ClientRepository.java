package com.example.inside.repository;

import com.example.inside.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByName(String name);

    Optional<Client> findByNameAndPassword(String name, String password);
}
