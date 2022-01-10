package ua.com.foxminded.university.controller.exception;

import javax.persistence.PersistenceException;

public class UniversityDaoException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public UniversityDaoException(String errorMessage) {
        super(errorMessage);
    }
    
    public UniversityDaoException(String errorMessage, PersistenceException exception) {
        super(errorMessage, exception);
    }
}
