package com.bibs.meetups.service.impl;

import com.bibs.meetups.exception.BusinessException;
import com.bibs.meetups.model.entity.Registration;
import com.bibs.meetups.repository.RegistrationRepository;
import com.bibs.meetups.service.RegistrationService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public class RegistrationServiceImpl implements RegistrationService {

    RegistrationRepository repository;
    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.repository = repository;
    }

    public Registration save(Registration registration) {
        if (repository.existsByRegistration(registration.getRegistration())) {
            throw new BusinessException("Registration already created");
        }

        return repository.save(registration);
    }

    @Override
    public Optional<Registration> getRegistrationByID(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Registration registration) {
        if (registration == null || registration.getId() == null) {
            throw new IllegalArgumentException("Registration id can't be null");
        }
        // método delete do JPA:
        this.repository.delete(registration);
    }

    @Override
    public Registration update(Registration registration) {
        if (registration == null || registration.getId() == null) {
            throw new IllegalArgumentException("Registration id can't be null");
        }
        return this.repository.save(registration);
    }

    @Override
    public Page<Registration> find(Registration filter, PageRequest pageRequest) {
        Example<Registration> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Registration> getRegistrationByRegistrationAtr(String registrationAttribute) {
        return repository.findByRegistration(registrationAttribute);
    }
}
