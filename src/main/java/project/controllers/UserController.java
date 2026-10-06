package project.controllers;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dao.UserDao;
import project.enums.Role;
import project.models.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * UserController
 */
@WebServlet({ "/register" })
public class UserController extends HttpServlet {
  @Inject
  private UserDao userDao;

  @Override
  protected void doGet(
      HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    request.getRequestDispatcher(
        "/WEB-INF/views/Auth/register.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String roleParam = request.getParameter("role");
    if (isBlank(firstName) || isBlank(lastName) || isBlank(email) || isBlank(password) || isBlank(roleParam)) {
      response.sendRedirect(request.getContextPath() + "/register?error=empty");
      return;
    }
    Role role;
    try {
      role = Role.valueOf(roleParam);
    } catch (IllegalArgumentException e) {
      response.sendRedirect(request.getContextPath() + "/register?error=invalid_role");
      return;
    }
    if (userDao.findByEmail(email.trim()) != null) {
      response.sendRedirect(request.getContextPath() + "/register?error=exists&email="
          + URLEncoder.encode(email.trim(), StandardCharsets.UTF_8));
      return;
    }
    User newUser = new User();
    newUser.setFirstName(firstName.trim());
    newUser.setLastName(lastName.trim());
    newUser.setEmail(email.trim());
    newUser.setPasswordHash(password);
    newUser.setRole(role);
    newUser.setActive(true);
    userDao.create(newUser);
    response.sendRedirect(request.getContextPath() + "/login");
  }

  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

}
