package com.desafio.crud.client.desafioCrud.service;

import com.desafio.crud.client.desafioCrud.dto.ClientDTO;
import com.desafio.crud.client.desafioCrud.entities.Client;
import com.desafio.crud.client.desafioCrud.repositories.ClientRepository;
import com.desafio.crud.client.desafioCrud.service.exceptions.DataBaseException;
import com.desafio.crud.client.desafioCrud.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){

        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID não encontrado"));
        ClientDTO clientDTO = new ClientDTO(client);
        return clientDTO;
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDTO){
        try{
            Client client = clientRepository.getReferenceById(id); //Não vai no banco, apenas pega a referencia.
            copyDtoToEntity(clientDTO, client);
            client = clientRepository.save(client);
            return new ClientDTO(client);
        }catch(EntityNotFoundException e){
            throw new ResourceNotFoundException("ID não encontrado");
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if(!clientRepository.existsById(id)){
            throw new ResourceNotFoundException("ID não encontrado");
        }
        try {
            clientRepository.deleteById(id);
        }catch(DataIntegrityViolationException e){
            throw new DataBaseException("Falha de integridade relacional");
        }

    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO){
        Client client = new Client();
        copyDtoToEntity(clientDTO, client);
        client = clientRepository.save(client);
        return new ClientDTO(client);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable){
        Page<Client> result = clientRepository.findAll(pageable);
        return result.map(x -> new ClientDTO(x));

    }

    private void copyDtoToEntity(ClientDTO clientDTO, Client client) {
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setIncome(clientDTO.getIncome());
        client.setBirthDate(clientDTO.getBirthDate());
        client.setChildren(clientDTO.getChildren());
    }


}
