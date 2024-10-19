package apap.ti.pharmacy2206814425.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "medicine_quantity")
public class MedicineQuantity {
    @Id
    private UUID id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "fulfilled_qty", nullable = false)
    private Integer fulfilledQty;

    @NotNull
    @Column(name = "medicine_id", nullable = false)
    private String medicineId;

    @NotNull
    @Column(name = "prescription_id", nullable = false)
    private String prescriptionId;
}
