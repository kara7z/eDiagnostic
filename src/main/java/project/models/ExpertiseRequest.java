package project.models;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import project.enums.Priority;
import project.enums.ExpertiseRequestStatus;

@Entity
@Table(name = "expertise_requests")
public class ExpertiseRequest implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consultation_id", nullable = false)
  private Consultation consultation;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "general_practitioner_id", nullable = false)
  private User generalPractitioner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "specialist_id")
  private User specialist;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "requested_specialty_id", nullable = false)
  private Specialty requestedSpecialty;

  @NotBlank
  @Size(max = 2000)
  @Column(nullable = false, length = 2000)
  private String question;

  @Size(max = 2000)
  @Column(name = "analysis_data", length = 2000)
  private String analysisData;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Priority priority = Priority.NORMAL;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ExpertiseRequestStatus status = ExpertiseRequestStatus.PENDING;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "time_slot_id")
  private TimeSlot timeSlot;

  @Size(max = 2000)
  @Column(name = "medical_opinion", length = 2000)
  private String medicalOpinion;

  @Size(max = 2000)
  @Column(length = 2000)
  private String recommendations;

  @Column(name = "request_date", updatable = false)
  private LocalDateTime requestDate;

  @Column(name = "response_date")
  private LocalDateTime responseDate;

  @Column(name = "expertise_cost", precision = 10, scale = 2)
  private BigDecimal expertiseCost;

  public ExpertiseRequest() {
  }

  @PrePersist
  protected void onCreate() {
    requestDate = LocalDateTime.now();
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

  public User getGeneralPractitioner() {
    return generalPractitioner;
  }

  public void setGeneralPractitioner(User generalPractitioner) {
    this.generalPractitioner = generalPractitioner;
  }

  public User getSpecialist() {
    return specialist;
  }

  public void setSpecialist(User specialist) {
    this.specialist = specialist;
  }

  public Specialty getRequestedSpecialty() {
    return requestedSpecialty;
  }

  public void setRequestedSpecialty(Specialty requestedSpecialty) {
    this.requestedSpecialty = requestedSpecialty;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnalysisData() {
    return analysisData;
  }

  public void setAnalysisData(String analysisData) {
    this.analysisData = analysisData;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public ExpertiseRequestStatus getStatus() {
    return status;
  }

  public void setStatus(ExpertiseRequestStatus status) {
    this.status = status;
  }

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }

  public void setTimeSlot(TimeSlot timeSlot) {
    this.timeSlot = timeSlot;
  }

  public String getMedicalOpinion() {
    return medicalOpinion;
  }

  public void setMedicalOpinion(String medicalOpinion) {
    this.medicalOpinion = medicalOpinion;
  }

  public String getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(String recommendations) {
    this.recommendations = recommendations;
  }

  public LocalDateTime getRequestDate() {
    return requestDate;
  }

  public LocalDateTime getResponseDate() {
    return responseDate;
  }

  public void setResponseDate(LocalDateTime responseDate) {
    this.responseDate = responseDate;
  }

  public BigDecimal getExpertiseCost() {
    return expertiseCost;
  }

  public void setExpertiseCost(BigDecimal expertiseCost) {
    this.expertiseCost = expertiseCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExpertiseRequest)) {
      return false;
    }
    ExpertiseRequest that = (ExpertiseRequest) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
