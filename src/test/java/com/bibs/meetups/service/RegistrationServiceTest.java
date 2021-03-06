package com.bibs.meetups.service;

import com.bibs.meetups.exception.BusinessException;
import com.bibs.meetups.model.entity.Registration;
import com.bibs.meetups.repository.RegistrationRepository;
import com.bibs.meetups.service.impl.RegistrationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RegistrationServiceTest {

    RegistrationService registrationService;

    @MockBean
    RegistrationRepository repository;

    @BeforeEach // antes de cada teste...
    public void setUp() {
        this.registrationService = new RegistrationServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save a registration")
    public void saveStudent(){

        // cenario
        Registration registration = createValidRegistration();

        // execução (simula o serviço e o controller)
        Mockito.when(repository.existsByRegistration(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(registration)).thenReturn(createValidRegistration());

        Registration savedRegistration = registrationService.save(registration);

        // assert
        assertThat(savedRegistration.getId()).isEqualTo(101);
        assertThat(savedRegistration.getName()).isEqualTo("Paula");
        assertThat(savedRegistration.getDateOfRegistration()).isEqualTo("04/04/2022");
        assertThat(savedRegistration.getRegistration()).isEqualTo("001");

    }

    @Test
    @DisplayName("Should throw business error when try to save a new registration " +
            "with a duplicated registration")
    public void shouldNotSaveAsDuplicatedRegistration() {

        Registration registration = createValidRegistration();
        Mockito.when(repository.existsByRegistration(Mockito.any())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable( () -> registrationService.save(registration));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Registration already created");

        // + uma etapa de verificação (para nunca salvar):
        Mockito.verify(repository, Mockito.never()).save(registration);
    }

    @Test
    @DisplayName("Should get a Registration by Id")
    public void getByRegistrationIdTest() {

        // cenário
        Integer id = 11;
        Registration registration = createValidRegistration();
        registration.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(registration));

        // execução
        Optional<Registration> registrationFound = registrationService.getRegistrationByID(id);

        assertThat(registrationFound.isPresent()).isTrue();
        assertThat(registrationFound.get().getId()).isEqualTo(id);
        assertThat(registrationFound.get().getName()).isEqualTo(registration.getName());
        assertThat(registrationFound.get().getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());
        assertThat(registrationFound.get().getRegistration()).isEqualTo(registration.getRegistration());
    }

    @Test
    @DisplayName("Should return empty when a registration by id doesn't exist")
    public void registrationNotFoundById() {

        // cenario de erro
        Integer id = 11;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty()); // objeto vazio

        Optional<Registration> registration = registrationService.getRegistrationByID(id);

        // a validação tem que retornar falso pq dá erro quando perguntamos se o objeto está presente:
        assertThat(registration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should delete a student")
    public void deleteRegistrationTest() {

        // builder() é um design pattern usado para buscar e construir infos, aqui estamos construindo só um id
        Registration registration = Registration.builder().id(11).build();

        assertDoesNotThrow(() -> registrationService.delete(registration));

        Mockito.verify(repository, Mockito.times(1)).delete(registration); // para chamar uma única vez

    }

    @Test
    @DisplayName("Should update a registration")
    public void updateRegistration() {

        // cenario
        Integer id = 11;
        Registration updatingRegistration = Registration.builder().id(11).build();

        // execução
        // depois de validar, retornar esse registration:
        Registration updatedRegistration = createValidRegistration();
        updatedRegistration.setId(id);

        Mockito.when(repository.save(updatingRegistration)).thenReturn(updatedRegistration);
        Registration registration = registrationService.update(updatingRegistration);

        // assert
        assertThat(registration.getId()).isEqualTo(updatedRegistration.getId());
        assertThat(registration.getName()).isEqualTo(updatedRegistration.getName());
        assertThat(registration.getDateOfRegistration()).isEqualTo(updatedRegistration.getDateOfRegistration());
        assertThat(registration.getRegistration()).isEqualTo(updatedRegistration.getRegistration());

    }

    @Test
    @DisplayName("Should filter registration throw its properties")
    public void findRegistrationTest() {

        // cenario
        Registration registration = createValidRegistration();
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Registration> listRegistrations = Arrays.asList(registration);
        Page<Registration> page = new PageImpl<Registration>(Arrays.asList(registration), PageRequest.of(0, 10), 1);

        // execução
        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page); // retorna o cenário de Page que criamos acima

        Page<Registration> result = registrationService.find(registration, pageRequest);

        // assert

        assertThat(result.getTotalElements()).isEqualTo(1); // 1 é o total de paginações que especificamos acima
        assertThat(result.getContent()).isEqualTo(listRegistrations); // confere se o conteúdo é o mesmo da nossa lista
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0); // page
        assertThat(result.getPageable().getPageSize()).isEqualTo(10); // size
    }

    @Test
    @DisplayName("Should get a Registration model by registration attribute")
    public void getRegistrationByRegistrationAtr() {

        String registrationAttribute = "1234";

        Mockito.when(repository.findByRegistration(registrationAttribute))
                .thenReturn(Optional.of(Registration.builder().id(11).registration(registrationAttribute).build()));

        Optional<Registration> registration = registrationService.getRegistrationByRegistrationAtr(registrationAttribute);

        assertThat(registration.isPresent()).isTrue();
        assertThat(registration.get().getId()).isEqualTo(11);
        assertThat(registration.get().getRegistration()).isEqualTo(registrationAttribute);

        // usamos o .times para saber quantas vezes aparece
        Mockito.verify(repository, Mockito.times(1)).findByRegistration(registrationAttribute);

    }

    private Registration createValidRegistration() {
        return Registration.builder()
                .id(101)
                .name("Paula")
                .dateOfRegistration("04/04/2022")
                .registration("001")
                .build();
    }

}