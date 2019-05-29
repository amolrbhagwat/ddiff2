package com.github.amolrbhagwat.ddiff2;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application
{
    public static void main( String[] args )
    {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		loader.setController(this);
		Parent root = loader.load();

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		stage = primaryStage;
	}
}
