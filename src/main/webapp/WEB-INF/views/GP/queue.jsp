<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>GP Queue</title>
  <style>
    *{ padding:0; margin:0; text-decoration:none; list-style:none; box-sizing:border-box; }
    body{ background-color:black; min-height:100vh; color:white; font-family:sans-serif; padding:80px 40px 40px; }
    table{ margin-top:20px; border-collapse:collapse; }
    th, td{ padding:8px 16px; border:1px solid #555; }
    a{ color:#6cf; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/navbar.jsp" %>
  <h1>Waiting patients</h1>
  ${param.error == 'queue' ? '<small style="color:red">Queue entry not found.</small>' : ''}
  ${param.error == 'notfound' ? '<small style="color:red">Not found.</small>' : ''}
  <%
    java.util.List<?> entries = (java.util.List<?>) request.getAttribute("entries");
    if (entries != null && !entries.isEmpty()) { %>
    <table>
      <tr><th>Name</th><th>NSS</th><th>Arrival</th><th>Action</th></tr>
      <% for (Object o : entries) {
           project.models.QueueEntry q = (project.models.QueueEntry) o;
           project.models.Patient p = q.getPatient(); %>
        <tr>
          <td><%= p.getFirstName() %> <%= p.getLastName() %></td>
          <td><%= p.getHealthNumber() %></td>
          <td><%= q.getArrivalTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></td>
          <td>
            <a href="${pageContext.request.contextPath}/general-practitioner/patient?id=<%= p.getId() %>">Dossier</a> |
            <a href="${pageContext.request.contextPath}/general-practitioner/consultation/new?queueEntryId=<%= q.getId() %>">Start consultation</a>
          </td>
        </tr>
      <% } %>
    </table>
  <% } else { %>
    <p style="margin-top:20px">No waiting patients.</p>
  <% } %>
</body>
</html>
