package ru.akirakozov.sd.refactoring.http;

import ru.akirakozov.sd.refactoring.dao.Dao;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public final class ResponseWriter {
    private ResponseWriter() {
    }

    public static void writeLong(HttpServletResponse response, String title, long value) throws IOException {
        response.getWriter().println("<html><body>");
        response.getWriter().println(title + ": ");
        response.getWriter().println(value);
        response.getWriter().println("</body></html>");
    }

    public static void writeProducts(HttpServletResponse response,
                                     String title, List<Dao.Product> products) throws IOException {
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>" + title + ": </h1>");
        for (Dao.Product product : products) {
            response.getWriter().println(product.getName()
                    + "\t" + product.getPrice() + "</br>");
        }
        response.getWriter().println("</body></html>");
    }
}
