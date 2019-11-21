import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class WebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(new rootHandler());

        context = server.createContext("/Product");
        context.setHandler(new productRequestHandler());
        context = server.createContext("/SaveProduct");
        context.setHandler(new saveProductRequestHandler());
        server.start();
    }

    static class rootHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hi there!";
            exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class productRequestHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder response = new StringBuilder();
            response.append("<html><body>");

            String query = exchange.getRequestURI().getQuery();
            String full_query = exchange.getRequestURI().getQuery();
            int pos = query.indexOf("=");
            int id = Integer.parseInt(query.substring(pos + 1));

            response.append("<h3>Hi there! You want to ask about product id = " + id);
            response.append("Full Query: " + full_query);

            String dbfile = "C:\\Users\\Kevin\\IdeaProjects\\ProjectTwo\\Data\\store.db";
            String url = "jdbc:sqlite:" + dbfile;
            try {
                Connection conn = DriverManager.getConnection(url);

                String sql = "SELECT * FROM Products WHERE mProductID = " + Integer.toString(id);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                response.append("<table border = '1'>");
                response.append("<th>ProductID</th>");
                response.append("<th>Product Name</th>");
                response.append("<th>Price</th>");
                response.append("<th>Availability</th>");

                while (rs.next()) {
                    response.append("<tr><td>" + rs.getInt("mProductID") + "</td>");
                    response.append("    <td>" + rs.getString("mName") + "</td>");
                    response.append("    <td>" + rs.getDouble("mPrice") + "</td>");
                    double quant = rs.getDouble("mQuantity");
                    String status;
                    if (quant > 0)
                        status = "In stock";
                    else
                        status = "Out of stock";
                    response.append("    <td>" + status + "</td></tr>");
                }
                response.append("</table>");
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            response.append("</body></html>");
            exchange.sendResponseHeaders(200, response.length());//response code and length
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }
    static class saveProductRequestHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hi there!";
            exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}