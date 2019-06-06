package com.github.amolrbhagwat.ddiff2;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class App extends Application
{
	@FXML Button checkButton;
	@FXML TextField sourceDirTextView;
	@FXML TextField targetDirTextView;

	@FXML TableView<DiffResult> resultsTableView;
	@FXML TableColumn<DiffResult, String> filenameColumn;
	@FXML TableColumn<DiffResult, String> statusColumn;
	@FXML TableColumn<DiffResult, String> commentColumn;

	Stage stage;

	File sourceDirectory;
	File targetDirectory;

	private ObservableList<DiffResult> diffResults;

	@FXML
	public void diff(ActionEvent event) throws IOException {
		Index index = new Index(targetDirectory);

		diffResults.setAll(DirectoryDiffer.diff(sourceDirectory, index));
	}

	@FXML
    private void initialize() {
		filenameColumn.setCellValueFactory(cellData ->
			cellData.getValue().filenameProperty());
		statusColumn.setCellValueFactory(cellData ->
			cellData.getValue().statusProperty());
		commentColumn.setCellValueFactory(cellData ->
			cellData.getValue().commentProperty());

		diffResults = FXCollections.observableArrayList();
		resultsTableView.setItems(diffResults);
    }

	public ObservableList<DiffResult> getDiffResults() {
        return diffResults;
    }

	@FXML
	public void setSourceDirectory(ActionEvent event) {
		File selectedDirectory = new DirectoryChooser().showDialog(stage);

		if(selectedDirectory != null) {
			sourceDirectory = selectedDirectory;
			sourceDirTextView.setText(sourceDirectory.toString());
		}
	}

	@FXML
	public void setTargetDirectory(ActionEvent event) throws IOException {
		File selectedDirectory = new DirectoryChooser().showDialog(stage);

		if(selectedDirectory != null) {
			targetDirectory = selectedDirectory;
			targetDirTextView.setText(targetDirectory.toString());
		}
	}

    public static void main(String[] args) {
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
