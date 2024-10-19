package apap.ti.pharmacy2206814425.repository;

import apap.ti.pharmacy2206814425.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientDb extends JpaRepository<Patient, UUID> {
    Patient findByNik(String nik);
}
