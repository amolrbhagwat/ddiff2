package com.github.amolrbhagwat.ddiff2;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import com.github.amolrbhagwat.ddiff2.DirectoryDiffer.DiffType;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class App extends Application
{
	@FXML Button checkButton;
	@FXML TextField sourceDirTextView;
	@FXML TextField targetDirTextView;

	@FXML TableView<DiffResult> resultsTableView;
	@FXML TableColumn<DiffResult, String> filenameColumn;
	@FXML TableColumn<DiffResult, String> sourcePathColumn;
	@FXML TableColumn<DiffResult, String> statusColumn;
	@FXML TableColumn<DiffResult, String> commentColumn;
	
	@FXML ChoiceBox<DiffType> diffTypeChoiceBox;
	
	@FXML TextField statusTextField;
	
	@FXML ToggleButton noRescanButton;

	Stage stage;

	File sourceDirectory;
	File targetDirectory;

	private ObservableList<DiffResult> diffResults;
	
	Index index = null;

	@FXML
	public void diff(ActionEvent event) throws IOException {
		var startTime = Instant.now();
		
		if(index == null || !noRescanButton.isSelected()) {
			index = new Index(targetDirectory);
		}

		diffResults.setAll(DirectoryDiffer.diff(sourceDirectory, index, diffTypeChoiceBox.getValue()));
		
		var endTime = Instant.now();
		var timeTaken = Duration.between(startTime, endTime);
		
		statusTextField.setText(String.format("Time taken: %d:%02d:%02d", timeTaken.toHours(),
															timeTaken.toMinutesPart(),
															timeTaken.toSecondsPart()));
		
		noRescanButton.setDisable(false);
	}

	@FXML
    private void initialize() {
		filenameColumn.setCellValueFactory(cellData ->
			cellData.getValue().filenameProperty());
		sourcePathColumn.setCellValueFactory(cellData ->
			cellData.getValue().sourcePathProperty());
		statusColumn.setCellValueFactory(cellData ->
			cellData.getValue().statusProperty());
		commentColumn.setCellValueFactory(cellData ->
			cellData.getValue().commentProperty());

		diffResults = FXCollections.observableArrayList();
		resultsTableView.setItems(diffResults);
		
		diffTypeChoiceBox.getItems().setAll(DiffType.values());
		diffTypeChoiceBox.setValue(DiffType.FilenameOnly);
    }

	public ObservableList<DiffResult> getDiffResults() {
        return diffResults;
    }

	@FXML
	public void setSourceDirectory(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(sourceDirectory);

		File selectedDirectory = directoryChooser.showDialog(stage);

		if(selectedDirectory != null) {
			sourceDirectory = selectedDirectory;
			sourceDirTextView.setText(sourceDirectory.toString());
		}
	}

	@FXML
	public void setTargetDirectory(ActionEvent event) throws IOException {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(targetDirectory);

		File selectedDirectory = directoryChooser.showDialog(stage);

		if(selectedDirectory != null) {
			targetDirectory = selectedDirectory;
			targetDirTextView.setText(targetDirectory.toString());
			noRescanButton.setSelected(false);
			noRescanButton.setDisable(true);
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
		primaryStage.setTitle("Ddiff2");
		primaryStage.setScene(scene);
		primaryStage.show();

		stage = primaryStage;
	}
}
