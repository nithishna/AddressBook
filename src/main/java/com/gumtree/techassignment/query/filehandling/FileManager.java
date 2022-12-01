package com.gumtree.techassignment.query.filehandling;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private final Map<String, FileLoader> loaders = new HashMap<>();
    public void addLoader(FileLoader loader, String fileExtension) {
        loaders.put(fileExtension, loader);
    }

    public FileLoader load(String fileName) throws IOException {
        int index = fileName.lastIndexOf(".");
        String fileExtension = "default";
        if(index != -1)
        {
            fileExtension = fileName.substring(index + 1);
        }
        FileLoader loader = loaders.get(fileExtension);
        try {
            if(loader == null) throw new IllegalArgumentException("Bad Data");
            return loader.load(fileName);
        } catch(IOException ex) {
            throw new IOException(String.format("Failed to load %s", fileName), ex);
        }
    }
}
