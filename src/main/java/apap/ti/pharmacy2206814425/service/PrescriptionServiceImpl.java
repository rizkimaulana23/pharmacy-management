package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.dto.*;
import apap.ti.pharmacy2206814425.model.*;
import apap.ti.pharmacy2206814425.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PatientDb patientDb;
    private final PrescriptionDb prescriptionDb;
    private final PharmacistDb pharmacistDb;
    private final MedicineQuantityDb medicineQuantityDb;
    private final MedicineDb medicineDb;

    public PrescriptionServiceImpl(
            PatientDb patientDb,
            PrescriptionDb prescriptionDb,
            PharmacistDb pharmacistDb,
            MedicineQuantityDb medicineQuantityDb,
            MedicineDb medicineDb) {
        this.patientDb = patientDb;
        this.prescriptionDb = prescriptionDb;
        this.pharmacistDb = pharmacistDb;
        this.medicineQuantityDb = medicineQuantityDb;
        this.medicineDb = medicineDb;
    }

    @Override
    public List<Prescription> getAllPrescription() {
        return prescriptionDb.findAll();
    }

    @Override
    public List<Prescription> getAllPrescriptionWithStatus(Integer status) {
        return prescriptionDb.findAllByStatus(status);
    }

    @Override
    public boolean deletePrescription(String id) {
        Prescription prescription = prescriptionDb.findById(id).isPresent() ? prescriptionDb.findById(id).get() : null;
        if (prescription == null) {
            return false;
        }

        if (prescription.getStatus() == 2 || prescription.getStatus() == 3) {
            return false;
        }

        List<MedicineQuantity> listMq = medicineQuantityDb.findAllByPrescriptionId(prescription.getId());
        for (MedicineQuantity mq : listMq) {
            Medicine medicine = medicineDb.findMedicineById(mq.getMedicineId());
            medicine.setStock(medicine.getStock() + mq.getFulfilledQty());
            medicineDb.save(medicine);

            medicineQuantityDb.delete(mq);
        }

        prescriptionDb.delete(prescription);
        return true;
    }

    @Override
    public Prescription getPrescription(String id) {
        return prescriptionDb.findById(id).isPresent() ? prescriptionDb.findById(id).get() : null;
    }

    @Override
    public Prescription createPrescription(CreatePrescriptionDTO dto) {
        Prescription prescription = new Prescription();
        prescription.setId(generatePrescriptionId(dto));
        prescription.setStatus(0);

        List<CreateMedicineQuantityDTO> listDTOUnique = changeCreateMedicineQuantityDTOtoUnique(dto.getListMedicineQuantity());
        Long totalPrice = 0L;
        for (CreateMedicineQuantityDTO mqDTO : listDTOUnique) {
            MedicineQuantity mq = new MedicineQuantity();
            mq.setId(UUID.randomUUID());
            mq.setPrescriptionId(prescription.getId());
            mq.setMedicineId(mqDTO.getMedicineId());
            mq.setFulfilledQty(0);
            mq.setQuantity(mqDTO.getQty());
            medicineQuantityDb.save(mq);

            Medicine medicine = medicineDb.findMedicineById(mqDTO.getMedicineId());
            totalPrice += medicine.getPrice() * mq.getQuantity();
        }
        prescription.setTotalPrice(totalPrice);
        prescription.setCreatedBy(pharmacistDb.findById(dto.getPharmacistId()).get().getName());
        prescription.setUpdatedBy(pharmacistDb.findById(dto.getPharmacistId()).get().getName());

        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setNik(dto.getNik());
        patient.setName(dto.getName());
        patient.setBirthDate(dto.getBirthDate());
        patient.setEmail(dto.getEmail());
        patient.setGender(dto.getGender());
        patient.setCreatedBy(pharmacistDb.findById(dto.getPharmacistId()).get().getName());
        patient.setUpdatedBy(pharmacistDb.findById(dto.getPharmacistId()).get().getName());
        Patient newPatient = patientDb.save(patient);

        prescription.setPatient(newPatient);
        return prescriptionDb.save(prescription);
    }

    @Override
    public Prescription createPrescription(UUID id, CreatePrescriptionRegisteredDTO dto) {
        Patient patient = patientDb.findById(id).get();

        Prescription prescription = new Prescription();
        CreatePrescriptionDTO createDTO = new CreatePrescriptionDTO();
        createDTO.setListMedicineQuantity(dto.getListMedicineQuantity());
        prescription.setId(generatePrescriptionId(createDTO));
        prescription.setStatus(0);

        List<CreateMedicineQuantityDTO> listDTOUnique = changeCreateMedicineQuantityDTOtoUnique(dto.getListMedicineQuantity());
        Long totalPrice = 0L;
        for (CreateMedicineQuantityDTO mqDTO : listDTOUnique) {
            MedicineQuantity mq = new MedicineQuantity();
            mq.setId(UUID.randomUUID());
            mq.setPrescriptionId(prescription.getId());
            mq.setMedicineId(mqDTO.getMedicineId());
            mq.setFulfilledQty(0);
            mq.setQuantity(mqDTO.getQty());
            medicineQuantityDb.save(mq);

            Medicine medicine = medicineDb.findMedicineById(mqDTO.getMedicineId());
            totalPrice += medicine.getPrice() * mq.getQuantity();
        }
        prescription.setTotalPrice(totalPrice);
        prescription.setCreatedBy(pharmacistDb.findById(dto.getPharmacistId()).get().getName());
        prescription.setUpdatedBy(pharmacistDb.findById(dto.getPharmacistId()).get().getName());

        prescription.setPatient(patient);
        return prescriptionDb.save(prescription);
    }

    @Override
    public String generatePrescriptionId(CreatePrescriptionDTO dto) {
        StringBuilder idBuilder = new StringBuilder("RES");

        Long totalMedicine = 0L;
        for (CreateMedicineQuantityDTO mqDTO : dto.getListMedicineQuantity()) {
            totalMedicine += mqDTO.getQty();
        }

        String medicationCount = String.format("%02d", totalMedicine % 100);
        idBuilder.append(medicationCount);

        String dayOfWeek = getDayOfWeek();
        idBuilder.append(dayOfWeek);

        String currentTime = getCurrentTime();
        idBuilder.append(currentTime);

        return idBuilder.toString();
    }

    private String getDayOfWeek() {
        String[] days = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
        Calendar calendar = Calendar.getInstance();
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Calendar.SUNDAY = 1, so subtract 1 to get zero-based index
        return days[dayIndex];
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(new Date());
    }


    private List<CreateMedicineQuantityDTO> changeCreateMedicineQuantityDTOtoUnique(List<CreateMedicineQuantityDTO> listDTO) {
        HashMap<String, CreateMedicineQuantityDTO> map = new HashMap<>();
        for (CreateMedicineQuantityDTO dto : listDTO) {
            if (map.containsKey(dto.getMedicineId())) {
                map.get(dto.getMedicineId()).setQty(map.get(dto.getMedicineId()).getQty() + dto.getQty());
            } else {
                map.put(dto.getMedicineId(), dto);
            }
        }
        List<CreateMedicineQuantityDTO> listDTOUnique = new ArrayList<>();
        for (CreateMedicineQuantityDTO dto : map.values()) {
            listDTOUnique.add(map.get(dto.getMedicineId()));
        }
        return listDTOUnique;
    }

    @Override
    @Transactional
    public Prescription updatePrescription(UpdatePrescriptionDTO dto, String id) {
        Prescription prescription = prescriptionDb.findById(id).isPresent() ? prescriptionDb.findById(id).get() : null;
        if (prescription == null) {
            return null;
        }
        Pharmacist pharmacist = pharmacistDb.findById(dto.getUpdatedBy()).isPresent() ? pharmacistDb.findById(dto.getUpdatedBy()).get() : null;
        if (pharmacist == null) {
            return null;
        }

        if (prescription.getStatus() != 0) {
            return null;
        }

        prescription.setUpdatedBy(pharmacist.getName());
        prescription.setPharmacist(pharmacist);

        medicineQuantityDb.deleteAllByPrescriptionId(id);
        List<UpdateMedicineQuantityDTO> listDTOUnique = changeUpdateMedicineQuantityDTOtoUnique(dto.getListMedicineQuantity());

        Long totalPrice = 0L;
        for (UpdateMedicineQuantityDTO mqDTO : listDTOUnique) {
            MedicineQuantity mq = new MedicineQuantity();
            mq.setId(UUID.randomUUID());
            mq.setPrescriptionId(prescription.getId());
            mq.setMedicineId(mqDTO.getMedicineId());
            mq.setQuantity(mqDTO.getQty());
            mq.setFulfilledQty(0);
            medicineQuantityDb.save(mq);
            Medicine medicine = medicineDb.findById(mqDTO.getMedicineId()).get();
            totalPrice += mq.getQuantity() * medicine.getPrice();
        }
        prescription.setTotalPrice(totalPrice);
        return prescriptionDb.save(prescription);
    }

    private List<UpdateMedicineQuantityDTO> changeUpdateMedicineQuantityDTOtoUnique(List<UpdateMedicineQuantityDTO> listDTO) {
        HashMap<String, UpdateMedicineQuantityDTO> map = new HashMap<>();
        for (UpdateMedicineQuantityDTO dto : listDTO) {
            if (map.containsKey(dto.getMedicineId())) {
                map.get(dto.getMedicineId()).setQty(map.get(dto.getMedicineId()).getQty() + dto.getQty());
            } else {
                map.put(dto.getMedicineId(), dto);
            }
        }
        List<UpdateMedicineQuantityDTO> listDTOUnique = new ArrayList<>();
        for (UpdateMedicineQuantityDTO dto : map.values()) {
            listDTOUnique.add(map.get(dto.getMedicineId()));
        }
        return listDTOUnique;
    }

    @Override
    public Prescription processPrescription(ProcessPrescriptionDTO dto, String id) {
        Prescription prescription = prescriptionDb.findById(id).isPresent() ? prescriptionDb.findById(id).get() : null;
        if (prescription == null) {
            return null;
        }
        if (prescription.getStatus() == 3) {
            return null;
        }
        boolean changeStatusToDone = true;
        List<MedicineQuantity> listMq = medicineQuantityDb.findAllByPrescriptionId(id);
        for (MedicineQuantity mq : listMq) {
            Medicine medicine = medicineDb.findById(mq.getMedicineId()).get();
            if (medicine.getStock() >= (mq.getQuantity() - mq.getFulfilledQty())) {
                mq.setFulfilledQty(mq.getQuantity());
                medicine.setStock(medicine.getStock() - mq.getFulfilledQty());
                medicineDb.save(medicine);
                medicineQuantityDb.save(mq);
            } else {
                mq.setFulfilledQty(medicine.getStock());
                medicine.setStock(0);
                medicineDb.save(medicine);
                medicineQuantityDb.save(mq);
                changeStatusToDone = false;
            }
        }
        if (changeStatusToDone) {
            prescription.setStatus(2);
        } else {
            prescription.setStatus(1);
        }
        log.info("YAH");
        log.info(dto.getProcessedBy());
        Pharmacist pharmacist = pharmacistDb.findById(UUID.fromString(dto.getProcessedBy())).isPresent() ? pharmacistDb.findById(UUID.fromString(dto.getProcessedBy())).get() : null;
        prescription.setPharmacist(pharmacist);
        return prescriptionDb.save(prescription);
    }

    private String prescriptionIdGenerator() {
        String result = "";
        result += "RES";

        return null;
    }
}
