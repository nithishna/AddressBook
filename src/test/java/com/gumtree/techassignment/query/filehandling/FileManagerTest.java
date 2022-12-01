package com.gumtree.techassignment.query.filehandling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileManagerTest {

    @InjectMocks
    private FileManager fileManager;

    private final CSVFileLoader csvFileLoader = new CSVFileLoader();
    private final XMLFileLoader xmlFileLoader = new XMLFileLoader();
    private final TextFileLoader personTextFileLoader = new PersonTextFileLoader();

    @BeforeEach
    public void setup() {
        fileManager.addLoader(csvFileLoader, "csv");
        fileManager.addLoader(xmlFileLoader, "xml");
        fileManager.addLoader(personTextFileLoader, "default");
    }

    @Test
    public void testLoaderSuccess() throws IOException {
        FileLoader csvloader = fileManager.load("test.csv");
        assertEquals(csvloader, csvFileLoader);
        FileLoader xmlLoader = fileManager.load("test.xml");
        assertEquals(xmlLoader, xmlFileLoader);
        FileLoader personTextFileLoader = fileManager.load("AddressBook");
        assertEquals(personTextFileLoader.getIndex().size(), 5);
    }

    @Test
    public void testLoaderIOException() {
        try {
            FileLoader personTextFileLoader = fileManager.load("TelephoneBook");
            fail("Should throw IOException");
        }catch(IOException ex) {
        }catch(Exception ex){
            fail("Different exception is thrown - IOException is expected. Got : " + ex.getClass().getName());
        }
    }

    @Test
    public void testIllegalArgumentException() {
        try {
            FileLoader personTextFileLoader = fileManager.load("TelephoneBook.xls");
            fail("Should throw IllegalArgumentException");
        }catch(IllegalArgumentException ex) {
        }catch(Exception ex){
            fail("Different exception is thrown - IllegalArgumentException is expected. Got : " + ex.getClass().getName());
        }
    }

    class CSVFileLoader implements FileLoader {

        @Override
        public void accept(FileSource pSource) {

        }

        @Override
        public Map<String, List<Integer>> getIndex() {
            return null;
        }

        @Override
        public FileLoader load(String fileName) throws IOException {
            return csvFileLoader;
        }
    }

    class XMLFileLoader implements FileLoader {

        @Override
        public void accept(FileSource pSource) {

        }

        @Override
        public Map<String, List<Integer>> getIndex() {
            return null;
        }

        @Override
        public FileLoader load(String fileName) throws IOException {
            return xmlFileLoader;
        }
    }
}