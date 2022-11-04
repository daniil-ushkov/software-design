package ru.akirakozov.sd.refactoring.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao {

    void close() throws SQLException;

    void clear() throws SQLException;

    void addProduct(Product product) throws SQLException;

    List<Product> getAll() throws SQLException;

    Product getMax() throws SQLException;

    Product getMin() throws SQLException;

    long getSum() throws SQLException;

    long getCount() throws SQLException;

    class Product {
        private final String name;
        private final long price;

        public Product(String name, long price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public long getPrice() {
            return price;
        }
    }
}
