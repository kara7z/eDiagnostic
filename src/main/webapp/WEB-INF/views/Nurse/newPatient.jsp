<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>New Patient</title>
  <style>
    *{ padding:0; margin:0; text-decoration:none; list-style:none; box-sizing:border-box; }
    body{ background-color:black; min-height:100vh; color:white; font-family:sans-serif; padding:80px 40px 40px; }
    form{ display:flex; flex-direction:column; gap:10px; max-width:400px; margin-top:20px; }
    input, button{ padding:12px; }
    a{ color:#6cf; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/navbar.jsp" %>
  <h1>Register New Patient</h1>
  <form method="POST" action="${pageContext.request.contextPath}/nurse/reception/new">
    <input type="text" name="firstName" placeholder="First name" required />
    <input type="text" name="lastName" placeholder="Last name" required />
    <label>Date of birth: <input type="date" name="birthDate" required /></label>
    <input type="text" name="healthNumber" placeholder="NSS / Health number" required />
    <input type="text" name="phone" placeholder="Phone" />
    <input type="text" name="address" placeholder="Address" />
    ${param.error == 'empty' ? '<small style="color:red">Please fill all required fields.</small>' : ''}
    ${param.error == 'dob' ? '<small style="color:red">Invalid date of birth.</small>' : ''}
    ${param.error == 'exists' ? '<small style="color:red">Health number already registered.</small>' : ''}
    <input type="submit" value="Create patient" />
  </form>
  <p style="margin-top:15px"><a href="${pageContext.request.contextPath}/nurse/reception">Back to search</a></p>
</body>
</html>
