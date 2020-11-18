package com.github.amolrbhagwat.ddiff2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.amolrbhagwat.ddiff2.DiffResult.DiffStatus;

public class DirectoryDiffer {

	public enum DiffType {
		FilenameOnly,
		ChecksumIfMultiple,
		ChecksumForAll
	}

	public static ArrayList<DiffResult> diff(File sourceDirectory, Index index, DiffType diffType) throws IOException {
		switch (diffType) {
		case FilenameOnly:
			return filenameOnlyDiff(sourceDirectory, index);
		case ChecksumIfMultiple:
		case ChecksumForAll:
			return checksumForAllDiff(sourceDirectory, index);
		default:
			return new ArrayList<DiffResult>();
		}
	}

	private static ArrayList<DiffResult> filenameOnlyDiff(File sourceDirectory, Index index) throws IOException {
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
				stringBuffer.append("File present at: ");
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
	
	private static ArrayList<DiffResult> checksumForAllDiff(File sourceDirectory, Index index) throws IOException {
		ArrayList<DiffResult> results = new ArrayList<DiffResult>();
		
		try {
		Files.walk(Paths.get(sourceDirectory.toURI()))
		.filter(p -> Files.isRegularFile(p))
		.forEach(p -> {
			File file = p.toFile();
			String filename = file.getName();
			ArrayList<IndexedFile> allIndexedFiles = index.getIndexedFiles(filename);
			
			DiffStatus status = DiffStatus.ERROR;
			StringBuffer stringBuffer = new StringBuffer("");
			
			String sourceFileDigest = new IndexedFile(file).getChecksum(); // TODO: Fix this hack!
			
			for (IndexedFile indexedFile : allIndexedFiles) {
				if (indexedFile.getChecksum().equals(sourceFileDigest)) {
					status = DiffStatus.SUCCESS;
					stringBuffer.append("File present at:");
					stringBuffer.append(indexedFile.getFileDirectory());
					break;
				}
			}
			
			if (status == DiffStatus.ERROR)
			{
				stringBuffer.append("File not present in the target directory.");
			}
			
			results.add(new DiffResult(filename, file.getPath(), status, stringBuffer.toString()));
		});
		
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return results;
	}

}
