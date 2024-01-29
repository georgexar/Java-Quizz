package net.quizz.utils;

import java.io.File;
import java.io.IOException;

public class FileWrapper {

	private final String name;
	private final String path;
	private final File file;
	
	public FileWrapper(String path, String name) {
		this.path = path;
		this.name = name;
		
		File folder = new File(path);
		if(!folder.exists()) folder.mkdirs();
		this.file = new File(path, name);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException ignored) {}
		}
	}
	
	public void read() {}
	public void save() {}
	
	public String getName() { return name; }
	public String getPath() { return path; }
	public File getFile() { return file; }
	
}
