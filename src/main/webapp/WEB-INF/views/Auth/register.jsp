<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register</title>
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
    input, select{
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
      <input type="text" name="firstName" placeholder="Please Enter your first name" value="${param.firstName}" required />
      <input type="text" name="lastName" placeholder="Please Enter your last name" value="${param.lastName}" required />
      <input type="email" name="email" placeholder="Please Enter your email" value="${param.email}" required />
      ${param.error == 'exists' ? '<small style="color:red">Email already registered.</small>' : ''}
      ${param.error == 'empty' ? '<small style="color:red">Please fill all fields.</small>' : ''}
      ${param.error == 'invalid_role' ? '<small style="color:red">Please select a valid role.</small>' : ''}
      <input type="password" name="password" placeholder="Please Enter your password" required />
      <select name="role" required>
        <option value="">Select role</option>
        <option value="NURSE" ${param.role == 'NURSE' ? 'selected' : ''}>Nurse</option>
        <option value="GENERAL_PRACTITIONER" ${param.role == 'GENERAL_PRACTITIONER' ? 'selected' : ''}>General Practitioner</option>
        <option value="SPECIALIST" ${param.role == 'SPECIALIST' ? 'selected' : ''}>Specialist</option>
      </select>
      <input type="submit" value="register"/>
  </form>

</body>
</html>
