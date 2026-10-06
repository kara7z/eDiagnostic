package project.controllers;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.dao.UserDao;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/health/db")
public class DbHealthServlet extends HttpServlet {

    @Inject
    private UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            int count = userDao.findAll().size();
            out.println("DB OK users=" + count);
        } catch (Exception e) {
            resp.setStatus(500);
            out.println("DB FAIL " + e);
            e.printStackTrace(out);
        }
    }
}
