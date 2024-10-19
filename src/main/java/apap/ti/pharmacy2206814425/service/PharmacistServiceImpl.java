package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.model.Pharmacist;
import apap.ti.pharmacy2206814425.repository.PharmacistDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacistServiceImpl implements PharmacistService {

    @Autowired
    private PharmacistDb pharmacistDb;

    @Override
    public Pharmacist addPharmacist(Pharmacist pharmacist) {
        return pharmacistDb.save(pharmacist);
    }

    @Override
    public List<Pharmacist> getAllPharmacists() {
        return pharmacistDb.findAll();
    }
}
