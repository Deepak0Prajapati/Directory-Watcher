package com.dencofamily.paycom.brain;

import java.io.File;
import java.util.*;

public class DirectoryWatcher {
    private static final String DIRECTORY_PATH = "C:\\Users\\Support\\Desktop\\sheets\\testing"; // Change this to your directory path

    public static void main(String[] args) {
        File directory = new File(DIRECTORY_PATH);
        
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Invalid directory path.");
            return;
        }

        System.out.println("Scanning directory....: " + directory.getAbsolutePath());
        
        Thread watcherThread = new Thread(() -> {
            Map<String, Long> currentFiles = getCurrentFiles(directory);
            
            while (true) {
                try {
                    Thread.sleep(5000); // Check every 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                Map<String, Long> newFiles = getCurrentFiles(directory);
                
                for (String fileName : newFiles.keySet()) {
                    if (!currentFiles.containsKey(fileName)) {
                    	System.out.println("working");
                        System.out.println("New file detected: " + fileName);
                        Thread thread = new Thread(new FileProcessor(directory.getAbsolutePath() + "/" + fileName));
                        thread.start();
                    }
                }
                
                currentFiles = newFiles;
            }
        });

        watcherThread.start();
    }

    private static Map<String, Long> getCurrentFiles(File directory) {
        Map<String, Long> filesMap = new HashMap<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                filesMap.put(file.getName(), file.lastModified());
            }
        }
        return filesMap;
    }
}

class FileProcessor implements Runnable {
    private final String filePath;

    public FileProcessor(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        // Perform processing on the file
    	System.out.println("sending file");
    	CSVFormatter2 csvFormatter=new CSVFormatter2(filePath);
    	Thread thread=new Thread(csvFormatter);
    	thread.start();
        System.out.println("we are Processing file: " + filePath);
        // Implement your logic to process the file here
    }
}
