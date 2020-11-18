package com.github.amolrbhagwat.ddiff2;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Index {

	private HashMap<String, ArrayList<IndexedFile>> index;

	Index(File directory) throws IOException {
		index = new HashMap<String, ArrayList<IndexedFile>>();

		Files.walk(Paths.get(directory.toURI()))
				.filter(p -> Files.isRegularFile(p))
				.map(p -> p.toFile())
				.forEach(f -> {
					String filename = f.getName(); 
					if(!index.containsKey(filename)) {
						index.put(filename, new ArrayList<IndexedFile>());
					}
					index.get(filename).add(new IndexedFile(f));
				});
	}

	public ArrayList<String> directoriesContainingFile(String filename) {
		ArrayList<String> directories = new ArrayList<String>();
		
		if(index.containsKey(filename)) {
			for(IndexedFile file : index.get(filename)) {
				directories.add(file.getFileDirectory());
			}
		}
		return directories;
	}

	public ArrayList<IndexedFile> getIndexedFiles(String filename) {
		if (index.containsKey(filename))
		    return index.get(filename);
		else
			return new ArrayList<IndexedFile>();
	}

}
