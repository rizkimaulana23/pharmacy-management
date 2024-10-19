package apap.ti.pharmacy2206814425.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pharmacist")
public class Pharmacist {
    @Id
    private UUID id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "gender", nullable = false)
    @Min(value = 0, message = "Gender harus 0 (perempuan) atau 1 (laki-laki)")
    @Max(value = 1, message = "Gender harus 0 (perempuan) atau 1 (laki-laki)")
    private int gender;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
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

    @OneToMany(mappedBy = "pharmacist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;
}
