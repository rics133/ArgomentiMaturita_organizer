import it.rics.dbcomunication.Database;
import it.rics.dbcomunication.DatabaseBuilder;
import it.rics.exceptions.NotSanitizedException;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTest {
  public static void main(String[] args) {
    Database database = DatabaseBuilder.newDatabase()
            .withUsername("")
            .withPassword("")
            .build();
    try {
      database.connect("file.mdb");
      ResultSet r = database.query("SELECT * FROM Prova");
      while (r.next()){
        System.out.println("risultato: " + r.getInt(1));
      }
    } catch ( SQLException | NotSanitizedException e) {
      e.printStackTrace();
    }
  }
}
