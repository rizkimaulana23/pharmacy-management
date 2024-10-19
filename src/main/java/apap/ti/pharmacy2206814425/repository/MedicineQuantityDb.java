package apap.ti.pharmacy2206814425.repository;

import apap.ti.pharmacy2206814425.model.MedicineQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicineQuantityDb extends JpaRepository<MedicineQuantity, UUID> {
    List<MedicineQuantity> findAllByPrescriptionId(String prescriptionId);
    void deleteAllByPrescriptionId(String prescriptionId);
}
