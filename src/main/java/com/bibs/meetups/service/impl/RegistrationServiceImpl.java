package com.bibs.meetups.service.impl;

import com.bibs.meetups.exception.BusinessException;
import com.bibs.meetups.model.entity.Registration;
import com.bibs.meetups.repository.RegistrationRepository;
import com.bibs.meetups.service.RegistrationService;

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
}
