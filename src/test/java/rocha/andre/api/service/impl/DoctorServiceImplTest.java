package rocha.andre.api.service.impl;
;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import rocha.andre.api.domain.address.DataAddress;
import rocha.andre.api.domain.doctor.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class DoctorServiceImplTest {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorServiceImpl doctorService;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        doctorRepository.deleteAll();
    }

    @AfterEach
    void tearDown() throws Exception {
            autoCloseable.close();
    }

    @Test
    @DisplayName("It should return a page of doctors")
    void getAllDoctorsScenario1() {
        //given
        var doctor1 = createDoctor("user1", "email1@email.com", "123456", Specialty.cardiology, true);
        doctorRepository.save(doctor1);

        Sort descendingSortByEmail = Sort.by(Sort.Order.desc("email"));
        Pageable pageable = PageRequest.of(0, 10, descendingSortByEmail);

        //when
        Page<DoctorReturnDTO> result = doctorService.getAllDoctors(pageable);

        //then
        assertNotNull(result);
        assertTrue(result instanceof Page);
        assertTrue(doctorRepository.findActiveById(doctor1.getId()));
    }

    @Test
    @DisplayName("It should return all doctors, following the pattern provided")
    void getAllDoctorsScenario2() {
        //given
        var doctor1 = createDoctor("user1", "email1@email.com", "123456", Specialty.cardiology, true);
        var doctor2 = createDoctor("user2", "zemail2@email.com", "654321", Specialty.cardiology, true);
        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);

        Sort descendingSortByEmail = Sort.by(Sort.Order.desc("email"));
        Pageable pageable = PageRequest.of(0, 10, descendingSortByEmail);

        //when
        Page<DoctorReturnDTO> result = doctorService.getAllDoctors(pageable);

        //then
        assertNotNull(result);
        assertTrue(result instanceof Page);

        String email1 = result.getContent().get(0).email();
        String email2 = result.getContent().get(1).email();
        assertTrue(email1.compareTo(email2) >= 0, "A lista nao está ordenada de forma descrescente, " +
                "seguindo a ordem por email");
    }

    @Test
    void getDoctorById() {
        //given
        var doctor = createDoctor("user1", "email1@email.com", "123456", Specialty.cardiology, true);
        var doctorSaved = doctorRepository.save(doctor);

        //when
        var doctorByIdCalled = doctorService.getDoctorById(doctorSaved.getId());

        //then
        assertEquals(doctorSaved.getName(), doctorByIdCalled.name());
    }

    @Test
    void createDoctor() {
        //given
        var doctor = dataDoctor("user1", "email1@email.com", "123456", Specialty.cardiology, true);

        //when
        doctorService.createDoctor(doctor);
        var doctorCreated = doctorRepository.findByName(doctor.name());

        //then
        assertEquals(doctor.name(), doctorCreated.getName());
    }

    @Test
    void updateDoctor() throws IOException {
        //given
        var doctor = createDoctor("user1", "email1@email.com", "123456", Specialty.cardiology, true);
        var doctorSaved = doctorRepository.save(doctor);

        //when
        var updatedName = "novo user100"; // Novo nome após a atualização
        var doctorUpdateInfo = new DoctorUpdateDTO(doctorSaved.getId(), updatedName, "85999999999", addressData());
        doctorService.updateDoctor(doctorUpdateInfo);

        var updatedDoctor = doctorRepository.findById(doctorSaved.getId()).orElse(null);

        //then
        assertNotNull(updatedDoctor);
        assertNotEquals(doctor.getName(), updatedDoctor.getName());
    }


    //IT HASNT PASSED YET, MAYBE I NEED TO REFACTOR ITS USECASE
    @Test
    void deleteDoctor() {
        // given
        var doctor = createDoctor("user1", "email1@email.com", "123456", Specialty.cardiology, true);
        var doctorSaved = doctorRepository.save(doctor);

        // when
        doctorService.deleteDoctor(doctorSaved.getId());

        // then
        assertFalse(doctorSaved.isActive());
    }


    ////////////////////
    private DoctorDTO dataDoctor(String name, String email, String crm, Specialty specialty, Boolean active) {
        return new DoctorDTO(
                name,
                email,
                "61999999999",
                crm,
                specialty,
                addressData(),
                active
        );
    }

    private DataAddress addressData() {
        return new DataAddress(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }

    private Doctor createDoctor(String name, String email, String crm, Specialty specialty, Boolean active) {
        var doctor = new Doctor(dataDoctor(name, email, crm, specialty, active));
        return doctor;
    }
}