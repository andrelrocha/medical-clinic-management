package rocha.andre.api.domain.doctor.UseCase;

import org.springframework.stereotype.Component;
import rocha.andre.api.domain.doctor.Doctor;
import rocha.andre.api.domain.doctor.DoctorRepository;


@Component
public class ListDoctorByIdUseCase {
    private DoctorRepository repository;

    public ListDoctorByIdUseCase(DoctorRepository repository) {
        this.repository = repository;
    }

    public Doctor listDoctorById (Long id) {

            Doctor doctorById = repository.getReferenceById(id);
                    //.orElseThrow(() -> new IllegalArgumentException("Doctor with id " + id + " not found in our database"));

            return doctorById;
    }
}