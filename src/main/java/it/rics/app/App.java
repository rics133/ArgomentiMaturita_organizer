package it.rics.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception{
    Parent root;
    root = FXMLLoader.load(getClass().getClassLoader().getResource("App.fxml"));
    primaryStage.setTitle("Maturita 2019-2020");
    primaryStage.setScene(new Scene(root, 600, 600));
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.show();
  }
}
