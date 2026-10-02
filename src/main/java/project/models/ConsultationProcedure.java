package project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "consultation_procedures",
        uniqueConstraints = @UniqueConstraint(name = "uk_consultation_procedure",
                columnNames = {"consultation_id", "procedure_id"}))
public class ConsultationProcedure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "procedure_id", nullable = false)
    private MedicalProcedure procedure;

    @NotNull
    @Column(name = "applied_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal appliedCost;

    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;

    public ConsultationProcedure() {
    }

    public ConsultationProcedure(Consultation consultation, MedicalProcedure procedure) {
        this.consultation = consultation;
        this.procedure = procedure;
        this.appliedCost = procedure.getCost();
    }

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
        if (appliedCost == null && procedure != null) {
            appliedCost = procedure.getCost();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public MedicalProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(MedicalProcedure procedure) {
        this.procedure = procedure;
    }

    public BigDecimal getAppliedCost() {
        return appliedCost;
    }

    public void setAppliedCost(BigDecimal appliedCost) {
        this.appliedCost = appliedCost;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsultationProcedure)) {
            return false;
        }
        ConsultationProcedure that = (ConsultationProcedure) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
