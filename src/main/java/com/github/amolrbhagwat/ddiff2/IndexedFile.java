package com.github.amolrbhagwat.ddiff2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;

public class IndexedFile {

	private File file;
	private String md5Digest;
	
	public IndexedFile(File f) {
		this.file = f;
	}
	
	public String getChecksum() {
		if(md5Digest != null) {
			return md5Digest;
		}
		
		try (InputStream is = Files.newInputStream(Paths.get(file.getPath()))) {
		    try {
		    	md5Digest = DigestUtils.md5Hex(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return md5Digest;
	}
	
	public String getFilename() {
		return file.getName();
	}
	
	public String getFileDirectory() {
		return file.getParent();
	}
}
