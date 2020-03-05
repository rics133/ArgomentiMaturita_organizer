package it.rics.query;

import it.rics.app.Materia;

public class Queries {
  public static final String GET_MATERIE = "SELECT * FROM Materia";
  public static String GET_ARGOMENTI(String  materia){
    return "SELECT Argomento.ID_Argomento,Argomento.NomeArgomento,Argomento.Descrizione FROM Materia LEFT JOIN Argomento ON Materia.ID_Materia = Argomento.ID_Materia  WHERE Materia.NomeMateria = '" +materia +"'";
  }
  public static final String ADD_MATERIA(Materia m){
    return "INSERT INTO Materia (ID_Materia, NomeMateria) VALUES (null , '" + m.getNomeMateria() +"')";
  }
}
