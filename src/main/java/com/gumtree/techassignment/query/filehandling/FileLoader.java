package com.gumtree.techassignment.query.filehandling;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileLoader {
    void accept(FileSource pSource) throws IOException;
    Map<String, List<Integer>> getIndex();
    FileLoader load(String fileName) throws IOException;
}

