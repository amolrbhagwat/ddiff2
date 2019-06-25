package com.github.amolrbhagwat.ddiff2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.github.amolrbhagwat.ddiff2.DiffResult.DiffStatus;

public class DirectoryDiffer {
	
	public static ArrayList<DiffResult> diff(File sourceDirectory, Index index) throws IOException {
		ArrayList<DiffResult> results = new ArrayList<DiffResult>();
		
		Files.walk(Paths.get(sourceDirectory.toURI()))
		.filter(p -> Files.isRegularFile(p))
		.forEach(p -> {
			File file = p.toFile();
			String filename = file.getName();
			ArrayList<String> directories = index.directoriesContainingFile(filename);
			
			DiffStatus status;
			StringBuffer stringBuffer = new StringBuffer("");
			switch (directories.size()) {
			case 0:
				status = DiffStatus.ERROR;
				stringBuffer.append("File not present in the target directory.");
				break;
			case 1:
				status = DiffStatus.SUCCESS;
				stringBuffer.append("File present at:");
				stringBuffer.append(" ");
				stringBuffer.append(directories.get(0));
				break;
			default:
				status = DiffStatus.WARNING;
				stringBuffer.append("File present at:");
				for(String dir : directories) {
					stringBuffer.append(" ");
					stringBuffer.append(dir);
				}
				break;
			}
			
			results.add(new DiffResult(filename, file.getPath(), status, stringBuffer.toString()));
		});
		
		return results;
	}

}
