package it.rics.query;


import it.rics.app.Argomento;

public class Queries {
  public static final String GET_MATERIE = "SELECT NomeMateria FROM Materia";

  public static String GET_ARGOMENTI(String  materia){
    return "SELECT Argomento.NomeArgomento,Argomento.Descrizione " +
            "FROM Materia LEFT JOIN Argomento ON Materia.ID_Materia = Argomento.ID_Materia  " +
            "WHERE Materia.NomeMateria = '" + materia +"'";
  }
  public static String ADD_MATERIA(String m){
    return "INSERT INTO Materia (ID_Materia, NomeMateria)" +
            " VALUES (NULL , '" + m +"')";
  }

  public static String ADD_ARGOMENTO(Argomento a){
    return "INSERT INTO Argomento ( NomeArgomento, Descrizione, ID_Materia)" +
            "\nSELECT '" + a.getNomeArgomento() +"','" + a.getRiferimento() +"', ID_Materia" +
            "\nFROM Materia" +
            "\nWHERE NomeMateria = '" + a.getMateria() +"'";
  }

  public static String  ID_MATERIA(String materia){
    return "SELECT ID_Materia FROM Materia WHERE NomeMateria = '" + materia + "'";
  }

  public static String REMOVE_MATERIA(String materia){
    return "DELETE FROM Materia WHERE NomeMateria = '" + materia +"'";
  }

  public static String REMOVE_ARGOMENTO(Argomento Arg){
    return "DELETE FROM Argomento WHERE NomeArgomento = '" + Arg.getNomeArgomento() +"'";
  }
}
