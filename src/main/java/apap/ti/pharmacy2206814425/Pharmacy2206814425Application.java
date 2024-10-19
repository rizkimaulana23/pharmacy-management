package apap.ti.pharmacy2206814425;

import apap.ti.pharmacy2206814425.dto.CreateMedicineDTO;
import apap.ti.pharmacy2206814425.model.Pharmacist;
import apap.ti.pharmacy2206814425.service.MedicineService;
import apap.ti.pharmacy2206814425.service.PharmacistService;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
public class Pharmacy2206814425Application {

	public static void main(String[] args) {
		SpringApplication.run(Pharmacy2206814425Application.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(PharmacistService pharmacistService,
						  MedicineService medicineService) {
		return args -> {
			var faker = new Faker(new Locale("in-ID"));

			var pharmacist = new Pharmacist();
			pharmacist.setId(UUID.fromString("52c1abd3-2421-4678-86b8-bd10ee8f8f40"));
			pharmacist.setName("Pharmacist 1");
			pharmacist.setEmail(faker.internet().emailAddress());
			pharmacist.setGender(faker.number().numberBetween(0, 1));
			pharmacist.setCreatedBy(faker.name().username());
			pharmacist.setUpdatedBy(faker.name().username());
			pharmacistService.addPharmacist(pharmacist);

			var medicineDTO = new CreateMedicineDTO();
			medicineDTO.setName("Bodrex 1");
			medicineDTO.setPrice(10000L);
			medicineDTO.setStock(1000);
			medicineDTO.setDescription("Bodrex adalah obat yang populer di Indonesia dan dikenal luas sebagai pereda sakit kepala yang efektif.");
			medicineDTO.setCreatedBy(pharmacist.getName());
			medicineService.addMedicine(medicineDTO);

			var medicineDTO2 = new CreateMedicineDTO();
			medicineDTO2.setName("Paracetamol");
			medicineDTO2.setPrice(10000L);
			medicineDTO2.setStock(1000);
			medicineDTO2.setDescription("Ya paracetamol bro");
			medicineDTO2.setCreatedBy(pharmacist.getName());
			medicineService.addMedicine(medicineDTO2);
		};
	}
}
