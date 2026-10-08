package project.controllers;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import project.dao.ConsultationDao;
import project.dao.ExpertiseRequestDao;
import project.dao.SpecialtyDao;
import project.dao.TimeSlotDao;
import project.dao.UserDao;
import project.enums.ConsultationStatus;
import project.enums.Priority;
import project.enums.SlotStatus;
import project.models.Consultation;
import project.models.ExpertiseRequest;
import project.models.Specialty;
import project.models.TimeSlot;
import project.models.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet({ "/general-practitioner/expertise/new", "/general-practitioner/expertise" })
public class ExpertiseRequestController extends HttpServlet {

  @Inject
  private ConsultationDao consultationDao;

  @Inject
  private ExpertiseRequestDao expertiseRequestDao;

  @Inject
  private SpecialtyDao specialtyDao;

  @Inject
  private TimeSlotDao timeSlotDao;

  @Inject
  private UserDao userDao;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Long consultationId = parseLong(request.getParameter("consultationId"));
    Consultation c = consultationId == null ? null : consultationDao.findWithPatient(consultationId);
    if (c == null) {
      response.sendRedirect(request.getContextPath() + "/general-practitioner/queue?error=notfound");
      return;
    }

    Long specialtyId = parseLong(request.getParameter("specialtyId"));
    Long specialistId = parseLong(request.getParameter("specialistId"));

    List<Specialty> specialties = specialtyDao.findAll();
    Specialty selected = specialtyId == null ? null : specialtyDao.find(specialtyId);

    // Stream: filter specialists by specialty, sort by tariff (consultation fee)
    List<User> specialists = userDao.findSpecialists().stream()
        .filter(u -> selected == null || (u.getSpecialty() != null && u.getSpecialty().getId().equals(selected.getId())))
        .sorted(Comparator.comparing(u -> u.getConsultationFee() == null ? BigDecimal.ZERO : u.getConsultationFee()))
        .collect(Collectors.toList());

    User selectedSpecialist = specialistId == null ? null : userDao.find(specialistId);

    // Stream: only future, available slots, sorted by date/time
    LocalDateTime now = LocalDateTime.now();
    List<TimeSlot> slots = timeSlotDao.findAvailableOrdered().stream()
        .filter(t -> t.getStatus() == SlotStatus.AVAILABLE)
        .filter(t -> selectedSpecialist == null || t.getSpecialist().getId().equals(selectedSpecialist.getId()))
        .filter(t -> LocalDateTime.of(t.getDate(), t.getStartTime()).isAfter(now))
        .sorted(Comparator.comparing(TimeSlot::getDate).thenComparing(TimeSlot::getStartTime))
        .collect(Collectors.toList());

    request.setAttribute("consultation", c);
    request.setAttribute("specialties", specialties);
    request.setAttribute("selectedSpecialty", selected);
    request.setAttribute("specialists", specialists);
    request.setAttribute("selectedSpecialist", selectedSpecialist);
    request.setAttribute("slots", slots);
    request.getRequestDispatcher("/WEB-INF/views/GP/expertiseForm.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Long consultationId = parseLong(request.getParameter("consultationId"));
    Consultation c = consultationId == null ? null : consultationDao.findWithPatient(consultationId);
    if (c == null) {
      response.sendRedirect(request.getContextPath() + "/general-practitioner/queue?error=notfound");
      return;
    }
    String question = trim(request.getParameter("question"));
    String analysisData = trim(request.getParameter("analysisData"));
    String priorityParam = trim(request.getParameter("priority"));
    Long specialtyId = parseLong(request.getParameter("specialtyId"));
    Long specialistId = parseLong(request.getParameter("specialistId"));
    Long slotId = parseLong(request.getParameter("timeSlotId"));
    TimeSlot slot = slotId == null ? null : timeSlotDao.findFull(slotId);
    // derive specialist/specialty from the selected slot; allow explicit specialty override
    User specialist = slot == null ? null : slot.getSpecialist();
    Specialty specialty = specialist != null && specialist.getSpecialty() != null
        ? specialist.getSpecialty()
        : (specialtyId == null ? null : specialtyDao.find(specialtyId));

    if (isBlank(question) || specialty == null || specialist == null || slot == null) {
      response.sendRedirect(request.getContextPath()
          + "/general-practitioner/expertise/new?consultationId=" + consultationId + "&error=missing");
      return;
    }
    Priority priority;
    try {
      priority = Priority.valueOf(priorityParam == null ? "NORMAL" : priorityParam);
    } catch (IllegalArgumentException e) {
      priority = Priority.NORMAL;
    }
    User gp = currentGp(request);
    if (gp == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }
    ExpertiseRequest er = new ExpertiseRequest();
    er.setConsultation(c);
    er.setGeneralPractitioner(gp);
    er.setSpecialist(specialist);
    er.setRequestedSpecialty(specialty);
    er.setQuestion(question);
    er.setAnalysisData(analysisData);
    er.setPriority(priority);
    er.setTimeSlot(slot);
    er.setExpertiseCost(specialist.getConsultationFee());
    expertiseRequestDao.create(er);

    slot.setStatus(SlotStatus.BOOKED);
    timeSlotDao.update(slot);

    c.setStatus(ConsultationStatus.PENDING_SPECIALIST_REVIEW);
    consultationDao.update(c);

    response.sendRedirect(request.getContextPath()
        + "/general-practitioner/patient?id=" + c.getPatient().getId() + "&expertise=1");
  }

  private User currentGp(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("userId") == null) {
      return null;
    }
    return userDao.find(Long.valueOf(session.getAttribute("userId").toString()));
  }

  private static Long parseLong(String s) {
    try {
      return isBlank(s) ? null : Long.valueOf(s.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static String trim(String s) {
    return s == null ? null : s.trim();
  }

  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }
}
