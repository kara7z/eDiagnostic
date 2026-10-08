package project.controllers;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import project.dao.ConsultationDao;
import project.dao.PatientDao;
import project.dao.QueueEntryDao;
import project.dao.UserDao;
import project.enums.ConsultationStatus;
import project.enums.QueueStatus;
import project.models.Consultation;
import project.models.Patient;
import project.models.QueueEntry;
import project.models.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet({
    "/general-practitioner/queue",
    "/general-practitioner/patient",
    "/general-practitioner/consultation/new",
    "/general-practitioner/consultation",
    "/general-practitioner/consultation/close"
})
public class GpConsultationController extends HttpServlet {

  @Inject
  private QueueEntryDao queueEntryDao;

  @Inject
  private PatientDao patientDao;

  @Inject
  private ConsultationDao consultationDao;

  @Inject
  private UserDao userDao;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getServletPath();
    switch (path) {
      case "/general-practitioner/patient": {
        Patient p = findPatient(request);
        if (p == null) {
          response.sendRedirect(request.getContextPath() + "/general-practitioner/queue?error=notfound");
          return;
        }
        request.setAttribute("patient", p);
        request.setAttribute("consultations", consultationDao.findByPatient(p));
        request.getRequestDispatcher("/WEB-INF/views/GP/patientDossier.jsp").forward(request, response);
        break;
      }
      case "/general-practitioner/consultation/new": {
        Long qeId = parseLong(request.getParameter("queueEntryId"));
        QueueEntry qe = qeId == null ? null : queueEntryDao.findWithPatient(qeId);
        if (qe == null) {
          response.sendRedirect(request.getContextPath() + "/general-practitioner/queue?error=queue");
          return;
        }
        request.setAttribute("queueEntry", qe);
        request.setAttribute("patient", qe.getPatient());
        request.getRequestDispatcher("/WEB-INF/views/GP/consultationForm.jsp").forward(request, response);
        break;
      }
      default: {
        List<QueueEntry> waiting = queueEntryDao.findAllOrderedByArrival().stream()
            .filter(q -> q.getStatus() == QueueStatus.WAITING)
            .collect(Collectors.toList());
        request.setAttribute("entries", waiting);
        request.getRequestDispatcher("/WEB-INF/views/GP/queue.jsp").forward(request, response);
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String path = request.getServletPath();
    if (path.equals("/general-practitioner/consultation")) {
      createConsultation(request, response);
    } else if (path.equals("/general-practitioner/consultation/close")) {
      closeConsultation(request, response);
    } else {
      response.sendRedirect(request.getContextPath() + "/general-practitioner/queue");
    }
  }

  private void createConsultation(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Long qeId = parseLong(request.getParameter("queueEntryId"));
    QueueEntry qe = qeId == null ? null : queueEntryDao.findWithPatient(qeId);
    if (qe == null) {
      response.sendRedirect(request.getContextPath() + "/general-practitioner/queue?error=queue");
      return;
    }
    String reason = trim(request.getParameter("reason"));
    String notes = trim(request.getParameter("notes"));
    String clinical = trim(request.getParameter("clinicalExamination"));
    String symptoms = trim(request.getParameter("symptomAnalysis"));
    if (isBlank(reason)) {
      response.sendRedirect(request.getContextPath()
          + "/general-practitioner/consultation/new?queueEntryId=" + qeId + "&error=reason");
      return;
    }
    User gp = currentGp(request);
    if (gp == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }
    Consultation c = new Consultation();
    c.setPatient(qe.getPatient());
    c.setGeneralPractitioner(gp);
    c.setReason(reason);
    c.setNotes(notes);
    c.setClinicalExamination(clinical);
    c.setSymptomAnalysis(symptoms);
    // direct care close (TERMINEE): diagnosis/treatment may be entered now
    String diagnosis = trim(request.getParameter("diagnosis"));
    String treatment = trim(request.getParameter("treatment"));
    boolean directClose = !isBlank(diagnosis) || !isBlank(treatment);
    if (directClose) {
      c.setDiagnosis(diagnosis);
      c.setTreatment(treatment);
      c.setStatus(ConsultationStatus.COMPLETED);
      c.setClosedAt(LocalDateTime.now());
    }
    consultationDao.create(c);

    qe.setStatus(directClose ? QueueStatus.COMPLETED : QueueStatus.IN_CONSULTATION);
    queueEntryDao.update(qe);

    response.sendRedirect(request.getContextPath()
        + "/general-practitioner/patient?id=" + qe.getPatient().getId() + "&created=1");
  }

  private void closeConsultation(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Long id = parseLong(request.getParameter("id"));
    Consultation c = id == null ? null : consultationDao.findWithPatient(id);
    if (c == null) {
      response.sendRedirect(request.getContextPath() + "/general-practitioner/queue?error=notfound");
      return;
    }
    String diagnosis = trim(request.getParameter("diagnosis"));
    String treatment = trim(request.getParameter("treatment"));
    if (isBlank(diagnosis) || isBlank(treatment)) {
      response.sendRedirect(request.getContextPath() + "/general-practitioner/patient?id="
          + c.getPatient().getId() + "&error=close");
      return;
    }
    c.setDiagnosis(diagnosis);
    c.setTreatment(treatment);
    c.setStatus(ConsultationStatus.COMPLETED);
    c.setClosedAt(LocalDateTime.now());
    consultationDao.update(c);
    response.sendRedirect(request.getContextPath()
        + "/general-practitioner/patient?id=" + c.getPatient().getId() + "&closed=1");
  }

  private User currentGp(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("userId") == null) {
      return null;
    }
    Object id = session.getAttribute("userId");
    return userDao.find(Long.valueOf(id.toString()));
  }

  private Patient findPatient(HttpServletRequest request) {
    Long id = parseLong(request.getParameter("id"));
    return id == null ? null : patientDao.find(id);
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
