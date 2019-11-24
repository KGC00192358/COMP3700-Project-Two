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
        context = server.createContext("/UpdateProduct");
        context.setHandler(new updateProductRequestHandler());

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
        public static final String DB_FILE = "C:\\Users\\Kevin\\IdeaProjects\\ProjectTwo\\Data\\store.db";
        public void handle(HttpExchange exchange) throws IOException {
            IDataAdapter adapter = new SQLiteDataAdapter();
            adapter.connect(DB_FILE);
            StringBuilder response = new StringBuilder();

            String query = exchange.getRequestURI().getQuery();
            String full_query = exchange.getRequestURI().getQuery();
            int pos = query.indexOf("=");
            int id = Integer.parseInt(query.substring(pos + 1));

            try {
                ProductModel prod = adapter.loadProduct(id);
                response.append(prod.mProductID);
                response.append("," + prod.mName);
                response.append("," + prod.mPrice);
                response.append("," + prod.mQuantity);
                exchange.sendResponseHeaders(200, response.length());//response code and length
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();
            } catch (java.lang.NullPointerException e) {
                response.append("No Product Loaded!");
                exchange.sendResponseHeaders(201, response.length());//response code and length
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                adapter.disconnect();
            }

        }
    }
    static class updateProductRequestHandler implements  HttpHandler {

        public static final String DB_FILE = "C:\\Users\\Kevin\\IdeaProjects\\ProjectTwo\\Data\\store.db";
        public void handle(HttpExchange exchange) throws IOException {
            boolean error = false;
            SQLiteDataAdapter adapter = new SQLiteDataAdapter();
            adapter.connect(DB_FILE);
            StringBuilder response = new StringBuilder();

            String query = exchange.getRequestURI().getQuery();
            String[] information = query.split("&");
            ProductModel prod_to_save = new ProductModel();
            prod_to_save.mProductID = Integer.parseInt(information[0].substring(information[0].indexOf("=") + 1));
            prod_to_save.mName = information[1].substring(information[1].indexOf("=") + 1);
            prod_to_save.mPrice = Double.parseDouble(information[2].substring(information[2].indexOf("=") + 1));
            prod_to_save.mQuantity = Double.parseDouble(information[3].substring(information[3].indexOf("=") + 1));
            switch (adapter.deleteProductById(prod_to_save.mProductID)) {
                case 1:
                    response.append("Delete Portion Failed: Unspecified");
                    error = true;
                    break;
                case -1:
                    response.append("Delete Portion Failed: Unspecified SQL Error");
                    error = true;
                    break;
                case -2:
                    response.append("Delete Portion Failed: Not Found");
                    error = true;
                    break;
                default:
                    response.append("Delete Portion Success");
                    break;
            }
            if(!error) {
                switch (adapter.saveProduct(prod_to_save)) {
                    case -1:
                        response.append("Failed: Unspecified");
                        break;
                    case 1:
                        response.append("Failed: Duplicate");
                        break;
                    default:
                        response.append("Success");
                        break;
                }
            }

            exchange.sendResponseHeaders(200, response.length());//response code and length
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
            adapter.disconnect();
        }
    }
    static class saveProductRequestHandler implements HttpHandler {
        public static final String DB_FILE = "C:\\Users\\Kevin\\IdeaProjects\\ProjectTwo\\Data\\store.db";
        public void handle(HttpExchange exchange) throws IOException {
            IDataAdapter adapter = new SQLiteDataAdapter();
            adapter.connect(DB_FILE);
            StringBuilder response = new StringBuilder();

            String query = exchange.getRequestURI().getQuery();
            String[] information = query.split("&");
            ProductModel prod_to_save = new ProductModel();
            prod_to_save.mProductID = Integer.parseInt(information[0].substring(information[0].indexOf("=") + 1));
            prod_to_save.mName = information[1].substring(information[1].indexOf("=") + 1);
            prod_to_save.mPrice = Double.parseDouble(information[2].substring(information[2].indexOf("=") + 1));
            prod_to_save.mQuantity = Double.parseDouble(information[3].substring(information[3].indexOf("=") + 1));

            switch (adapter.saveProduct(prod_to_save)) {
                case -1:
                    response.append("Failed: Unspecified");
                    break;
                case 1:
                    response.append("Failed: Duplicate");
                    break;
                default:
                    response.append("Success");
                    break;
            }


            exchange.sendResponseHeaders(200, response.length());//response code and length
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
            adapter.disconnect();
        }
    }
}