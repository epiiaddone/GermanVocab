
import static model.Datasource.CONNECTION_STRING;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Datasource;
import model.Word;



  public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
      Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
      primaryStage.setTitle("German Vocabulary");
      primaryStage.setScene(new Scene(root, 700, 500));
      primaryStage.show();
    }

    public static void main(String[] args) {
      launch(args);
    }


}