package com.github.amolrbhagwat.ddiff2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DiffResult {
	
	enum DiffStatus {
		SUCCESS,
		WARNING,
		ERROR
	}
	
	private final StringProperty filename;
	private final StringProperty sourcePath;
	private final StringProperty status;
	private final StringProperty comment;
	
	DiffResult(String filename, String sourcePath, DiffStatus status, String comment) {
		this.filename = new SimpleStringProperty(filename);
		this.sourcePath = new SimpleStringProperty(sourcePath);
		this.status = new SimpleStringProperty(status.toString());
		this.comment = new SimpleStringProperty(comment);
	}
	
	public StringProperty filenameProperty() {
		return filename;
	}

	public StringProperty sourcePathProperty() {
		return sourcePath;
	}

	public StringProperty statusProperty() {
		return status;
	}

	public StringProperty commentProperty() {
		return comment;
	}
	
	public String getFilename() {
		return filename.get();
	}

	public String getSourcePath() {
		return sourcePath.get();
	}

	public String getStatus() {
		return status.get();
	}

	public String getComment() {
		return comment.get();
	}
}
