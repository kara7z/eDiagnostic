<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard</title>
    <style>
    *{
      padding: 0;
      margin: 0;
      text-decoration: none;
      list-style: none;
      box-sizing: border-box;
    }
    body{
      background-color: black;
      min-height: 100vh;
      width: 100%;
      margin: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      color: white;
      font-family: sans-serif;
    }
    #dashboard{
      display: flex;
      flex-direction: column;
      gap:10px;
      align-items: center;
    }
    #logout-btn{
      padding: 12px 24px;
      cursor: pointer;
    }
    </style>
</head>
<body>
  <div id="dashboard">
    <h1>Dashboard</h1>
    <p>Role: ${sessionScope.role}</p>
    <a href="${pageContext.request.contextPath}/logout"><button id="logout-btn" type="button">Logout</button></a>
  </div>

</body>
</html>
