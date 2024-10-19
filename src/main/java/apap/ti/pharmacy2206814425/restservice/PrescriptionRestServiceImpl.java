package apap.ti.pharmacy2206814425.restservice;

import apap.ti.pharmacy2206814425.model.*;
import apap.ti.pharmacy2206814425.repository.*;
import apap.ti.pharmacy2206814425.restdto.request.CreatePrescriptionRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.FindPatientRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.MedicineQuantityRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.response.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.*;

@Service
@Transactional
@Slf4j
public class PrescriptionRestServiceImpl implements PrescriptionRestService{

    @Autowired
    private MedicineDb medicineDb;

    @Autowired
    private PatientDb patientDb;

    @Autowired
    private PrescriptionDb prescriptionDb;

    @Autowired
    private PharmacistDb pharmacistDb;

    @Autowired
    private MedicineQuantityDb medicineQuantityDb;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<PrescriptionResponseDTO> getAllPrescriptions(Integer status) {
        List<Prescription> prescriptions = new ArrayList<>();
        if (status == null) prescriptions =  prescriptionDb.findAll();
        else {
            prescriptions = prescriptionDb.findAllByStatus(status);
        }
        return ListPrescriptionToListPrescriptionResponse(prescriptions);
    }

    @Override
    public PrescriptionResponseDTO getPrescription(String id) throws  Exception {
        Prescription prescription = prescriptionDb.findById(id).isPresent() ? prescriptionDb.findById(id).get() : null;
        if (prescription == null) throw new Exception("Prescription ID not found");
        List<MedicineQuantity> medicineQuantityList = medicineQuantityDb.findAllByPrescriptionId(prescription.getId());
        return prescriptionResponseDTOGenerator(medicineQuantityList, prescription);
    }

    @Override
    public PatientResponseDTO findPatient(FindPatientRequestRestDTO dto) throws Exception {
        Patient patient = patientDb.findByNik(dto.getNik());
        if (patient == null) throw new Exception("Patient tidak ditemukan");
        return patientToPatientResponseDTO(patient);
    }

    @Override
    public PrescriptionResponseDTO createPrescriptionUnregistered(CreatePrescriptionRequestRestDTO dto) throws Exception {
        Prescription prescription = new Prescription();
        prescription.setId(UUID.randomUUID().toString());
        List<MedicineQuantity> listNewMq = new ArrayList<>();
        Long totalPrice = 0L;
        for (MedicineQuantityRequestRestDTO mqDTO: dto.getListMedicineQuantity()) {
            Medicine medicine = medicineDb.findMedicineById(mqDTO.getId());
            totalPrice += medicine.getPrice() * mqDTO.getQuantity();
            MedicineQuantity mq = new MedicineQuantity();
            mq.setId(UUID.randomUUID());
            mq.setQuantity(mqDTO.getQuantity());
            mq.setFulfilledQty(0);
            mq.setPrescriptionId(prescription.getId());
            mq.setMedicineId(mqDTO.getId());
            medicineQuantityDb.save(mq);
            listNewMq.add(mq);
        }

        prescription.setTotalPrice(totalPrice);
        prescription.setStatus(0);
        prescription.setCreatedDate(new Date());
        prescription.setUpdatedDate(new Date());
        prescription.setCreatedBy(dto.getCreatedBy());
        prescription.setUpdatedBy(dto.getCreatedBy());

        Patient patient = savePatient(dto);
        prescription.setPatient(patient);

        Prescription newPrescription = savePrescription(prescription);

        PrescriptionResponseDTO responseDTO = prescriptionResponseDTOGenerator(listNewMq, newPrescription);
        return responseDTO;
    }

    @Transactional
    public Prescription savePrescription(Prescription prescription) {
        return prescriptionDb.save(prescription);
    }

    @Override
    public PrescriptionResponseDTO createPrescriptionRegistered(CreatePrescriptionRequestRestDTO dto) throws Exception {
        Patient patient = patientDb.findByNik(dto.getPatientDTO().getNik());
        if (patient == null) throw new Exception("Patient tidak ditemukan");

        Prescription prescription = new Prescription();
        prescription.setId(UUID.randomUUID().toString());

        List<MedicineQuantity> listNewMq = new ArrayList<>();
        Long totalPrice = 0L;
        for (MedicineQuantityRequestRestDTO mqDTO: dto.getListMedicineQuantity()) {
            Medicine medicine = medicineDb.findMedicineById(mqDTO.getId());
            totalPrice += medicine.getPrice() * mqDTO.getQuantity();
            MedicineQuantity mq = new MedicineQuantity();
            mq.setId(UUID.randomUUID());
            mq.setQuantity(mqDTO.getQuantity());
            mq.setFulfilledQty(0);
            mq.setPrescriptionId(prescription.getId());
            mq.setMedicineId(mqDTO.getId());
            medicineQuantityDb.save(mq);
            listNewMq.add(mq);
        }
        prescription.setTotalPrice(totalPrice);
        prescription.setStatus(0);
        prescription.setCreatedBy(dto.getCreatedBy());
        prescription.setUpdatedBy(dto.getCreatedBy());
        prescription.setPatient(patient);

        Pharmacist pharmacist = pharmacistDb.findFirstByName(dto.getCreatedBy());
        prescription.setPharmacist(pharmacist);

        Prescription newPrescription = prescriptionDb.save(prescription);
        return prescriptionResponseDTOGenerator(listNewMq, newPrescription);
    }

    @Override
    @Transactional
    public PrescriptionResponseDTO changePrescriptionStatus(String id) throws Exception {
        Prescription prescription = prescriptionDb.findById(id).isPresent() ? prescriptionDb.findById(id).get() : null;
        if (prescription == null) throw new Exception("Prescription tidak ditemukan");
        boolean changeStatusToDone = true;

        if (prescription.getStatus() == 2) {
            List<MedicineQuantity> listMq = medicineQuantityDb.findAllByPrescriptionId(id);
            return prescriptionResponseDTOGenerator(listMq, prescription);
        } else {
            List<MedicineQuantity> listMq = medicineQuantityDb.findAllByPrescriptionId(id);
            List <MedicineQuantity> listNewMq = new ArrayList<>();
            for (MedicineQuantity mq : listMq) {
                Medicine medicine = medicineDb.findMedicineById(mq.getMedicineId());
                if (mq.getQuantity() <= medicine.getStock()) {
                    mq.setFulfilledQty(mq.getQuantity());
                    listNewMq.add(medicineQuantityDb.save(mq));
                    medicine.setStock(medicine.getStock() - mq.getFulfilledQty());
                    medicineDb.save(medicine);
                } else {
                    mq.setFulfilledQty(medicine.getStock());
                    listNewMq.add(medicineQuantityDb.save(mq));
                    medicine.setStock(0);
                    medicineDb.save(medicine);
                    changeStatusToDone = false;
                }
            }

            if (changeStatusToDone) {
                List<MedicineQuantity> listMqUpdated = medicineQuantityDb.findAllByPrescriptionId(id);
                prescription.setStatus(2);
                Prescription newPrescription = prescriptionDb.save(prescription);
                return prescriptionResponseDTOGenerator(listNewMq, newPrescription);
            } else {
                List<MedicineQuantity> listMqUpdated = medicineQuantityDb.findAllByPrescriptionId(id);
                prescription.setStatus(1);
                Prescription newPrescription = prescriptionDb.save(prescription);
                return prescriptionResponseDTOGenerator(listNewMq, newPrescription);
            }
        }
    }

    @Override
    public PrescriptionResponseDTO cancelPrescription(String id) throws Exception {
        Prescription prescription = prescriptionDb.findById(id).isPresent() ? prescriptionDb.findById(id).get() : null;
        if (prescription == null) throw new Exception("Prescription tidak ditemukan");
        List<MedicineQuantity> listMq = medicineQuantityDb.findAllByPrescriptionId(id);

        if (prescription.getStatus() == 0) {
            prescription.setStatus(3);
            Prescription newPrescription = prescriptionDb.save(prescription);
            return prescriptionResponseDTOGenerator(listMq, newPrescription);
        } else if (prescription.getStatus() == 1) {
            prescription.setStatus(3);
            for (MedicineQuantity mq : listMq) {
                Medicine medicine = medicineDb.findMedicineById(mq.getMedicineId());
                medicine.setStock(medicine.getStock() + mq.getFulfilledQty());
                mq.setFulfilledQty(0);
                medicineDb.save(medicine);
                medicineQuantityDb.save(mq);
            }
            Prescription newPrescription = prescriptionDb.save(prescription);
            List<MedicineQuantity> listMqUpdated = medicineQuantityDb.findAllByPrescriptionId(id);
            return prescriptionResponseDTOGenerator(listMqUpdated, newPrescription);
        } else {
            throw new Exception("Status tidak dapat diganti");
        }
    }

    @Override
    public StatisticsResponseDTO getStatistics(String month, Integer year) throws Exception {

        Map<String, Integer> map = new HashMap<>();
        for (MedicineQuantity mq:  medicineQuantityDb.findAll()) {
            Prescription prescription = prescriptionDb.findById(mq.getPrescriptionId()).isPresent() ? prescriptionDb.findById(mq.getPrescriptionId()).get() : null;

            if (year == null && month == null) {
                Medicine medicine = medicineDb.findMedicineById(mq.getMedicineId());
                if(!map.containsKey(medicine.getName())) {
                    map.put(medicine.getName(), mq.getFulfilledQty());
                } else {
                    map.put(medicine.getName(), map.get(medicine.getName()) + mq.getFulfilledQty());
                }
            } else {
                if (!isValidMonth(month)) throw new Exception("Month bukan merupakan bulan yang valid");
                log.info(Integer.toString(prescription.getUpdatedDate().getMonth()));
                log.info(Integer.toString(prescription.getUpdatedDate().getYear()));
                log.info(Integer.toString(Month.valueOf(month).getValue()));

                if (prescription.getUpdatedDate().getMonth() + 1 == Month.valueOf(month).getValue() && prescription.getUpdatedDate().getYear() + 1900 == year) {
                    Medicine medicine = medicineDb.findMedicineById(mq.getMedicineId());
                    if(!map.containsKey(medicine.getName())) {
                        map.put(medicine.getName(), mq.getFulfilledQty());
                    } else {
                        map.put(medicine.getName(), map.get(medicine.getName()) + mq.getFulfilledQty());
                    }
                }
            }

        }
        List<String> listNamaMedicine = new ArrayList<>();
        List<Integer> listInteger = new ArrayList<>();

        for (String namaMedicine: map.keySet()) {
            if (map.get(namaMedicine) < 1) {
                continue;
            }
            if (map.keySet().size() == 10 ) {
                break;
            }
            listNamaMedicine.add(namaMedicine);
            listInteger.add(map.get(namaMedicine));
        }

        StatisticsResponseDTO responseDTO = new StatisticsResponseDTO();
        responseDTO.setLabels(listNamaMedicine);
        List<DatasetDTO> listDataset = new ArrayList<>();
        DatasetDTO datasetDTO = new DatasetDTO();
        datasetDTO.setData(listInteger);
        datasetDTO.setLabel("Most Medicine Usage Quantity");
        datasetDTO.setBorderWidth(1);
        listDataset.add(datasetDTO);
        responseDTO.setDatasets(listDataset);
        return responseDTO;
    }

    public static boolean isValidMonth(String monthName) {
        try {
            Month.valueOf(monthName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }



    private Patient savePatient(CreatePrescriptionRequestRestDTO dto) throws Exception {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setNik(dto.getPatientDTO().getNik());
        patient.setName(dto.getPatientDTO().getName());
        patient.setEmail(dto.getPatientDTO().getEmail());
        patient.setGender(dto.getPatientDTO().getGender());
        patient.setCreatedBy(dto.getCreatedBy());
        patient.setUpdatedBy(dto.getCreatedBy());
        patient.setBirthDate(dto.getPatientDTO().getBirthDate());
        return patientDb.save(patient);
    }

    private PatientResponseDTO patientToPatientResponseDTO(Patient patient) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId());
        patientResponseDTO.setNik(patient.getNik());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setGender(patient.getGender());
        patientResponseDTO.setCreatedDate(patient.getCreatedDate());
        patientResponseDTO.setUpdatedDate(patient.getUpdatedDate());
        return patientResponseDTO;
    }

    @Transactional
    public PrescriptionResponseDTO prescriptionResponseDTOGenerator(List<MedicineQuantity> listMq, Prescription pr) {
        PrescriptionResponseDTO response = new PrescriptionResponseDTO();
        Prescription prescription = prescriptionDb.findById(pr.getId()).isPresent() ?  prescriptionDb.findById(pr.getId()).get() : null ;
        response.setId(prescription.getId());
        response.setTotalPrice(prescription.getTotalPrice());
        response.setStatus(prescription.getStatus());
        response.setCreatedBy(prescription.getCreatedBy());
        response.setUpdatedBy(prescription.getUpdatedBy());
        response.setCreatedDate(prescription.getCreatedDate());
        response.setUpdatedDate(prescription.getUpdatedDate());

        Patient patient = patientDb.findByNik(pr.getPatient().getNik());
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId());
        patientResponseDTO.setNik(patient.getNik());
        patientResponseDTO.setBirthDate(patient.getBirthDate());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setGender(patient.getGender());
        patientResponseDTO.setCreatedDate(patient.getCreatedDate());
        patientResponseDTO.setUpdatedDate(patient.getUpdatedDate());
        response.setPatient(patientResponseDTO);

        if (pr.getPharmacist() != null) {
            Pharmacist pharmacist = pharmacistDb.findById(pr.getPharmacist().getId()).get();
            PharmacistResponseDTO pharmacistResponseDTO = new PharmacistResponseDTO();
            pharmacistResponseDTO.setId(pharmacist.getId());
            pharmacistResponseDTO.setName(pharmacist.getName());
            pharmacistResponseDTO.setEmail(pharmacist.getEmail());
            pharmacistResponseDTO.setGender(pharmacist.getGender());
            pharmacistResponseDTO.setCreatedDate(pharmacist.getCreatedDate());
            pharmacistResponseDTO.setUpdatedDate(pharmacist.getUpdatedDate());
            response.setPharmacist(pharmacistResponseDTO);
        }

        List<MedicineQuantityResponseDTO> mqListDTO = new ArrayList<>();
        for (MedicineQuantity mq: listMq) {

            mqListDTO.add(mqToMqResponseDTO(mq));
        }
        response.setListMedicineQuantity(mqListDTO);
        return response;
    }

    private MedicineQuantityResponseDTO mqToMqResponseDTO(MedicineQuantity mq) {
        MedicineQuantityResponseDTO mqResponseDTO = new MedicineQuantityResponseDTO();
        mqResponseDTO.setId(mq.getId());
        mqResponseDTO.setQuantity(mq.getQuantity());
        mqResponseDTO.setFulfilledQty(mq.getFulfilledQty());
        Medicine medicine = medicineDb.findMedicineById(mq.getMedicineId());

        MedicineResponseDTO medicineResponseDTO = new MedicineResponseDTO();
        medicineResponseDTO.setId(medicine.getId());
        medicineResponseDTO.setStock(medicine.getStock());
        medicineResponseDTO.setName(medicine.getName());
        medicineResponseDTO.setDescription(medicine.getDescription());
        medicineResponseDTO.setCreatedDate(medicine.getCreatedDate());
        medicineResponseDTO.setUpdatedDate(medicine.getUpdatedDate());
        medicineResponseDTO.setPrice(medicine.getPrice());
        mqResponseDTO.setMedicine(medicineResponseDTO);
        return mqResponseDTO;
    }

    private List<PrescriptionResponseDTO> ListPrescriptionToListPrescriptionResponse(List<Prescription> list) {
        List<PrescriptionResponseDTO> prescriptionResponseDTOList = new ArrayList<>();
        for (Prescription prescription: list) {
            List<MedicineQuantity> listMq = medicineQuantityDb.findAllByPrescriptionId(prescription.getId());
            prescriptionResponseDTOList.add(prescriptionResponseDTOGenerator(listMq, prescription));
        }
        return prescriptionResponseDTOList;
    }
}
