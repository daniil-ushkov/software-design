package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.server.Response;
import ru.akirakozov.sd.refactoring.dao.Dao;
import ru.akirakozov.sd.refactoring.http.ResponseWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final Dao dao;

    public QueryServlet(Dao dao) {
        this.dao = dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try {
                ResponseWriter.writeProducts(
                        response,
                        "Product with max price",
                        List.of(dao.getMax())
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                ResponseWriter.writeProducts(
                        response,
                        "Product with min price",
                        List.of(dao.getMin())
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                ResponseWriter.writeLong(
                        response,
                        "Summary price",
                        dao.getSum()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                ResponseWriter.writeLong(
                        response,
                        "Number of products",
                        dao.getCount()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
