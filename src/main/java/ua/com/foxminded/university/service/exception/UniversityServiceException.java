package ua.com.foxminded.university.service.exception;

import javax.persistence.PersistenceException;

public class UniversityServiceException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public UniversityServiceException(String errorMessage) {
        super(errorMessage);
    }
 
    public UniversityServiceException(String errorMessage, PersistenceException exception) {
        super(errorMessage, exception);
    }  
}
