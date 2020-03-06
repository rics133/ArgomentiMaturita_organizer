package it.rics.app;

import it.rics.dbcomunication.Database;
import it.rics.dbcomunication.DatabaseBuilder;
import it.rics.exceptions.NoResultsException;
import it.rics.exceptions.NotSanitizedException;
import it.rics.query.Queries;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class controller {
  public ListView List;
  public Label Title;
  public Button button;
  public Button Aggiungi;
  public Button Removebut;
  private Database database;
  private final String DATABASE_FILE_NAME = "Maturita.accdb";
  private ArrayList<String> materie;
  private ArrayList<Argomento> args;
  private String currentMateria;

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
      currentMateria = (String) List.getSelectionModel().getSelectedItem();
      prepareArguments();
    }
    else{
      if (Desktop.isDesktopSupported()) {
        if(args.size()>0) {
          int i = 0;
          for (; i < args.size(); i++) {
            if (args.get(i).getNomeArgomento().equals(List.getSelectionModel().getSelectedItem())) break;
          }
          try {
            Desktop.getDesktop().open(new File(args.get(i).getRiferimento()));
          } catch (IOException e) {
            Error(e.getLocalizedMessage(), false);
          }
        }
      }
    }
  }

  public void onButtonClick(ActionEvent actionEvent) {
    prepareMaterie();
  }

  public void onAddClick(ActionEvent actionEvent) {

    if(Title.getText().equals("Materie")){
      try {
        String Materia = showMateriaDialog();
        addMateria(Materia);
      } catch (NoResultsException e) {
        new Alert(Alert.AlertType.INFORMATION,"Nessuna Materia inserita").showAndWait();
      } catch (NotSanitizedException | SQLException e) {
        Error(e.getLocalizedMessage(),false);
      }
    }
    else{
      try {
        Argomento argomento = showArgumentDialog(currentMateria);
        addArgomento(argomento);
      } catch (NoResultsException e) {
        new Alert(Alert.AlertType.INFORMATION,"Nessuna Materia inserita").showAndWait();
      } catch (NotSanitizedException | SQLException e) {
        Error(e.getLocalizedMessage(),false);
      }
    }

  }

  private void addArgomento(Argomento argomento) throws SQLException, NotSanitizedException {
    if(!isInArguments(argomento)){
      args.add(argomento);
      List.getItems().add(argomento.getNomeArgomento());
      database.update(Queries.ADD_ARGOMENTO(argomento));
    }
  }

  public void onRemoveClick(ActionEvent actionEvent) {
    if(Title.getText().equals("Materie")){
      try {
        String Materia = (String) List.getSelectionModel().getSelectedItem();
        RemoveMateria(Materia);
      }catch (NotSanitizedException | SQLException e) {
        Error(e.getLocalizedMessage(),false);
      }
    }
    else{
      try {
        Argomento argomento = getArgByName((String) List.getSelectionModel().getSelectedItem());
        RemoveArgomento(argomento);
      } catch (NotSanitizedException | SQLException e) {
        Error(e.getLocalizedMessage(),false);
      }
    }
  }

  private void RemoveMateria(String m) throws NotSanitizedException, SQLException {
    for (int i = 0; i < materie.size(); i++) {
      String mat = materie.get(i);
      if (mat.equals(m)) {
        materie.remove(i);
        break;
      }
    }
    materie.remove(m);
    database.update(Queries.REMOVE_MATERIA(m));
    List.getItems().remove(m);
  }

  private void RemoveArgomento(Argomento arg) throws NotSanitizedException, SQLException {
    for (int i = 0; i <args.size(); i++) {
      Argomento argomento = args.get(i);
      if (argomento.getNomeArgomento().equals(arg.getNomeArgomento())) {
        args.remove(i);
        break;
      }
    }
    database.update(Queries.REMOVE_ARGOMENTO(arg));
    List.getItems().remove(arg.getNomeArgomento());
  }

  private Argomento getArgByName(String name){
    for (Argomento arg : args) {
      if (arg.getNomeArgomento().equals(name)) return arg;
    }
    return null;
  }
  private void prepareMaterie(){
    Title.setText("Materie");
    List.getItems().clear();
    materie.clear();
    try {
      materie = getMaterie();

      for (int i = 0; i < materie.size(); i++) {
        List.getItems().add(i, materie.get(i));
      }

    } catch (NotSanitizedException | SQLException e) {
      Error(e.getLocalizedMessage(),false);
    }
    if(materie.size()>0) Removebut.setVisible(true);
    else Removebut.setVisible(false);
    button.setVisible(false);
  }
  
  private void prepareArguments(){
    Title.setText("Argomenti");
    List.getItems().clear();
    try {
      args = getArguments(currentMateria);

      for (int i = 0; i < args.size(); i++) {
        Argomento a = args.get(i);
        List.getItems().add(i, a.getNomeArgomento());
      }

    } catch (NotSanitizedException | SQLException e) {
      Error(e.getLocalizedMessage(),false);
    }

    if(args.size()>0) Removebut.setVisible(true);
    else Removebut.setVisible(false);
    button.setVisible(true);
  }

  private ArrayList<Argomento> getArguments(String Materia) throws NotSanitizedException, SQLException {
    ArrayList<Argomento> args = new ArrayList<>();
    ResultSet r = database.query(Queries.GET_ARGOMENTI(Materia));
    while (r.next()){
      args.add(new Argomento(r.getString(1),r.getString(2), Materia));
    }
    return args;
  }

  private ArrayList<String> getMaterie() throws NotSanitizedException, SQLException {
    ArrayList<String> materie = new ArrayList<>();
    ResultSet r = database.query(Queries.GET_MATERIE);
    while (r.next()){
      materie.add(r.getString(1));
    }
    return materie;
  }

  private void addMateria(String m) throws NotSanitizedException, SQLException {
    if(!isInMaterie(m)) {
      database.update(Queries.ADD_MATERIA(m));
      materie.add(m);
      List.getItems().add(m);
    }
  }

  private boolean isInMaterie(String m) {
    for (String mat:materie) {
      if(mat.equals(m)) return true;
    }
    return false;
  }

  private boolean isInArguments(Argomento arg) {
    for (Argomento a:args) {
      if(a.getNomeArgomento().equals(arg.getNomeArgomento())) return true;
    }
    return false;
  }

  private String showMateriaDialog() throws NoResultsException {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Nuova Materia");
    dialog.setHeaderText("Inserisci i dati per la Materia");


    ButtonType ConnectButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(ConnectButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(5);
    grid.setVgap(5);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField NomeMateria = new TextField();
    NomeMateria.setPromptText("Nome");

    grid.add(new Label("Nome Materia:"), 0, 0);
    grid.add(NomeMateria, 1, 0);

    dialog.getDialogPane().setContent(grid);

    Platform.runLater(NomeMateria::requestFocus);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == ConnectButtonType) {
        return NomeMateria.getText();
      }
      return null;
    });

    Optional<String> result = dialog.showAndWait();

    if (result.isPresent()) {
      return result.get();
    }
    throw new NoResultsException();
  }

  private Argomento showArgumentDialog(String currentMateria) throws NoResultsException {
    Dialog<Argomento> dialog = new Dialog<>();
    dialog.setTitle("Nuovo Argomento");
    dialog.setHeaderText("Inserisci i dati per l'Argomento");


    ButtonType ConnectButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(ConnectButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(5);
    grid.setVgap(5);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField NomeArgomento = new TextField();
    NomeArgomento.setPromptText("Nome");
    TextField Riferimento = new TextField();
    Riferimento.setPromptText("Riferimento");

    grid.add(new Label("Nome Argomento:"), 0, 0);
    grid.add(NomeArgomento, 1, 0);
    grid.add(new Label("Percorso file:"), 0, 1);
    grid.add(Riferimento, 1, 1);

    dialog.getDialogPane().setContent(grid);

    Platform.runLater(NomeArgomento::requestFocus);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == ConnectButtonType) {
        return new Argomento(NomeArgomento.getText(),Riferimento.getText(), currentMateria);
      }
      return null;
    });

    Optional<Argomento> result = dialog.showAndWait();

    if (result.isPresent()) {
      return result.get();
    }
    throw new NoResultsException();
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
