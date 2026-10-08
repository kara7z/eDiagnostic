package project.controllers;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dao.QueueEntryDao;
import project.dao.VitalSignDao;
import project.models.QueueEntry;
import project.models.VitalSign;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/nurse/daylist")
public class NurseDayListController extends HttpServlet {

  @Inject
  private QueueEntryDao queueEntryDao;

  @Inject
  private VitalSignDao vitalSignDao;

  public static class Row {
    public final QueueEntry entry;
    public final VitalSign vitals;

    Row(QueueEntry entry, VitalSign vitals) {
      this.entry = entry;
      this.vitals = vitals;
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String dateParam = request.getParameter("date");
    LocalDate date;
    if (dateParam == null || dateParam.trim().isEmpty()) {
      date = LocalDate.now();
    } else {
      try {
        date = LocalDate.parse(dateParam.trim());
      } catch (Exception e) {
        response.sendRedirect(request.getContextPath() + "/nurse/daylist?error=date");
        return;
      }
    }

    List<QueueEntry> all = queueEntryDao.findAllOrderedByArrival();
    List<Row> rows = all.stream()
        .filter(q -> q.getArrivalTime().toLocalDate().equals(date))
        .sorted(Comparator.comparing(QueueEntry::getArrivalTime))
        .map(q -> new Row(q, vitalSignDao.findLatestByPatient(q.getPatient())))
        .collect(Collectors.toList());

    request.setAttribute("rows", rows);
    request.setAttribute("date", date.toString());
    request.getRequestDispatcher("/WEB-INF/views/Nurse/dayList.jsp").forward(request, response);
  }
}
