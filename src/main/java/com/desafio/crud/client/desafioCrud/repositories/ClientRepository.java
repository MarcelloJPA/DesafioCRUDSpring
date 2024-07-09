package com.desafio.crud.client.desafioCrud.repositories;

import com.desafio.crud.client.desafioCrud.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
