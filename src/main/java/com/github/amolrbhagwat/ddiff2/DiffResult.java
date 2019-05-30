package com.github.amolrbhagwat.ddiff2;

public class DiffResult {
	
	enum DiffStatus {
		SUCCESS,
		WARNING,
		ERROR
	}
	
	private String filename;
	private DiffStatus status;
	private String comment;
	
	DiffResult(String filename, DiffStatus status, String comment) {
		this.filename = filename;
		this.status = status;
		this.comment = comment;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public DiffStatus getStatus() {
		return status;
	}
	
	public String getComment() {
		return comment;
	}

}
