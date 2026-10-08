<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Day List</title>
  <style>
    *{ padding:0; margin:0; text-decoration:none; list-style:none; box-sizing:border-box; }
    body{ background-color:black; min-height:100vh; color:white; font-family:sans-serif; padding:80px 40px 40px; }
    form{ display:flex; gap:10px; margin-top:20px; }
    input, button{ padding:12px; }
    table{ margin-top:20px; border-collapse:collapse; }
    th, td{ padding:8px 16px; border:1px solid #555; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/navbar.jsp" %>
  <h1>Day patient list - ${date}</h1>
  ${param.error == 'date' ? '<small style="color:red">Invalid date.</small>' : ''}
  <form method="GET" action="${pageContext.request.contextPath}/nurse/daylist">
    <input type="date" name="date" value="${date}" />
    <input type="submit" value="Filter by registration date" />
    <a href="${pageContext.request.contextPath}/nurse/daylist"><button type="button">Reset</button></a>
  </form>
  <%
    java.util.List<?> rows = (java.util.List<?>) request.getAttribute("rows");
    if (rows != null && !rows.isEmpty()) { %>
    <table>
      <tr>
        <th>Name</th><th>NSS</th><th>Arrival</th><th>Status</th>
        <th>BP</th><th>HR</th><th>Temp</th><th>RR</th><th>Weight</th><th>Height</th>
      </tr>
      <% for (Object o : rows) {
           project.controllers.NurseDayListController.Row r = (project.controllers.NurseDayListController.Row) o;
           project.models.Patient p = r.entry.getPatient();
           project.models.VitalSign v = r.vitals; %>
        <tr>
          <td><%= p.getFirstName() %> <%= p.getLastName() %></td>
          <td><%= p.getHealthNumber() %></td>
          <td><%= r.entry.getArrivalTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></td>
          <td><%= r.entry.getStatus() %></td>
          <td><%= v != null && v.getBloodPressure() != null ? v.getBloodPressure() : "-" %></td>
          <td><%= v != null && v.getHeartRate() != null ? v.getHeartRate() : "-" %></td>
          <td><%= v != null && v.getTemperature() != null ? v.getTemperature() : "-" %></td>
          <td><%= v != null && v.getRespiratoryRate() != null ? v.getRespiratoryRate() : "-" %></td>
          <td><%= v != null && v.getWeight() != null ? v.getWeight() : "-" %></td>
          <td><%= v != null && v.getHeight() != null ? v.getHeight() : "-" %></td>
        </tr>
      <% } %>
    </table>
  <% } else { %>
    <p style="margin-top:20px">No patients for this date.</p>
  <% } %>
</body>
</html>
