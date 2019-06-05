package com.github.amolrbhagwat.ddiff2;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Index {
	
	private HashMap<String, ArrayList<String>> index;

	Index(File directory) throws IOException {
		index = new HashMap<String, ArrayList<String>>();

		Files.walk(Paths.get(directory.toURI()))
				.filter(p -> Files.isRegularFile(p))
				.map(p -> p.toFile())
				.forEach(f -> {
					String filename = f.getName(); 
					if(!index.containsKey(filename)) {
						index.put(filename, new ArrayList<String>());
					}
					index.get(filename).add(f.getParent());
				});
	}
	
	public ArrayList<String> directoriesContainingFile(String filename) {
		if(index.containsKey(filename)) {
			return index.get(filename);
		}
		return new ArrayList<String>();
	}

}
