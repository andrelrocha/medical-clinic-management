package rocha.andre.api.domain.doctor.UseCase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rocha.andre.api.domain.doctor.DataListDoctor;
import rocha.andre.api.domain.doctor.DoctorRepository;

@Component
public class ListDoctorUseCase {
    private DoctorRepository repository;

    public ListDoctorUseCase(DoctorRepository repository) {
        this.repository = repository;
    }

    public Page<DataListDoctor> listDoctor(Pageable pagination) {
            // O método map() é uma operação do Stream que permite transformar cada elemento da sequência
            // em outro objeto usando uma função de mapeamento.
            //return repository.findAll(pagination).stream().map(DataListDoctor::new).toList();

            return repository.findAllByActiveTrue(pagination).map(DataListDoctor::new);
    }
}