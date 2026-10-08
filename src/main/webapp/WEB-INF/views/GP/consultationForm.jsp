<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>New Consultation</title>
  <style>
    *{ padding:0; margin:0; text-decoration:none; list-style:none; box-sizing:border-box; }
    body{ background-color:black; min-height:100vh; color:white; font-family:sans-serif; padding:80px 40px 40px; }
    form{ display:flex; flex-direction:column; gap:10px; max-width:500px; margin-top:20px; }
    input, button, textarea{ padding:12px; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/navbar.jsp" %>
  <h1>New consultation - ${patient.firstName} ${patient.lastName}</h1>
  <p>Arrival: <%= ((project.models.QueueEntry) request.getAttribute("queueEntry")).getArrivalTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %> | Fixed cost: 150 DH</p>
  ${param.error == 'reason' ? '<small style="color:red">Reason (motif) is required.</small>' : ''}
  <form method="POST" action="${pageContext.request.contextPath}/general-practitioner/consultation">
    <input type="hidden" name="queueEntryId" value="${queueEntry.id}" />
    <textarea name="reason" placeholder="Motif / reason" rows="2" required></textarea>
    <textarea name="notes" placeholder="Observations" rows="2"></textarea>
    <textarea name="clinicalExamination" placeholder="Clinical exam" rows="2"></textarea>
    <textarea name="symptomAnalysis" placeholder="Symptoms analysis" rows="2"></textarea>
    <input type="text" name="diagnosis" placeholder="Diagnosis (optional - closes directly)" />
    <input type="text" name="treatment" placeholder="Treatment (optional - closes directly)" />
    <input type="submit" value="Save consultation" />
  </form>
</body>
</html>
