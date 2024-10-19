package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.model.Pharmacist;

import java.util.List;

public interface PharmacistService {
    Pharmacist addPharmacist(Pharmacist pharmacist);
    List<Pharmacist> getAllPharmacists();
}
