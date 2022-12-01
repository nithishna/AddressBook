package com.gumtree.techassignment.query.filehandling;

import com.gumtree.techassignment.query.constants.Gender;
import com.gumtree.techassignment.query.dto.Person;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class PersonTextFileLoader extends TextFileLoader {

    private final Map<String, List<Integer>> index = new HashMap<>();
    private final Map<String, Long> genderCounter = new HashMap<>();
    private Person oldestPerson = null;
    private String fileName = null;
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.UK);

    public PersonTextFileLoader() {}
    public PersonTextFileLoader(FileLoader pFileLoader, String fileName) throws IOException {
        this.fileName = fileName;
        pFileLoader.accept(new FileSource() {
            @Override
            public void populateIndex() throws IOException {
                int counter=1;

                //Used BufferedReader because if the AddressBook file grows larger (like 10GB) then it is best to not load the entire file in-memory avoiding Heap memory leaks
                try(BufferedReader in = new BufferedReader(new FileReader(ResourceUtils.getFile(String.format("classpath:%s", fileName))))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        String[] personDetails = line.split(",");
                        if(personDetails.length == 3) {
                            String name[] = personDetails[0].split("\\s+");
                            //Maintaining an index for the first name to access those data quickly at run time
                            //If the file size is expected to be small then all the data could be maintained in-memory or a distributed cache like memcache
                            index.computeIfAbsent(name[0], k -> new ArrayList<>()).add(counter);
                            genderCounter.merge(personDetails[1].trim(), 1l, Long::sum);
                            try {
                                Date currentPersonDOB = dateFormat.parse(personDetails[2]);
                                if (oldestPerson == null) {
                                    oldestPerson = new Person(name[0], personDetails[0].substring(name[0].length()), Gender.getFromVal(personDetails[1].trim()), currentPersonDOB);
                                } else if(oldestPerson.getDateOfBirth().after(currentPersonDOB)){
                                    oldestPerson = new Person(name[0], personDetails[0].substring(name[0].length()), Gender.getFromVal(personDetails[1].trim()), currentPersonDOB);
                                }
                            } catch (ParseException e) {
                                //notify about this failure to support team with line number
                            }
                        } else {
                            //notify about this entry to support team with line number
                        }
                        counter++;
                    }
                } catch(IOException ex){
                    throw ex;
                }
            }
        });
    }

    @Override
    public void accept(FileSource pSource) throws IOException {
        pSource.populateIndex();
    }

    @Override
    public Map<String, List<Integer>> getIndex() {
        return this.index;
    }

    @Override
    public FileLoader load(String fileName) throws IOException {
        return new PersonTextFileLoader(this, fileName);
    }

    /*
    * Based on the index computed on load of the application we can easily get the person details by first name
    * */
    public Person findPersonByLineNumber(long lineNumber) throws IOException, ParseException {

        try (Stream<String> lines = Files.lines(ResourceUtils.getFile(String.format("classpath:%s", fileName)).toPath())) {
            String line = lines.skip(lineNumber).findFirst().get();
            String[] personDetails = line.split(",");
            Date dob = dateFormat.parse(personDetails[2]);
            String name[] = personDetails[0].split("\\s+");
            return new Person(name[0], personDetails[0].substring(name[0].length()), Gender.getFromVal(personDetails[1].trim()), dob);
        }
        catch(IOException e){
            throw e;
        } catch (ParseException e) {
            throw e;
        }
    }

    public long getCountByGender(String value) {
        return this.genderCounter.getOrDefault(value, 0l);
    }

    public Person getOldestPerson() {
        return this.oldestPerson;
    }
}