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
      database.connect("Maturita.accdb");
      ResultSet r = database.query("SELECT NomeMateria FROM Materia");
      while (r.next()){
        System.out.println("risultato: " + r.getString(1));
      }
    } catch ( SQLException | NotSanitizedException e)
    {
      e.printStackTrace();
    }
  }
}
