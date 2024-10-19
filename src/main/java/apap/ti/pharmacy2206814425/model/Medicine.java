package apap.ti.pharmacy2206814425.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name = "medicine")
public class Medicine {
    @Id
    private String id;

    @NotEmpty
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    @Min(value = 1000, message = "Price minimal dari Medicine harus 1000")
    private Long price;

    @NotNull
    @Column(name = "stock", nullable = false)
//    @Positive(message = "Stock minimal dari Medicine harus bilangan positif")
    private Integer stock;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false, nullable = false)
    private Date createdDate;

    @NotEmpty
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", nullable = false)
    private Date updatedDate;

    @NotEmpty
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(name = "deleted_date")
    private Date deletedDate;

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
