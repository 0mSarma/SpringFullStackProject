//this class contains methods for handling file uploads, saving files, cleaning directories, and removing directories.
package com.shopom.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException{ //This method is used to save an uploaded file to a specified directory.
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);  //creating file direrctories
		} 
		
		try (InputStream inputStream = multipartFile.getInputStream()){
			Path filePath= uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName, ex);
		}
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException ex) {
						LOGGER.error("Could not delete file: " + file);
						//System.out.println("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException ex) {
			LOGGER.error("could not list directory: " + dirPath);
			//System.out.println("could not list directory: " + dirPath);
		}
	}

	public static void removeDir(String categoryDir) {
		// TODO Auto-generated method stub
		cleanDir(categoryDir);
		
		try {
			Files.delete(Paths.get(categoryDir));
		} catch (IOException e) {
			LOGGER.error("Could not remove directory: " + categoryDir);
		}
	}
}
