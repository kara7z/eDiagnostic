<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Patient</title>
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
  <h1>${patient.firstName} ${patient.lastName}</h1>
  <p>DOB: ${patient.birthDate} | Health number: ${patient.healthNumber}</p>
  <p>Phone: ${patient.phone} | Address: ${patient.address}</p>

  ${param.created == '1' ? '<p style="color:lightgreen;margin-top:10px">Patient created and added to the queue.</p>' : ''}
  ${param.vitals == '1' ? '<p style="color:lightgreen;margin-top:10px">Vitals recorded and patient added to the queue.</p>' : ''}

  <h2 style="margin-top:20px">Record vitals</h2>
  <form method="POST" action="${pageContext.request.contextPath}/nurse/reception/vitals?id=${patient.id}">
    <input type="text" name="bloodPressure" placeholder="BP (e.g. 120/80)" />
    <input type="number" name="heartRate" placeholder="HR (bpm)" />
    <input type="number" step="0.1" name="temperature" placeholder="Temp (°C)" />
    <input type="number" name="respiratoryRate" placeholder="RR (breaths/min)" />
    <input type="number" step="0.1" name="weight" placeholder="Weight (kg)" />
    <input type="number" step="0.1" name="height" placeholder="Height (cm)" />
    <input type="submit" value="Save vitals & add to queue" />
  </form>
  <p style="margin-top:15px"><a href="${pageContext.request.contextPath}/nurse/reception">Back to search</a></p>
</body>
</html>
