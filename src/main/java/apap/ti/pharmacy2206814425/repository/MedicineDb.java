package apap.ti.pharmacy2206814425.repository;

import apap.ti.pharmacy2206814425.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineDb extends JpaRepository<Medicine, String> {
    Medicine findMedicineByName(String name);
    Medicine findMedicineById(String id);
    List<Medicine> findAllByDeletedDateIsNull();
}
