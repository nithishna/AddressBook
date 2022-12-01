package com.gumtree.techassignment.query.filehandling;

import java.io.IOException;

public interface FileSource {
    default void populateIndex() throws IOException {}
}
