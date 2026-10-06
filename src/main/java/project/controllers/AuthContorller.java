package project.controllers;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import project.dao.UserDao;
import project.models.User;

import java.io.IOException;

/**
 * AuthContorller
 */
@WebServlet({ "/login", "/logout" })
public class AuthContorller extends HttpServlet {
  @Inject
  private UserDao userDao;

  private User findByEmail(String email, String password) {
    if (email == null || password == null) {
      return null;
    }
    User user = userDao.findByEmail(email);
    if (user != null && user.getPasswordHash().equals(password)) {
      return user;
    }
    return null;
  }

  @Override
  protected void doGet(
      HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    if (request.getServletPath().equals("/logout")) {
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }
    request.getRequestDispatcher(
        "/WEB-INF/views/Auth/login.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String path = request.getServletPath();
    if (path.equals("/logout")) {
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }
    // Login with email
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    User user = findByEmail(email, password);
    if (user == null) {
      response.sendRedirect(request.getContextPath() + "/login?error=invalid");
      return;
    }
    HttpSession session = request.getSession();
    request.changeSessionId();
    session.setAttribute("userId", user.getId());
    session.setAttribute("role", user.getRole());

    response.sendRedirect(request.getContextPath() + "/dashboard");

  }

}
