import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.dao.Dao;
import ru.akirakozov.sd.refactoring.dao.DaoImpl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.List;

public class ServletTest {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Dao dao = new DaoImpl("jdbc:sqlite:test.db");

    public ServletTest() throws SQLException {
    }

    @Before
    public void clear() throws SQLException {
        dao.clear();
    }

    public void initDb() throws IOException, InterruptedException {
        for (int i = 0; i < 3; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8081/add-product" +
                            "?name=product_" + i + "&price=" + i))
                    .build();
            HttpResponse<String> response
                    = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assert.assertEquals(200, response.statusCode());
        }
    }

    @Test
    public void testAddProduct() throws IOException, InterruptedException, SQLException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8081/add-product?name=aboba&price=1337"))
                .build();
        HttpResponse<String> response
                = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals(200, response.statusCode());

        List<Dao.Product> products = dao.getAll();
        Assert.assertEquals(1, products.size());
        Assert.assertEquals("aboba", products.get(0).getName());
        Assert.assertEquals(1337, products.get(0).getPrice());
    }

    @Test
    public void testGetProducts() throws IOException, InterruptedException {
        initDb();
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8081/get-products"))
                    .build();
            HttpResponse<String> response
                    = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assert.assertEquals(200, response.statusCode());
            Assert.assertEquals("<html><body>\n" +
                    "<h1>All products: </h1>\n" +
                    "product_0\t0</br>\n" +
                    "product_1\t1</br>\n" +
                    "product_2\t2</br>\n" +
                    "</body></html>\n", response.body());
        }
    }

    @Test
    public void testQueryMax() throws IOException, InterruptedException {
        initDb();
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8081/query?command=max"))
                    .build();
            HttpResponse<String> response
                    = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assert.assertEquals(200, response.statusCode());
            Assert.assertEquals("<html><body>\n" +
                    "<h1>Product with max price: </h1>\n" +
                    "product_2\t2</br>\n" +
                    "</body></html>\n", response.body());
        }
    }

    @Test
    public void testQueryMin() throws IOException, InterruptedException {
        initDb();
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8081/query?command=min"))
                    .build();
            HttpResponse<String> response
                    = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assert.assertEquals(200, response.statusCode());
            Assert.assertEquals("<html><body>\n" +
                    "<h1>Product with min price: </h1>\n" +
                    "product_0\t0</br>\n" +
                    "</body></html>\n", response.body());
        }
    }

    @Test
    public void testQuerySum() throws IOException, InterruptedException {
        initDb();
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8081/query?command=sum"))
                    .build();
            HttpResponse<String> response
                    = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assert.assertEquals(200, response.statusCode());
            Assert.assertEquals("<html><body>\n" +
                    "Summary price: \n" +
                    "3\n" +
                    "</body></html>\n", response.body());
        }
    }

    @Test
    public void testQueryCount() throws IOException, InterruptedException {
        initDb();
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8081/query?command=count"))
                    .build();
            HttpResponse<String> response
                    = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assert.assertEquals(200, response.statusCode());
            Assert.assertEquals("<html><body>\n" +
                    "Number of products: \n" +
                    "3\n" +
                    "</body></html>\n", response.body());
        }
    }
}
