package it.rics.app;

import it.rics.dbcomunication.Database;
import it.rics.dbcomunication.DatabaseBuilder;
import it.rics.exceptions.NotSanitizedException;
import it.rics.query.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class controller {
  public ListView List;
  public Label Title;
  public Button button;
  private Database database;
  private final String DATABASE_FILE_NAME = "Maturita.accdb";
  private ArrayList<Materia> materie;
  private ArrayList<Argomenti> args;

  @FXML
  public void initialize(){
    database = DatabaseBuilder
            .newDatabase()
            .build();
    try {
      database.connect(DATABASE_FILE_NAME);
    } catch (SQLException e) {
      Error(e.getLocalizedMessage(),false);
    }
    materie = new ArrayList<>();
    args = new ArrayList<>();

    prepareMaterie();
  }


  public void onListClicked(MouseEvent mouseEvent) {
    if(Title.getText().equals("Materie")){
      prepareArguments((String) List.getSelectionModel().getSelectedItem());
    }
    else{
      if (Desktop.isDesktopSupported()) {
        int i = 0;
        for (; i < args.size(); i++) {
          if(args.get(i).getNomeArgomento().equals(List.getSelectionModel().getSelectedItem())) break;
        }
        try {
          Desktop.getDesktop().open(new File(args.get(i).getRiferimento()));
        } catch (IOException e) {
          Error(e.getLocalizedMessage(),false);
        }
      }
    }
  }

  public void onButtonClick(ActionEvent actionEvent) {
    prepareMaterie();
  }

  public void onAddClick(ActionEvent actionEvent) {

    if(Title.getText().equals("Materie")){
      //TODO alert with materie creds
    }
    else{
      //TODO alert with args cred
    }
  }
  
  private void prepareMaterie(){
    Title.setText("Materie");
    List.getItems().clear();
    try {
      materie = getMaterie();

      for (int i = 0; i < materie.size(); i++) {
        Materia m = materie.get(i);
        List.getItems().add(i, m.getNomeMateria());
      }

    } catch (NotSanitizedException | SQLException e) {
      Error(e.getLocalizedMessage(),false);
    }

    button.setVisible(false);
  }
  
  private void prepareArguments(String Materia){
    Title.setText("Argomenti");
    List.getItems().clear();
    try {
      args = getArguments(Materia);

      for (int i = 0; i < args.size(); i++) {
        Argomenti a = args.get(i);
        List.getItems().add(i, a.getNomeArgomento());
      }

    } catch (NotSanitizedException | SQLException e) {
      Error(e.getLocalizedMessage(),false);
    }

    button.setVisible(true);
  }

  private ArrayList<Argomenti> getArguments(String Materia) throws NotSanitizedException, SQLException {
    ArrayList<Argomenti> args = new ArrayList<>();
    ResultSet r = database.query(Queries.GET_ARGOMENTI(Materia));
    while (r.next()){
      args.add(new Argomenti(r.getInt(1),r.getString(2),r.getString(3)));
    }
    return args;
  }

  private ArrayList<Materia> getMaterie() throws NotSanitizedException, SQLException {
    ArrayList<Materia> materie = new ArrayList<>();
    ResultSet r = database.query(Queries.GET_MATERIE);
    while (r.next()){
      materie.add(new Materia(r.getInt(1),r.getString(2)));
    }
    return materie;
  }

  private void addMateria(Materia m) throws NotSanitizedException, SQLException {
    if(!isInMaterie(m)) {
      database.query(Queries.ADD_MATERIA(m));
      materie.add(m);
    }
  }

  private boolean isInMaterie(Materia m) {
    for (Materia mat:materie) {
      if(mat.equals(m)) return true;
    }
    return false;
  }

  private void Error(String mex, boolean exit){
    new Alert(Alert.AlertType.ERROR,mex, ButtonType.OK).showAndWait();
    if(exit) quit();
  }
  
  @FXML
  private void quit(){
    if(database.isConnected()) {
      try {
        database.close();
      } catch (SQLException e) {
        System.exit(-1);
      }
    }
    System.exit(0);
  }
}
