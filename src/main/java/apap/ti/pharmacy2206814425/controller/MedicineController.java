package apap.ti.pharmacy2206814425.controller;

import apap.ti.pharmacy2206814425.dto.CreateMedicineDTO;
import apap.ti.pharmacy2206814425.dto.RestockDTO;
import apap.ti.pharmacy2206814425.dto.RestockMultipleDTO;
import apap.ti.pharmacy2206814425.dto.UpdateMedicineDTO;
import apap.ti.pharmacy2206814425.model.Medicine;
import apap.ti.pharmacy2206814425.service.MedicineService;
import apap.ti.pharmacy2206814425.service.PharmacistService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@Slf4j
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private PharmacistService pharmacistService;

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        model.addAttribute("medicineDTO", new CreateMedicineDTO());
        return "form-add-medicine";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("medicineDTO") CreateMedicineDTO medicineDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("medicineDTO", medicineDTO);
            model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
            return "form-add-medicine";
        }
        Medicine medicine = medicineService.addMedicine(medicineDTO);
        model.addAttribute("responseMessage", String.format("Medicine dengan ID %s berhasil ditambahkan", medicine.getId()));
        return "success-medicine";
    }

    @GetMapping("")
    public String viewall(Model model) {
        model.addAttribute("listMedicineDTO", medicineService.getAllMedicine());
        return "viewall-medicine";
    }

    @GetMapping("/{id}")
    public String viewDetail(@PathVariable("id") String id, Model model) {
        Medicine medicine = medicineService.getMedicine(id);
        if (medicine == null) {
            model.addAttribute("responseMessage", String.format("Tidak ada medicine dengan ID %s", id));
            return "success-response";
        } else {
            model.addAttribute("medicine", medicine);
            return "view-medicine";
        }
    }

    @GetMapping("/{id}/update")
    public String getUpdate(@PathVariable("id") String id, Model model) {
        Medicine medicine = medicineService.getMedicine(id);
        if (medicine == null) {
            model.addAttribute("responseMessage", String.format("Tidak ada medicine dengan ID %s", id));
            return "success-medicine";
        }
        UpdateMedicineDTO updateMedicineDTO = new UpdateMedicineDTO();
        updateMedicineDTO.setId(medicine.getId());
        updateMedicineDTO.setName(medicine.getName());
        updateMedicineDTO.setDescription(medicine.getDescription());
        updateMedicineDTO.setStock(medicine.getStock());
        updateMedicineDTO.setPrice(medicine.getPrice());
        updateMedicineDTO.setCreatedBy(medicine.getCreatedBy());

        model.addAttribute("medicineDTO", updateMedicineDTO);
        model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
        return "form-update-medicine";
    }

    @PostMapping("/update")
    public String postUpdate(@Valid @ModelAttribute("medicineDTO") UpdateMedicineDTO medicineDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("medicineDTO", medicineDTO);
            model.addAttribute("listPharmacist", pharmacistService.getAllPharmacists());
            return "form-update-medicine";
        }
        log.info("postUpdate " + medicineDTO.getId());
        Medicine medicine = medicineService.updateMedicine(medicineDTO);
        model.addAttribute("responseMessage", String.format("Berhasil memperbarui Medicine dengan ID %s", medicine.getId()));
        return "success-medicine";
    }

    @GetMapping("/restock")
    public String restockMultiple(Model model) {
        RestockMultipleDTO restockMultipleDTO = new RestockMultipleDTO();

        model.addAttribute("restockMultipleDTO", restockMultipleDTO);
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "restock-multiple-medicine";
    }

    @PostMapping("/restock")
    public String restockMultiple(@Valid @ModelAttribute("restockMultipleDTO") RestockMultipleDTO restockDTO,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restockMultipleDTO", restockDTO);
            model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info("fieldError {} {}", fieldError.getField(), fieldError.getDefaultMessage());
            }
            return "restock-multiple-medicine";
        }
        medicineService.restockMedicine(restockDTO);
        model.addAttribute("responseMessage", "Restock berhasil dilakukan");
        return "success-medicine";
    }

    @PostMapping(value = "/restock", params = {"addRow"})
    public String addRowForRestockMultiple(@ModelAttribute RestockMultipleDTO restockMultipleDTO, Model model) {
        if (restockMultipleDTO.getListRestockDTO() == null || restockMultipleDTO.getListRestockDTO().isEmpty()) {
            restockMultipleDTO.setListRestockDTO(new ArrayList<>());
        }
        RestockDTO restockDTO = new RestockDTO();
        restockMultipleDTO.getListRestockDTO().add(restockDTO);

        model.addAttribute("restockMultipleDTO", restockMultipleDTO);
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "restock-multiple-medicine";
    }

    @PostMapping(value = "/restock", params = {"deleteRow"})
    public String deleteRowForRestockMultiple(@ModelAttribute RestockMultipleDTO restockMultipleDTO, @RequestParam("deleteRow") int row, Model model) {
        restockMultipleDTO.getListRestockDTO().remove(row);
        model.addAttribute("restockMultipleDTO", restockMultipleDTO);
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "restock-multiple-medicine";
    }

    @PostMapping(value = "/restock", params = {"cancelAll"})
    public String deleteAllRowForRestockMultiple(@ModelAttribute RestockMultipleDTO restockMultipleDTO, Model model) {
        if (restockMultipleDTO.getListRestockDTO() != null) {
            restockMultipleDTO.getListRestockDTO().clear();
        }
        model.addAttribute("restockMultipleDTO", restockMultipleDTO);
        model.addAttribute("listMedicineExisting", medicineService.getAllMedicine());
        return "restock-multiple-medicine";
    }

    @GetMapping("/{id}/update-stock")
    public String restockSingle(@PathVariable("id") String id, Model model) {
        RestockDTO restockDTO = new RestockDTO();
        restockDTO.setMedicineId(id);
        model.addAttribute("restockDTO", restockDTO);
        model.addAttribute("medicine", medicineService.getMedicine(id));
        return "restock-single-medicine";
    }

    @PostMapping("/{id}/update-stock")
    public String restockSingle(@Valid @ModelAttribute("restockDTO") RestockDTO restockDTO, BindingResult bindingResult, Model model, @PathVariable("id") String id) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restockDTO", restockDTO);
            model.addAttribute("medicine", medicineService.getMedicine(id));
            return "restock-single-medicine";
        }
        medicineService.restockSingleMedicine(restockDTO);
        model.addAttribute("responseMessage", String.format("Restock untuk Medicine dengan ID %s berhasil dilakukan.", id));
        return "success-medicine";
    }

    @GetMapping("/{id}/delete")
    public String deleteMedicine(@PathVariable String id, Model model) {
        if (medicineService.deleteMedicine(id)) {
            model.addAttribute("responseMessage", String.format("Medicine dengan ID %s berhasil dihapus", id));
            return "success-medicine";
        } else {
            model.addAttribute("responseMessage", String.format("Medicine dengan ID %s tidak dapat dihapus", id));
            return "success-medicine";
        }
    }
}
