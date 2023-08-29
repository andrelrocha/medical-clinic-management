package rocha.andre.api.domain.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByEmail(String email);
    Page<Patient> findAllByActiveTrue(Pageable pagination);

    @Query("""
            select p.active from Patient p
            where
            p.id = :patientId
            """)
    boolean findActiveById(Long patientId);
}
