package apap.ti.pharmacy2206814425.controller;

import apap.ti.pharmacy2206814425.dto.*;
import apap.ti.pharmacy2206814425.model.Medicine;
import apap.ti.pharmacy2206814425.model.MedicineQuantity;
import apap.ti.pharmacy2206814425.model.Prescription;
import apap.ti.pharmacy2206814425.repository.PharmacistDb;
import apap.ti.pharmacy2206814425.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final MedicineQuantityService medicineQuantityService;
    private final MedicineService medicineService;
    private final PatientService patientService;
    private final PharmacistService pharmacistService;

    public PrescriptionController(
            PrescriptionService prescriptionService,
            MedicineQuantityService medicineQuantityService,
            MedicineService medicineService,
            PatientService patientService,
            PharmacistService pharmacistService) {
        this.prescriptionService = prescriptionService;
        this.medicineQuantityService = medicineQuantityService;
        this.medicineService = medicineService;
        this.patientService = patientService;
        this.pharmacistService = pharmacistService;
    }

    @Autowired
    private PharmacistDb pharmacistDb;

    @GetMapping("/add/find-patient")
    public String findPatient(Model model) {
        FindPatientDTO dto = new FindPatientDTO();
        model.addAttribute("dto", dto);
        return "form-search-patient";
    }

    @PostMapping("/add/find-patient")
    public String findPatient(FindPatientDTO dto, Model model) {
        if (patientService.findPatientByNik(dto.getNik()) == null) return "patient-not-found";
        else {
            model.addAttribute("patient", patientService.findPatientByNik(dto.getNik()));
            return "patient-found";
        }
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        model.addAttribute("prescription", prescriptionService.getPrescription(id));
        model.addAttribute("listMedicineQuantity", medicineQuantityService.getMedicineQuantityForPrescription(id));
        List<Medicine> listMedicine = new ArrayList<>();
        for (MedicineQuantity mq : medicineQuantityService.getMedicineQuantityForPrescription(id)) {
            Medicine medicine = medicineService.getMedicine(mq.getMedicineId());
            listMedicine.add(medicine);
        }
        model.addAttribute("listMedicine", listMedicine);
        return "view-prescription";
    }

    @GetMapping("")
    public String viewall(Model model, @RequestParam(value = "status", required = false) String status) {
        if (status != null) {
            if (!status.equals("All")) {
                model.addAttribute("listPrescription", prescriptionService.getAllPrescriptionWithStatus(Integer.parseInt(status)));
                for (Prescription p : prescriptionService.getAllPrescriptionWithStatus(Integer.parseInt(status))) {
                    log.info(p.getId());
                }
                return "viewall-prescription";
            }
        }
        model.addAttribute("listPrescription", prescriptionService.getAllPrescription());
        return "viewall-prescription";
    }

    @GetMapping("/add")
    public String createPrescriptionUnregistered(Model model) {
        model.addAttribute("dto", new CreatePrescriptionDTO());
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-prescription-unregistered";
    }

    @PostMapping(value = "/add", params = {"addRow"})
    public String addRowPrescriptionUnregistered(@ModelAttribute CreatePrescriptionDTO dto, Model model) {
        if ( dto.getListMedicineQuantity() == null || dto.getListMedicineQuantity().isEmpty() ) {
            dto.setListMedicineQuantity(new ArrayList<>());
        }
        CreateMedicineQuantityDTO mqDTO = new CreateMedicineQuantityDTO();
        dto.getListMedicineQuantity().add(mqDTO);
        log.info(String.format("addRow %s", dto.getListMedicineQuantity().size()));

        model.addAttribute("dto", dto);
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-prescription-unregistered";
    }

    @PostMapping(value = "/add", params = {"deleteRow"})
    public String deleteRowPrescriptionUnregistered(@ModelAttribute CreatePrescriptionDTO dto, @RequestParam("deleteRow") int row, Model model) {
        dto.getListMedicineQuantity().remove(row);
        model.addAttribute("dto", dto);
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-prescription-unregistered";
    }

    @PostMapping("add")
    public String createPrescriptionUnregistered(@Valid @ModelAttribute("dto") CreatePrescriptionDTO dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info("TAII");
                log.error(fieldError.toString());
            }
            model.addAttribute("dto", dto);
            model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
            model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
            return "form-prescription-unregistered";
        }
        Prescription prescription = prescriptionService.createPrescription(dto);
        model.addAttribute("responseMessage", String.format("Prescription dengan ID %s berhasil ditambahkan", prescription.getId()));
        return "success-prescription";
    }

    @GetMapping("/add/{patientId}")
    public String createPrescriptionRegistered(@PathVariable("patientId") UUID id, Model model) {
        model.addAttribute("dto", new CreatePrescriptionRegisteredDTO());
        model.addAttribute("patient", patientService.findPatient(id));
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-prescription-registered";
    }

    @PostMapping("/add/{patientId}")
    public String createPrescriptionRegistered(@PathVariable("patientId") UUID id, @Valid @ModelAttribute("dto") CreatePrescriptionRegisteredDTO dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("patient", patientService.findPatient(id));
            model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
            model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
            return "form-prescription-registered";
        }
        prescriptionService.createPrescription(id, dto);
        model.addAttribute("responseMessage", String.format("Prescription dengan ID %s berhasil dibuat", id));
        return "success-prescription";
    }

    @PostMapping(value = "/add/{patientId}", params = {"addRow"})
    public String addRowPrescriptionRegistered(@ModelAttribute CreatePrescriptionDTO dto, Model model, @PathVariable("patientId") UUID id) {
        if ( dto.getListMedicineQuantity() == null || dto.getListMedicineQuantity().isEmpty() ) {
            dto.setListMedicineQuantity(new ArrayList<>());
        }
        CreateMedicineQuantityDTO mqDTO = new CreateMedicineQuantityDTO();
        dto.getListMedicineQuantity().add(mqDTO);
        log.info(String.format("addRow %s", dto.getListMedicineQuantity().size()));

        model.addAttribute("dto", dto);
        model.addAttribute("patient", patientService.findPatient(id));
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-prescription-registered";
    }

    @PostMapping(value = "/add/{patientId}", params = {"deleteRow"})
    public String deleteRowPrescriptionRegistered(@ModelAttribute("dto") CreatePrescriptionDTO dto, @PathVariable("patientId") UUID id, Model model, @RequestParam("deleteRow") int row) {
        dto.getListMedicineQuantity().remove(row);
        model.addAttribute("dto", dto);
        model.addAttribute("patient", patientService.findPatient(id));
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-prescription-registered";
    }

    @GetMapping("/{id}/update")
    public String updatePrescription(@PathVariable("id") String id, Model model) {
        Prescription prescription = prescriptionService.getPrescription(id);
        if (prescription == null) {
            model.addAttribute("responseMessage", "Prescription tidak ditemukan");
            return "success-prescription";
        }
        if (prescription.getStatus() != 0) {
            model.addAttribute("responseMessage", "Status Prescription bukahlah Created. Tidak dapat di-update");
            return "success-prescription";
        }
        UpdatePrescriptionDTO dto = new UpdatePrescriptionDTO();
        List<UpdateMedicineQuantityDTO> listMqUpdate = new ArrayList<>();
        for (MedicineQuantity mq: medicineQuantityService.getMedicineQuantityForPrescription(id)) {
            UpdateMedicineQuantityDTO mqDTO = new UpdateMedicineQuantityDTO();
            mqDTO.setQty(mq.getQuantity());
            mqDTO.setMedicineId(mq.getMedicineId());
            listMqUpdate.add(mqDTO);
        }
        dto.setPrescriptionId(id);
        dto.setListMedicineQuantity(listMqUpdate);
        dto.setPatientName(prescription.getPatient().getName());
        model.addAttribute("dto", dto);
        model.addAttribute("idPrescription", id);
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-update-prescription";
    }

    @PostMapping("/{id}/update")
    public String updatePrescription(@ModelAttribute UpdatePrescriptionDTO dto, Model model, @PathVariable("id") String id) {
        Prescription prescription = prescriptionService.updatePrescription(dto, id);
        model.addAttribute("responseMessage", String.format("Prescription dengan ID %s berhasil di-update", id));
        return "success-prescription";
    }

    @PostMapping(value = "/{id}/update", params = {"addRow"})
    public String addRowUpdatePrescription(@ModelAttribute UpdatePrescriptionDTO dto, @PathVariable("id") String id, Model model) {

        UpdateMedicineQuantityDTO mqDTO = new UpdateMedicineQuantityDTO();

        dto.getListMedicineQuantity().add(mqDTO);
        model.addAttribute("dto", dto);
        model.addAttribute("idPrescription", id);
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-update-prescription";
    }

    @PostMapping(value = "/{id}/value", params = {"deleteRow"})
    public String deleteRowUpdatePrescription(@ModelAttribute UpdatePrescriptionDTO dto, @PathVariable("id") String id, Model model, @RequestParam("deleteRow") int row) {
        dto.getListMedicineQuantity().remove(row);
        model.addAttribute("dto", dto);
        model.addAttribute("idPrescription", id);
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "form-update-prescription";
    }

    @GetMapping("/{id}/mark-done")
    public String changeStatus(@PathVariable("id") String id, Model model) {
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        Prescription prescription = prescriptionService.getPrescription(id);
        if (prescription == null) {
            model.addAttribute("responseMessage", "Prescription tidak ditemukan");
            return "success-prescription";
        }
        ProcessPrescriptionDTO dto = new ProcessPrescriptionDTO();
        model.addAttribute("idPrescription", id);
        model.addAttribute("dto", dto);
        model.addAttribute("prescription", prescription);
        List<MedicineQuantity> listMq = medicineQuantityService.getMedicineQuantityForPrescription(id);
        dto.setListMedicine(new ArrayList<>());
        for (MedicineQuantity mq: listMq) {
            dto.getListMedicine().add(medicineService.getMedicine(mq.getMedicineId()));
        }
        model.addAttribute("listMedicineQuantity", listMq);
        return "form-change-status";
    }

    @PostMapping("/{id}/mark-done")
    public String changeStatus(@Valid @ModelAttribute("dto") ProcessPrescriptionDTO dto, BindingResult bindingResult, Model model, @PathVariable("id") String id) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
            Prescription prescription = prescriptionService.getPrescription(id);
            model.addAttribute("idPrescription", id);
            model.addAttribute("dto", dto);
            model.addAttribute("prescription", prescription);
            List<MedicineQuantity> listMq = medicineQuantityService.getMedicineQuantityForPrescription(id);
            dto.setListMedicine(new ArrayList<>());
            for (MedicineQuantity mq: listMq) {
                dto.getListMedicine().add(medicineService.getMedicine(mq.getMedicineId()));
            }
            model.addAttribute("listMedicineQuantity", listMq);
            return "form-change-status";
        }

        prescriptionService.processPrescription(dto, id);
        model.addAttribute("responseMessage", String.format("Prescription dengan ID %s berhasil diupdate.", id));
        return "success-prescription";
    }

    @GetMapping("/{id}/delete")
    public String cancel(@PathVariable("id") String id, Model model) {
        if (prescriptionService.deletePrescription(id)) {
            model.addAttribute("responseMessage", String.format("Prescription dengan ID %s berhasil dihapus.", id));
            return "success-prescription";
        }
        model.addAttribute("responseMessage", String.format("Prescription dengan ID %s tidak dapat dihapus.", id));
        return "success-prescription";
    }

    @GetMapping("/statistic")
    public String statistic(Model model) {
        return "show-statistics";
    }
}
