package com.github.amolrbhagwat.ddiff2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import com.github.amolrbhagwat.ddiff2.DiffResult.DiffStatus;
import com.github.amolrbhagwat.ddiff2.DirectoryDiffer.DiffType;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class DirectoryDifferTest extends TestCase {
	
	public final String TEST_DIR_NAME = ".testroot";
	private File srcDir;
	private File targetDir;
	
    public static Test suite()
    {
        return new TestSuite( DirectoryDifferTest.class );
    }

	protected void setUp() throws Exception {
		super.setUp();
		
		new File(TEST_DIR_NAME).mkdirs();
		
		srcDir = new File(Paths.get(TEST_DIR_NAME, "source").toString());
    	targetDir = new File(Paths.get(TEST_DIR_NAME, "target").toString());
    	
    	srcDir.mkdirs();
    	targetDir.mkdirs();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		try {
			FileUtils.deleteDirectory(new File(TEST_DIR_NAME));
		}
		catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", TEST_DIR_NAME);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", TEST_DIR_NAME);
		} catch (IOException x) {
		    // File permission problems are caught here.
		    System.err.println(x);
		}
		
	}
   
    public void test_filenameOnlyDiff_emptySourceEmptyTarget_returnsEmptyResult() throws IOException
    {
    	assertEquals(DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.FilenameOnly).size(), 0);  	
    }

    public void test_filenameOnly_fileNotPresentInTarget_returnsError() throws IOException
    {    	
    	createFile(Paths.get(srcDir.getPath(), "a").toString(), "Doesn't matter");
    	
    	var results = DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.FilenameOnly); 
    	
    	assertEquals(1, results.size());
    	assertEquals(DiffStatus.ERROR.toString(), results.get(0).getStatus());    	
    }
    
    public void test_filenameOnly_filePresentInTargetInOneDirectory_returnsSuccess() throws IOException
    {
        createFile(Paths.get(srcDir.getPath(), "a").toString(), "foo");
        createFile(Paths.get(targetDir.getPath(), "a").toString(), "foo");
    	
    	var results = DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.FilenameOnly); 
    	
    	assertEquals(1, results.size());
    	assertEquals(DiffStatus.SUCCESS.toString(), results.get(0).getStatus());
    }
    
    public void test_filenameOnly_filePresentInTargetInMoreThanOneDirectory_returnsWarning() throws IOException
    {
    	createFile(Paths.get(srcDir.getPath(), "a").toString(), "foo");
        createFile(Paths.get(targetDir.getPath(), "a").toString(), "foo");
        createFile(Paths.get(targetDir.getPath(), "subdirectory", "a").toString(), "foo");
    	
    	var results = DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.FilenameOnly); 
    	
    	assertEquals(1, results.size());
    	assertEquals(DiffStatus.WARNING.toString(), results.get(0).getStatus());
    }
    
    public void test_checksumAll_fileNotPresentInTarget_returnsError() throws IOException
    {
    	createFile(Paths.get(srcDir.getPath(), "a").toString(), "foo");
            	
    	var results = DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.ChecksumForAll); 
    	
    	assertEquals(1, results.size());
    	assertEquals(DiffStatus.ERROR.toString(), results.get(0).getStatus());
    }
    
    public void test_checksumAll_filePresentOnceInTargetSameLevelSameContent_returnsSuccess() throws IOException
    {
    	createFile(Paths.get(srcDir.getPath(), "a").toString(), "foo");
    	createFile(Paths.get(targetDir.getPath(), "a").toString(), "foo");
    	
    	var results = DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.ChecksumForAll); 
    	
    	assertEquals(1, results.size());
    	assertEquals(DiffStatus.SUCCESS.toString(), results.get(0).getStatus());
    }
    
    public void test_checksumAll_filePresentOnceInTargetSameLevelDifferentContent_returnsError() throws IOException
    {
    	createFile(Paths.get(srcDir.getPath(), "a").toString(), "foo");
    	createFile(Paths.get(targetDir.getPath(), "a").toString(), "bar");
    	
    	var results = DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.ChecksumForAll); 
    	
    	assertEquals(1, results.size());
    	assertEquals(DiffStatus.ERROR.toString(), results.get(0).getStatus());
    }
    
    public void test_checksumAll_filePresentManyTimesInTargetAtLeastOneMatchesContent_returnsSuccess() throws IOException
    {
    	createFile(Paths.get(srcDir.getPath(), "a").toString(), "foo");
    	createFile(Paths.get(targetDir.getPath(), "a").toString(), "bar");
    	createFile(Paths.get(targetDir.getPath(), "folder1", "a").toString(), "baz");
    	createFile(Paths.get(targetDir.getPath(), "folder2", "a").toString(), "foo");
    	
    	var results = DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.ChecksumForAll); 
    	
    	assertEquals(1, results.size());
    	assertEquals(DiffStatus.SUCCESS.toString(), results.get(0).getStatus());
    }
    
    public void test_checksumAll_filePresentManyTimesInTargetNoneMatchContent_returnsError() throws IOException
    {
    	createFile(Paths.get(srcDir.getPath(), "a").toString(), "foo");
    	createFile(Paths.get(targetDir.getPath(), "a").toString(), "rat");
    	createFile(Paths.get(targetDir.getPath(), "folder1", "a").toString(), "cat");
    	createFile(Paths.get(targetDir.getPath(), "folder2", "a").toString(), "mat");
    	
    	var results = DirectoryDiffer.diff(srcDir, new Index(targetDir), DiffType.ChecksumForAll); 
    	
    	assertEquals(1, results.size());
    	assertEquals(DiffStatus.ERROR.toString(), results.get(0).getStatus());
    }
    
    private void createFile(String fullFilenameString, String content) throws IOException
    {
    	File file = new File(fullFilenameString);
    	file.getParentFile().mkdirs();
    	file.createNewFile();
    	
    	FileWriter fw = new FileWriter(fullFilenameString);
    	fw.write(content);
    	fw.close();
    }
    
}
