package project.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import project.enums.ConsultationStatus;

@Entity
@Table(name = "consultations")
public class Consultation implements Serializable {

  public static final BigDecimal CONSULTATION_COST = new BigDecimal("150.00");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "patient_id", nullable = false)
  private Patient patient;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "general_practitioner_id", nullable = false)
  private User generalPractitioner;

  @Size(max = 2000)
  @Column(length = 2000)
  private String reason;

  @Size(max = 2000)
  @Column(length = 2000)
  private String notes;

  @Size(max = 2000)
  @Column(name = "clinical_examination", length = 2000)
  private String clinicalExamination;

  @Size(max = 2000)
  @Column(name = "symptom_analysis", length = 2000)
  private String symptomAnalysis;

  @Size(max = 2000)
  @Column(length = 2000)
  private String diagnosis;

  @Size(max = 2000)
  @Column(length = 2000)
  private String treatment;

  @Column(name = "consultation_cost", nullable = false, precision = 10, scale = 2)
  private BigDecimal consultationCost = CONSULTATION_COST;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 35)
  private ConsultationStatus status = ConsultationStatus.IN_PROGRESS;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "closed_at")
  private LocalDateTime closedAt;

  @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ExpertiseRequest> expertiseRequests = new ArrayList<>();

  @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ConsultationProcedure> procedures = new ArrayList<>();

  public Consultation() {
  }

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    if (consultationCost == null) {
      consultationCost = CONSULTATION_COST;
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public User getGeneralPractitioner() {
    return generalPractitioner;
  }

  public void setGeneralPractitioner(User generalPractitioner) {
    this.generalPractitioner = generalPractitioner;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getClinicalExamination() {
    return clinicalExamination;
  }

  public void setClinicalExamination(String clinicalExamination) {
    this.clinicalExamination = clinicalExamination;
  }

  public String getSymptomAnalysis() {
    return symptomAnalysis;
  }

  public void setSymptomAnalysis(String symptomAnalysis) {
    this.symptomAnalysis = symptomAnalysis;
  }

  public String getDiagnosis() {
    return diagnosis;
  }

  public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
  }

  public String getTreatment() {
    return treatment;
  }

  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }

  public BigDecimal getConsultationCost() {
    return consultationCost;
  }

  public void setConsultationCost(BigDecimal consultationCost) {
    this.consultationCost = consultationCost;
  }

  public ConsultationStatus getStatus() {
    return status;
  }

  public void setStatus(ConsultationStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getClosedAt() {
    return closedAt;
  }

  public void setClosedAt(LocalDateTime closedAt) {
    this.closedAt = closedAt;
  }

  public List<ExpertiseRequest> getExpertiseRequests() {
    return expertiseRequests;
  }

  public List<ConsultationProcedure> getProcedures() {
    return procedures;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Consultation)) {
      return false;
    }
    Consultation that = (Consultation) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
