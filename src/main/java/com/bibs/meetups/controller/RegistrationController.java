package com.bibs.meetups.controller;

import com.bibs.meetups.model.entity.Registration;
import com.bibs.meetups.model.entity.RegistrationDTO;
import com.bibs.meetups.service.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {


    private RegistrationService registrationService;

    private ModelMapper modelMapper;

    public RegistrationController(RegistrationService registrationService, ModelMapper modelMapper) {
        this.registrationService = registrationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // aqui botamos CREATED pq é o que passamos no teste de controller (201)
    public RegistrationDTO create(@RequestBody @Valid RegistrationDTO registrationDTO) {

        Registration entity = modelMapper.map(registrationDTO, Registration.class);

        entity = registrationService.save(entity); // serviço dentro do método save

        return modelMapper.map(entity, RegistrationDTO.class);

    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegistrationDTO get (@PathVariable Integer id) {

        return registrationService
                .getRegistrationByID(id) // busca por id
                .map(registration -> modelMapper.map(registration, RegistrationDTO.class)) // mapear entidade/dados de transferência com lambda functions
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND)); // caso não encontre os dados, dá o NOT FOUND

    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegistrationbyId(@PathVariable Integer id) {
        Registration registration = registrationService.getRegistrationByID(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        registrationService.delete(registration);
    }

}
