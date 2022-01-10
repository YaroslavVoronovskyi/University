package ua.com.foxminded.university.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertiesDataLoader {
    private static final String PROPERTIES = "application.properties";
    
    public  String getProperty(String propertyName) throws IOException {
        String propertyValue;
        Properties properties = new Properties();
        try (FileInputStream inputStream = getFileFromResurces(PROPERTIES)) {
            properties.load(inputStream);
            propertyValue = properties.getProperty(propertyName);
        } catch (IOException exception) {
            throw new IOException("IOException exeption in getProperty()", exception);
        }
        return propertyValue;
    }
    
    private FileInputStream getFileFromResurces(String file) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(file);
        if (url == null) {
            throw new IllegalArgumentException("File not foud");
        } else {
            return new FileInputStream(url.getFile());
        }
    } 
}
