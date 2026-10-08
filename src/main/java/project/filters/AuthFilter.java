package project.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import project.enums.Role;

/**
 * AuthFilter
 */
@WebFilter({
    "/nurse/*",
    "/general-practitioner/*",
    "/specialist/*"
})
public class AuthFilter implements Filter {
  // nurse , general-practitioner, specialist;
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("userId") == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String path = request.getRequestURI()
        .substring(request.getContextPath().length());
    Role sessionRole = path.startsWith("/nurse/") ? Role.NURSE
        : path.startsWith("/general-practitioner/") ? Role.GENERAL_PRACTITIONER
            : Role.SPECIALIST;
    if (!sessionRole.equals(session.getAttribute("role"))) {
      response.sendRedirect(request.getContextPath() + "/dashboard");
      return;
    }
    chain.doFilter(request, response);

  }

}
