<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Request Expertise</title>
  <style>
    *{ padding:0; margin:0; text-decoration:none; list-style:none; box-sizing:border-box; }
    body{ background-color:black; min-height:100vh; color:white; font-family:sans-serif; padding:80px 40px 40px; }
    form{ display:flex; flex-direction:column; gap:10px; max-width:500px; margin-top:20px; }
    input, button, select, textarea{ padding:12px; }
    a{ color:#6cf; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/navbar.jsp" %>
  <h1>Request expertise</h1>
  <p>Patient: ${consultation.patient.firstName} ${consultation.patient.lastName} | Reason: ${consultation.reason}</p>
  ${param.error == 'missing' ? '<small style="color:red">Please fill question, specialty, specialist and slot.</small>' : ''}

  <form method="GET" action="${pageContext.request.contextPath}/general-practitioner/expertise/new" style="margin-top:15px">
    <input type="hidden" name="consultationId" value="${consultation.id}" />
    <select name="specialtyId" onchange="this.form.submit()">
      <option value="">All specialties</option>
      <%
        java.util.List<?> specs = (java.util.List<?>) request.getAttribute("specialties");
        if (specs != null) for (Object o : specs) {
          project.models.Specialty s = (project.models.Specialty) o;
          boolean sel = request.getAttribute("selectedSpecialty") != null
              && ((project.models.Specialty) request.getAttribute("selectedSpecialty")).getId().equals(s.getId()); %>
        <option value="<%= s.getId() %>" <%= sel ? "selected" : "" %>><%= s.getName() %></option>
      <% } %>
    </select>
    <select name="specialistId" onchange="this.form.submit()">
      <option value="">All specialists</option>
      <%
        java.util.List<?> ss = (java.util.List<?>) request.getAttribute("specialists");
        if (ss != null) for (Object o : ss) {
          project.models.User u = (project.models.User) o;
          boolean sel = request.getAttribute("selectedSpecialist") != null
              && ((project.models.User) request.getAttribute("selectedSpecialist")).getId().equals(u.getId()); %>
        <option value="<%= u.getId() %>" <%= sel ? "selected" : "" %>><%= u.getFirstName() %> <%= u.getLastName() %> - <%= u.getConsultationFee() %> DH</option>
      <% } %>
    </select>
  </form>

  <form method="POST" action="${pageContext.request.contextPath}/general-practitioner/expertise">
    <input type="hidden" name="consultationId" value="${consultation.id}" />
    <input type="hidden" name="specialtyId" value="${selectedSpecialty != null ? selectedSpecialty.id : ''}" />
    <input type="hidden" name="specialistId" value="${selectedSpecialist != null ? selectedSpecialist.id : ''}" />
    <textarea name="question" placeholder="Question" rows="2" required></textarea>
    <textarea name="analysisData" placeholder="Analyses / results" rows="2"></textarea>
    <select name="priority">
      <option value="URGENT">URGENTE</option>
      <option value="NORMAL" selected>NORMALE</option>
      <option value="NON_URGENT">NON_URGENTE</option>
    </select>
    <select name="timeSlotId" required>
      <option value="">Select a future slot</option>
      <%
        java.util.List<?> slots = (java.util.List<?>) request.getAttribute("slots");
        if (slots != null) for (Object o : slots) {
          project.models.TimeSlot t = (project.models.TimeSlot) o; %>
        <option value="<%= t.getId() %>"><%= t.getDate() %> <%= t.getStartTime() %> - <%= t.getEndTime() %> (<%= t.getSpecialist().getFirstName() %> <%= t.getSpecialist().getLastName() %>)</option>
      <% } %>
    </select>
    <input type="submit" value="Send expertise request" />
  </form>
  <p style="margin-top:15px"><a href="${pageContext.request.contextPath}/general-practitioner/patient?id=${consultation.patient.id}">Back to dossier</a></p>
</body>
</html>
