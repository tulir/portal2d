package net.maunium.Portal2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NativeLoader {
	public static void load() {
		File natives = new File(System.getProperty("user.home") + "/.portal2d/natives");
		File download = new File(natives, "download.zip");
		if (!natives.exists()) {
			natives.mkdirs();
			NativeLoader.download("http://dl.maunium.net/portal2d-natives.zip", download.getAbsolutePath());
			NativeLoader.unzip(download.getAbsolutePath(), natives.getAbsolutePath());
			download.delete();
		}
		System.setProperty("org.lwjgl.librarypath", natives.getAbsolutePath());
	}
	
	public static void unzip(String zipFile, String outputFolder) {
		byte[] buffer = new byte[1024];
		
		try {
			
			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			
			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			
			while (ze != null) {
				
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);
				
				System.out.println("file unzip : " + newFile.getAbsoluteFile());
				
				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();
				
				FileOutputStream fos = new FileOutputStream(newFile);
				
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				
				fos.close();
				ze = zis.getNextEntry();
			}
			
			zis.closeEntry();
			zis.close();
			
			System.out.println("Done");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static boolean download(String urlStr, String saveIn) {
		try {
			// Get a connection to the URL and start up a buffered reader.
			long startTime = System.currentTimeMillis();
			
			System.out.println("[Util_Internet] " + "Connecting...");
			
			URL url = new URL(urlStr);
			url.openConnection();
			InputStream reader = url.openStream();
			
			// Setup a buffered file writer to write out what we read from the website.
			
			FileOutputStream writer = new FileOutputStream(saveIn);
			byte[] buffer = new byte[153600];
			int totalBytesRead = 0;
			int bytesRead = 0;
			
			System.out.println("[Util_Internet] " + "Reading file 150KB blocks at a time.\n");
			
			while ((bytesRead = reader.read(buffer)) > 0) {
				writer.write(buffer, 0, bytesRead);
				buffer = new byte[153600];
				totalBytesRead += bytesRead;
			}
			
			long endTime = System.currentTimeMillis();
			
			System.out.println("[Util_Internet] " + "Done. " + new Integer(totalBytesRead).toString() + " bytes read ("
					+ new Long(endTime - startTime).toString() + " millseconds).\n");
			writer.close();
			reader.close();
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
