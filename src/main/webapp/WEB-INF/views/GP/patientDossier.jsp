<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dossier</title>
  <style>
    *{ padding:0; margin:0; text-decoration:none; list-style:none; box-sizing:border-box; }
    body{ background-color:black; min-height:100vh; color:white; font-family:sans-serif; padding:80px 40px 40px; }
    table{ margin-top:20px; border-collapse:collapse; }
    th, td{ padding:8px 16px; border:1px solid #555; }
    a{ color:#6cf; }
    form{ display:flex; flex-direction:column; gap:10px; max-width:400px; margin:20px 0; }
    input, button, textarea{ padding:12px; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/navbar.jsp" %>
  <h1>Dossier - ${patient.firstName} ${patient.lastName}</h1>
  <p>DOB: ${patient.birthDate} | NSS: ${patient.healthNumber} | Phone: ${patient.phone} | ${patient.address}</p>

  ${param.created == '1' ? '<p style="color:lightgreen;margin-top:10px">Consultation saved (150 DH).</p>' : ''}
  ${param.expertise == '1' ? '<p style="color:lightgreen;margin-top:10px">Expertise request created - specialist notified, slot booked, consultation PENDING_SPECIALIST_REVIEW.</p>' : ''}
  ${param.closed == '1' ? '<p style="color:lightgreen;margin-top:10px">Consultation closed (TERMINEE).</p>' : ''}
  ${param.error == 'close' ? '<small style="color:red">Diagnosis and treatment are required.</small>' : ''}

  <h2 style="margin-top:20px">Consultations</h2>
  <%
    java.util.List<?> list = (java.util.List<?>) request.getAttribute("consultations");
    if (list != null && !list.isEmpty()) { %>
    <table>
      <tr><th>Date</th><th>Reason</th><th>Status</th><th>Cost</th><th>Close</th></tr>
      <% for (Object o : list) {
           project.models.Consultation c = (project.models.Consultation) o; %>
        <tr>
          <td><%= c.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></td>
          <td><%= c.getReason() %></td>
          <td><%= c.getStatus() %></td>
          <td><%= c.getConsultationCost() %> DH</td>
          <td>
            <% if (c.getStatus() != project.enums.ConsultationStatus.COMPLETED) { %>
            <a href="${pageContext.request.contextPath}/general-practitioner/expertise/new?consultationId=<%= c.getId() %>">Request expertise</a>
            <form method="POST" action="${pageContext.request.contextPath}/general-practitioner/consultation/close" style="margin:0">
              <input type="hidden" name="id" value="<%= c.getId() %>" />
              <input type="text" name="diagnosis" placeholder="Diagnosis" required />
              <input type="text" name="treatment" placeholder="Treatment" required />
              <input type="submit" value="Close" />
            </form>
            <% } else { %>
              <%= c.getDiagnosis() %> / <%= c.getTreatment() %>
            <% } %>
          </td>
        </tr>
      <% } %>
    </table>
  <% } else { %>
    <p style="margin-top:10px">No consultations yet.</p>
  <% } %>
</body>
</html>
