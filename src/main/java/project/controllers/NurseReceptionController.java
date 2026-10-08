package project.controllers;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dao.PatientDao;
import project.dao.QueueEntryDao;
import project.dao.VitalSignDao;
import project.models.Patient;
import project.models.QueueEntry;
import project.models.VitalSign;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet({ "/nurse/reception", "/nurse/reception/new", "/nurse/reception/patient", "/nurse/reception/vitals" })
public class NurseReceptionController extends HttpServlet {

  @Inject
  private PatientDao patientDao;

  @Inject
  private VitalSignDao vitalSignDao;

  @Inject
  private QueueEntryDao queueEntryDao;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getServletPath();
    switch (path) {
      case "/nurse/reception/new":
        request.getRequestDispatcher("/WEB-INF/views/Nurse/newPatient.jsp").forward(request, response);
        break;
      case "/nurse/reception/patient": {
        Patient p = findPatient(request);
        if (p == null) {
          response.sendRedirect(request.getContextPath() + "/nurse/reception?error=notfound");
          return;
        }
        request.setAttribute("patient", p);
        request.getRequestDispatcher("/WEB-INF/views/Nurse/patient.jsp").forward(request, response);
        break;
      }
      default: {
        String q = request.getParameter("q");
        if (q != null && !q.trim().isEmpty()) {
          List<Patient> results = patientDao.search(q.trim());
          request.setAttribute("results", results);
          request.setAttribute("q", q.trim());
        }
        request.getRequestDispatcher("/WEB-INF/views/Nurse/reception.jsp").forward(request, response);
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String path = request.getServletPath();
    switch (path) {
      case "/nurse/reception/new":
        handleCreatePatient(request, response);
        break;
      case "/nurse/reception/vitals":
        handleVitals(request, response);
        break;
      default:
        response.sendRedirect(request.getContextPath() + "/nurse/reception");
    }
  }

  private Patient findPatient(HttpServletRequest request) {
    String idParam = request.getParameter("id");
    if (idParam == null) {
      return null;
    }
    try {
      return patientDao.find(Long.parseLong(idParam));
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private void handleCreatePatient(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String firstName = trim(request.getParameter("firstName"));
    String lastName = trim(request.getParameter("lastName"));
    String dobParam = trim(request.getParameter("birthDate"));
    String healthNumber = trim(request.getParameter("healthNumber"));
    String phone = trim(request.getParameter("phone"));
    String address = trim(request.getParameter("address"));

    if (isBlank(firstName) || isBlank(lastName) || isBlank(dobParam) || isBlank(healthNumber)) {
      response.sendRedirect(request.getContextPath() + "/nurse/reception/new?error=empty");
      return;
    }
    LocalDate birthDate;
    try {
      birthDate = LocalDate.parse(dobParam);
    } catch (Exception e) {
      response.sendRedirect(request.getContextPath() + "/nurse/reception/new?error=dob");
      return;
    }
    if (patientDao.findByHealthNumber(healthNumber) != null) {
      response.sendRedirect(request.getContextPath() + "/nurse/reception/new?error=exists");
      return;
    }

    Patient patient = new Patient();
    patient.setFirstName(firstName);
    patient.setLastName(lastName);
    patient.setBirthDate(birthDate);
    patient.setHealthNumber(healthNumber);
    patient.setPhone(phone);
    patient.setAddress(address);
    patientDao.create(patient);

    queueEntryDao.create(new QueueEntry(patient));

    response.sendRedirect(request.getContextPath() + "/nurse/reception/patient?id=" + patient.getId() + "&created=1");
  }

  private void handleVitals(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    Patient patient = findPatient(request);
    if (patient == null) {
      response.sendRedirect(request.getContextPath() + "/nurse/reception?error=notfound");
      return;
    }

    VitalSign v = new VitalSign();
    v.setPatient(patient);
    v.setBloodPressure(trim(request.getParameter("bloodPressure")));
    v.setHeartRate(parseInt(request.getParameter("heartRate")));
    v.setTemperature(parseDecimal(request.getParameter("temperature")));
    v.setRespiratoryRate(parseInt(request.getParameter("respiratoryRate")));
    v.setWeight(parseDouble(request.getParameter("weight")));
    v.setHeight(parseDouble(request.getParameter("height")));
    vitalSignDao.create(v);

    queueEntryDao.create(new QueueEntry(patient));

    response.sendRedirect(request.getContextPath() + "/nurse/reception/patient?id=" + patient.getId() + "&vitals=1");
  }

  private static String trim(String s) {
    return s == null ? null : s.trim();
  }

  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

  private static Integer parseInt(String s) {
    try {
      return isBlank(s) ? null : Integer.valueOf(s.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static Double parseDouble(String s) {
    try {
      return isBlank(s) ? null : Double.valueOf(s.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static BigDecimal parseDecimal(String s) {
    try {
      return isBlank(s) ? null : new BigDecimal(s.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
