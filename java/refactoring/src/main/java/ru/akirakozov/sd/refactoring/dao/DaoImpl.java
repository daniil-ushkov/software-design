package ru.akirakozov.sd.refactoring.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DaoImpl implements Dao {

    private final Connection c;

    public DaoImpl(String url) throws SQLException {
        c = DriverManager.getConnection(url);

        String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
        Statement stmt = c.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        c.close();
    }

    @Override
    public void clear() throws SQLException {
        String sql = "DELETE FROM PRODUCT";
        Statement stmt = c.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
        Statement stmt = c.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    @Override
    public List<Product> getAll() throws SQLException {
        List<Product> result = new ArrayList<>();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");

        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            result.add(new Product(name, price));
        }

        rs.close();
        stmt.close();

        return result;
    }

    @Override
    public Product getMax() throws SQLException {
        Product product = null;

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            product = new Product(name, price);
        }

        rs.close();
        stmt.close();

        return product;
    }

    @Override
    public Product getMin() throws SQLException {
        Product product = null;

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            product = new Product(name, price);
        }

        rs.close();
        stmt.close();

        return product;
    }

    @Override
    public long getSum() throws SQLException {
        int sum = 0;

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");

        if (rs.next()) {
            sum = rs.getInt(1);
        }

        rs.close();
        stmt.close();

        return sum;
    }

    @Override
    public long getCount() throws SQLException {
        int count = 0;

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");

        if (rs.next()) {
            count = rs.getInt(1);
        }

        rs.close();
        stmt.close();

        return count;
    }
}
