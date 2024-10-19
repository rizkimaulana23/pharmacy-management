package apap.ti.pharmacy2206814425.repository;


import apap.ti.pharmacy2206814425.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionDb extends JpaRepository<Prescription, String> {
    List<Prescription> findAllByStatus(Integer status);
    List<Prescription> findAllByStatusOrStatus(Integer status1, Integer status2);
}
