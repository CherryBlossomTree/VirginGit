/* 
 Name: Justina Mason
 Course: CNT 4714 – Spring 2017– Project Four
 Assignment title: Developing A Three-Tier Distributed Web-Based Application
 Date: April 9, 2017
 */


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Project4Controller extends HttpServlet {
    
    public String driver = "com.mysql.jdbc.Driver";
    public String db_url = "jdbc:mysql://localhost/project4";
    public String username = "root";
    public String password = "";
    public String sql_string = null;
    public String default_query = "select * from suppliers;";
    
    public Statement sql_query_statement = null;
    
    public Connection connect;
    
    public int updated_rows = 0;

  /** Get data
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BusinessLogic(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Project4Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * Post data
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BusinessLogic(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Project4Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Process the SQL execution
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    protected void BusinessLogic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException {
        try {
            try {
                Class.forName(driver).newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Project4Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            connect = DriverManager.getConnection(db_url, username, password);
            sql_string = request.getParameter("text_area");
            sql_query_statement = connect.createStatement();

            if (sql_string.toUpperCase().contains("select")) {
                sql_query_statement.executeQuery(sql_string);
                 updated_rows = sql_query_statement.getUpdateCount();

                request.setAttribute("rs", updated_rows);

            } 
            
               if ((sql_string.toUpperCase().contains("insert")) && sql_string.toUpperCase().contains("shipment")) {
                   sql_query_statement = connect.createStatement();
                   
                   updated_rows = sql_query_statement.executeUpdate(sql_string);
                   request.setAttribute("rows_affected", updated_rows);
                    
               }
               
                    
               if ((sql_string.toUpperCase().contains("update")) && sql_string.toUpperCase().contains("shipment")) {
                       sql_query_statement = connect.createStatement();
                       updated_rows = sql_query_statement.executeUpdate("UPDATE suppliers, shipments SET suppliers.status = suppliers.status + 5 WHERE suppliers.num = shipments.snum AND shipments.quantity >= 100");
                       request.setAttribute("business_logic_rows_affected", updated_rows);
                   }
               
               
               
                
               else if (!sql_string.isEmpty() && sql_string.contains("insert") || !sql_string.isEmpty() && sql_string.contains("update")) {
                //request.setAttribute("rs", sql_query_statement.executeQuery(sql_string));
                BusinessLogic(request, response);
            }
               else {
                request.setAttribute("rs", sql_query_statement.executeQuery(default_query));
            }

        } catch (SQLException e) {
            request.setAttribute("error_message", e.getMessage());
        }

        request.getRequestDispatcher("Index.jsp").forward(request, response);
    }

    
    
}
