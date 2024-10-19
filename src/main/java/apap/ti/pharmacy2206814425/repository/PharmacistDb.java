package apap.ti.pharmacy2206814425.repository;

import apap.ti.pharmacy2206814425.model.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PharmacistDb extends JpaRepository<Pharmacist, UUID> {
    Pharmacist findFirstByName(String name);
}
