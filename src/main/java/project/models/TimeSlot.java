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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import project.enums.SlotStatus;

@Entity
@Table(name = "time_slots", uniqueConstraints = @UniqueConstraint(name = "uk_slot_specialist_time", columnNames = {
    "specialist_id", "slot_date", "start_time" }))
public class TimeSlot implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "specialist_id", nullable = false)
  private User specialist;

  @NotNull
  @Column(name = "slot_date", nullable = false)
  private LocalDate date;

  @NotNull
  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @NotNull
  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SlotStatus status = SlotStatus.AVAILABLE;

  public TimeSlot() {
  }

  public TimeSlot(User specialist, LocalDate date, LocalTime startTime, LocalTime endTime) {
    this.specialist = specialist;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  @PrePersist
  protected void onCreate() {
    if (status == null) {
      status = SlotStatus.AVAILABLE;
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getSpecialist() {
    return specialist;
  }

  public void setSpecialist(User specialist) {
    this.specialist = specialist;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public SlotStatus getStatus() {
    return status;
  }

  public void setStatus(SlotStatus status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TimeSlot)) {
      return false;
    }
    TimeSlot that = (TimeSlot) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
