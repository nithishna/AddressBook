package com.gumtree.techassignment.query.configuration;

import com.gumtree.techassignment.query.filehandling.FileManager;
import com.gumtree.techassignment.query.filehandling.PersonTextFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Configuration
public class FileConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(FileConfiguration.class);

    @Value("${person.filename: AddressBook}")
    private String personTextFileName;

    @Bean
    public FileManager fileManager() {
        FileManager fileManager = new FileManager();
        fileManager.addLoader(new PersonTextFileLoader(), "default");
        return fileManager;
    }

    @Bean
    public PersonTextFileLoader personTextFileLoader() throws IOException {
        return (PersonTextFileLoader)fileManager().load(personTextFileName);
    }

    @Bean(name="firstNameIndex")
    public Map<String, List<Integer>> fileIndex() throws IOException {
        return personTextFileLoader().getIndex();
    }
}
