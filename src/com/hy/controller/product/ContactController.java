package com.hy.controller.product;

import com.hy.controller.base.BaseController;
import java.sql.DriverManager;
import java.sql.SQLException;

@org.springframework.stereotype.Controller
@org.springframework.web.bind.annotation.RequestMapping({"/contant"})
public class ContactController extends BaseController
{
  public static void main(String[] args) throws SQLException
  {
    try
    {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      java.sql.Connection con = DriverManager.getConnection(
        "jdbc:sqlserver://192.168.1.100/tro", "root", 
        "admin000");
      java.sql.ResultSet rs_cgp = null;
      java.sql.Statement statement_cgp = con.createStatement();
      String sql_cgp = "INSERT INTO Midorders ()VALUE();";
      rs_cgp = statement_cgp.executeQuery(sql_cgp);
      //System.out.println("Connection Successful!");
      rs_cgp.close();
      statement_cgp.close();
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}


