<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CNT 4714 Remote Database Management System</title>
        <link rel="stylesheet" type="text/css" href="Index.css">
    </head>
    <body>
        <h1>Welcome to the Project 4 Remote Database Management System</h1>
        <hr />
        <p>You are connected to the Project4 database.</p>
        <p>Please enter any valid SQL query or update statement.</p>
        <p>If no query/update command is given the Execute button will display all supplier information in the database.</p>
        <p>All execution results will appear below.</p>
        <form method="post" action="./Project4Controller">
            <textarea name="text_area"></textarea>    
              <div>
                <input class="buttons" type="submit" name="execute" value="Execute Command" />
                <input class ="buttons" type ="reset" name ="clear" value ="Reset"/>
                </div>

        </form>
        <hr />
        <p>Database Results:</p>
        <%
            if (request.getAttribute("rs") != null) {
                ResultSet result = (ResultSet) request.getAttribute("rs");
                ResultSetMetaData metaData = result.getMetaData();

                String buffer = "<table border='1'>";
                buffer += "<tr>";

                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    buffer += "<th>" + metaData.getColumnName(i + 1) + "</th>";
                }

                buffer += "</tr>";

                while (result.next()) {
                    buffer += "<tr>";

                    for (int i = 0; i < metaData.getColumnCount(); i++) {
                        buffer += "<td>" + result.getObject(i + 1) + "</td>";
                    }

                    buffer += "</tr>";
                }

                buffer += "</table>";
                out.println(buffer);
            }

            if (request.getAttribute("rows_affected") != null) {
                String buffer = "<table border='1'>";
                buffer += "<th style='background:green'>";
                buffer += "<p>The statement executed successfully.<br />" + request.getAttribute("rows_affected") + " row(s) affected.</p>";

                if (request.getAttribute("business_logic_rows_affected") != null) {
                    buffer += "<p>Business Logic Detected! - Updating Supplier Status</p>";
                    buffer += "<p>Business Logic updated " + request.getAttribute("business_logic_rows_affected") + " supplier status marks.</p>";
                }

                buffer += "</th>";
                buffer += "</table>";
                out.println(buffer);
            }

            if (request.getAttribute("error_message") != null) {
                String buffer = "<table border='1'>";
                buffer += "<tr><th style='background:red'>Error executing the SQL statement:<br />" + request.getAttribute("error_message") + "</th></tr>";
                buffer += "</table>";
                out.println(buffer);
            }
        %>
    </body>
</html>
