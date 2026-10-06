<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
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
    }
    input{
      padding: 12px;
    }
    #auth-form{
      display: flex;
      flex-direction: column; 
      gap:10px;
    }
    </style>
</head>
<body>
  <form method="POST" id="auth-form">
      <input type="email" name="email" placeholder="Please Enter your email" required />
      ${param.error == 'invalid' ? '<small style="color:red">Invalid email or password.</small>' : ''}
      <input type="password" name="password" placeholder="Please Enter your password" required />
      <input type="submit" value="submit"/>
  </form>

</body>
</html>
