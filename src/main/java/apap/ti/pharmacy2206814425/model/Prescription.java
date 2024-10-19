package apap.ti.pharmacy2206814425.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    private String id;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @NotNull
    @Column(name = "status", nullable = false)
    private int status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false, nullable = false)
    private Date createdDate;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", nullable = false)
    private Date updatedDate;

    @NotNull
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pharmacist", referencedColumnName = "id")
    private Pharmacist pharmacist;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_patient", referencedColumnName = "id")
    private Patient patient;

    public static String formatRupiah(long amount) {
        // Create a DecimalFormat instance
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        // Define the currency format pattern
        DecimalFormat rupiahFormat = new DecimalFormat("Rp #,##0.00", symbols);

        // Format the amount and return it as a string
        return rupiahFormat.format(amount);
    }

    public static String formatDate(Date date) {
        // Define the date pattern for the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy | HH:mm:ss");

        // Format the date and return it as a string
        return formatter.format(date);
    }
}
