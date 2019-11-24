
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.net.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SQLiteDataAdapter implements IDataAdapter {

    Connection conn = null;

    public int connect(String dbfile) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + dbfile;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_OPEN_FAILED;
        }
        return CONNECTION_OPEN_OK;
    }

    public int disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_CLOSE_FAILED;
        }
        System.out.println("Connection Closed.");
        return CONNECTION_CLOSE_OK;
    }

    public ProductModel loadProduct(int productID) {
        ProductModel product = null;

        try {
            String sql = "SELECT mProductID, mName, mPrice, mQuantity FROM Products WHERE mProductID = " + productID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                product = new ProductModel();
                product.mProductID = rs.getInt("mProductID");
                product.mName = rs.getString("mName");
                product.mPrice = rs.getDouble("mPrice");
                product.mQuantity = rs.getDouble("mQuantity");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return product;
    }

    public int saveProduct(ProductModel product) {
        try {
            String sql = "INSERT INTO Products(mProductID, mName, mPrice, mQuantity) VALUES " + product;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed")) {
                return PRODUCT_DUPLICATE_ERROR;
        }
        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            /*if (msg.contains("UNIQUE constraint failed"))
                return PRODUCT_DUPLICATE_ERROR;*/
            System.out.println(msg);
            return -1;
        }

        return PRODUCT_SAVED_OK;
    }
    public int deleteProductById(int id) {
        try {
            String sql = "DELETE FROM Products WHERE mProductID = " + String.valueOf(id);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
               String msg = e.getMessage();
               System.out.println(e.getMessage());
               if (msg.contains("Not Found")) {
                   return -2;
               } else {
                   return -1;
               }
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public PurchaseModel loadPurchase(int PurchaseID) {
        PurchaseModel purchase = null;

        try {
            String sql = "SELECT {PurchaseId, Total, Date FROM Purchases WHERE PurchaseId = " + PurchaseID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                purchase = new PurchaseModel();
                purchase.mPurchaseID = rs.getInt("PurchaseId");
                purchase.mTotal = rs.getDouble("Total");
                purchase.mDate = rs.getString("Date");

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return purchase;
    }

    public int savePurchase(PurchaseModel purchase) {
        try {
            String sql = "INSERT INTO Purchases VALUES " + purchase;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PURCHASE_DUPLICATE_ERROR;
        }

        return PURCHASE_SAVED_OK;

    }

    public int saveCustomer(CustomerModel cust) {
        try {
            String sql = "INSERT INTO Customers VALUES " + cust;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return CUSTOMER_DUP_ERR;
        }
        return CUSTOMER_SAVED_OK;
    }

    public CustomerModel loadCustomer(int id) {
        CustomerModel customer = null;

        try {
            String sql = "SELECT * FROM Customers WHERE CustomerId = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                customer = new CustomerModel();
                customer.mCustomerID = id;
                customer.mName = rs.getString("Name");
                customer.mPhone = rs.getString("Phone");
                customer.mAddress = rs.getString("Address");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }

    public int saveProductOverHttp(ProductModel prod)  {
        try {
            String URLString = "http://localhost:8080/SaveProduct";
            Map<String, String> parameters = new LinkedHashMap<String, String>();
            parameters.put("id", String.valueOf(prod.mProductID));
            parameters.put("Name", prod.mName);
            parameters.put("Price", String.valueOf(prod.mPrice));
            parameters.put("Quantity", String.valueOf(prod.mQuantity));
            URLString += ParameterStringBuilder.getParamsString(parameters);
            URL req_url = new URL(URLString);
            HttpURLConnection con = (HttpURLConnection) req_url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(content);
            in.close();
            if (content.toString().contains("Unspecified")) {
                return -1;
            } else if (content.toString().contains("Duplicate")) {
                return PRODUCT_DUPLICATE_ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }
    public int updateProductOverHttp(ProductModel prod)  {
        try {
            String URLString = "http://localhost:8080/UpdateProduct";
            Map<String, String> parameters = new LinkedHashMap<String, String>();
            parameters.put("id", String.valueOf(prod.mProductID));
            parameters.put("Name", prod.mName);
            parameters.put("Price", String.valueOf(prod.mPrice));
            parameters.put("Quantity", String.valueOf(prod.mQuantity));
            URLString += ParameterStringBuilder.getParamsString(parameters);
            URL req_url = new URL(URLString);
            HttpURLConnection con = (HttpURLConnection) req_url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(content);
            in.close();
            if (content.toString().contains("Error")) {
                return -1;
            } else if (content.toString().contains("Duplicate")) {
                return PRODUCT_DUPLICATE_ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public ProductModel getProductOverHttp(String id)  {
        ProductModel ret = new ProductModel();
        try {
            String URLString = "http://localhost:8080/Product";
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("id", id);
            URLString += ParameterStringBuilder.getParamsString(parameters);
            URL req_url = new URL(URLString);

            HttpURLConnection con = (HttpURLConnection) req_url.openConnection();
            con.setRequestMethod("GET");

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(content);
            in.close();
            if(content.toString().compareTo("No Product Loaded!") != 0) {
                String info = content.toString();
                String[] contentInfo = info.split(",");
                ret.mProductID = Integer.parseInt(contentInfo[0]);
                ret.mName = contentInfo[1];
                ret.mPrice = Double.parseDouble(contentInfo[2]);
                ret.mQuantity = Double.parseDouble(contentInfo[3]);
            } else {
                return null;
            }
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
        return ret;
    }
    public CustomerModel getCustomerOverHttp(String id) {
        return new CustomerModel();
    }
    public PurchaseModel getPurchaseOverHttp(String id) {
        return new PurchaseModel();
    }

    private static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params) throws Exception {
            StringBuilder result = new StringBuilder();
            result.append("?");

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }

    }
}
