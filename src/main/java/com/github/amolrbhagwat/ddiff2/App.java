package com.github.amolrbhagwat.ddiff2;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class App extends Application
{
	@FXML Button checkButton;
	@FXML TextField sourceDirTextView;
	@FXML TextField targetDirTextView;

	Stage stage;

	File sourceDirectory;
	File targetDirectory;

	@FXML
	public void setSourceDirectory(ActionEvent event) {
		File selectedDirectory = new DirectoryChooser().showDialog(stage);

		if(selectedDirectory != null) {
			sourceDirectory = selectedDirectory;
			sourceDirTextView.setText(sourceDirectory.toString());
		}
	}

	@FXML
	public void setTargetDirectory(ActionEvent event) {
		File selectedDirectory = new DirectoryChooser().showDialog(stage);

		if(selectedDirectory != null) {
			targetDirectory = selectedDirectory;
			targetDirTextView.setText(targetDirectory.toString());
		}
	}

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
