package com.bibs.meetups.service;

import com.bibs.meetups.model.entity.Registration;

import java.util.Optional;

public interface RegistrationService {

    Registration save(Registration any);

    Optional<Registration> getRegistrationByID(Integer id);

    void delete(Registration registration);

    Registration update(Registration registration);
}
