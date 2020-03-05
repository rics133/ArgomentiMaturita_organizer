package it.rics.dbcomunication;

import it.rics.exceptions.NotSanitizedException;

import java.io.File;
import java.sql.*;
import java.util.Arrays;

public class Database {


  private Connection connection;
  private String Username;
  private String Password;

  public Database() { }

  public String getUsername() {
    return Username;
  }

  public void setUsername(String username) {
    Username = username;
  }

  public String getPassword() {
    return Password;
  }

  public void setPassword(String password) {
    Password = password;
  }

  public void connect(String filename) throws  SQLException {
    String path = getClass().getClassLoader().getResource(filename).getFile();
    System.out.println("path to Access db: " + path );
    String database ="jdbc:ucanaccess:/"+path;
    connection = DriverManager.getConnection(database, Username, Password);
  }

  public boolean isConnected(){
    if(connection != null){
      try {
        if(!connection.isClosed()){
          return true;
        }
      } catch (SQLException e) {
        return false;
      }
    }
    return false;
  }

  public ResultSet query(String qr) throws SQLException, NotSanitizedException {

    Statement st = connection.createStatement();
    if (isSanitized(qr)) {
      ResultSet r = st.executeQuery(qr);
      st.close();
      return r;
    }
    st.close();
    throw new NotSanitizedException();

  }

  public int update(String qr) throws SQLException, NotSanitizedException {

    Statement st = connection.createStatement();
    if(isSanitized(qr)) {
      int r =  st.executeUpdate(qr);
      st.close();
      return r;
    }
    st.close();
    throw new NotSanitizedException();

  }

  private boolean isSanitized(String qr) {
    if(qr.contains("--") || qr.contains("/*") || qr.contains(";"))
      return false;
    return true;
  }

  public void close() throws SQLException {
    connection.close();
  }
}
