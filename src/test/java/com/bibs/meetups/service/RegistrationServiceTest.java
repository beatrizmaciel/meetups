package com.bibs.meetups.service;

import com.bibs.meetups.model.entity.Registration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RegistrationServiceTest {

    @BeforeEach // antes de cada teste...
    public void setUp() {
        // dependencia do service e dar um new
    }

    @Test
    @DisplayName("Should save a registration")
    public void saveStudent(){

        // cenario
        Registration registration = createValidRegistration();

        // execucao

        // assert

    }

    private Registration createValidRegistration() {
        return Registration.builder()
                .id(101)
                .name("Paula")
                .dateOfRegistration(LocalDate.now())
                .meetupCommunity("001")
                .build();
    }

}