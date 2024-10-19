package apap.ti.pharmacy2206814425.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    private UUID id;

    @NotNull
    @Column(name = "nik", nullable = false, unique = true)
    private String nik;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "gender", nullable = false)
    @Min(value = 0, message = "Gender harus 0 (perempuan) atau 1 (laki-laki)")
    @Max(value = 1, message = "Gender harus 0 (perempuan) atau 1 (laki-laki)")
    private int gender;

    @NotNull
    @Column(name = "birth_date", nullable = false, columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false, nullable = false)
    private Date createdDate;

    @NotNull
    private String createdBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", nullable = false)
    private Date updatedDate;

    @NotNull
    private String updatedBy;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;
}
