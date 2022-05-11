package com.bibs.meetups.controller.resource;

import com.bibs.meetups.model.entity.Registration;
import com.bibs.meetups.controller.dto.RegistrationDTO;
import com.bibs.meetups.service.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    @PutMapping("{id}")
    public RegistrationDTO update(@PathVariable Integer id, RegistrationDTO registrationDTO) {

        return registrationService.getRegistrationByID(id)
                .map(registration -> {
                    registration.setName(registrationDTO.getName());
                    registration.setDateOfRegistration(registrationDTO.getDateOfRegistration());
                    registration = registrationService.update(registration);

                    return modelMapper.map(registration, RegistrationDTO.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping
    public Page<RegistrationDTO> find(RegistrationDTO dto, Pageable pageRequest) {
        Registration filter = modelMapper.map(dto, Registration.class);
        Page<Registration> result = registrationService.find(filter, (PageRequest) pageRequest);

        List<RegistrationDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, RegistrationDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<RegistrationDTO>(list, pageRequest, result.getTotalElements());
    }

}
