<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Patient Reception</title>
  <style>
    *{ padding:0; margin:0; text-decoration:none; list-style:none; box-sizing:border-box; }
    body{ background-color:black; min-height:100vh; color:white; font-family:sans-serif; padding:80px 40px 40px; }
    form{ display:flex; flex-direction:column; gap:10px; max-width:400px; margin-top:20px; }
    input, button{ padding:12px; }
    table{ margin-top:20px; border-collapse:collapse; }
    th, td{ padding:8px 16px; border:1px solid #555; }
    a{ color:#6cf; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/navbar.jsp" %>
  <h1>Patient Reception</h1>
  <form method="GET" action="${pageContext.request.contextPath}/nurse/reception">
    <input type="text" name="q" placeholder="Search by name or health number" value="${q}" />
    <input type="submit" value="Search" />
  </form>
  ${param.error == 'notfound' ? '<small style="color:red">Patient not found.</small>' : ''}
  <p style="margin-top:15px"><a href="${pageContext.request.contextPath}/nurse/reception/new">+ Register new patient</a></p>

  <% if (request.getAttribute("results") != null) {
       java.util.List<?> results = (java.util.List<?>) request.getAttribute("results");
       if (!results.isEmpty()) { %>
    <table>
      <tr><th>Name</th><th>DOB</th><th>Health Number</th><th></th></tr>
      <% for (Object o : results) {
           project.models.Patient p = (project.models.Patient) o; %>
        <tr>
          <td><%= p.getFirstName() %> <%= p.getLastName() %></td>
          <td><%= p.getBirthDate() %></td>
          <td><%= p.getHealthNumber() %></td>
          <td><a href="${pageContext.request.contextPath}/nurse/reception/patient?id=<%= p.getId() %>">Select</a></td>
        </tr>
      <% } %>
    </table>
  <% } } %>
</body>
</html>
