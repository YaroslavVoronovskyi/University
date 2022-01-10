package ua.com.foxminded.university.service;

import java.util.List;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.controller.dao.AuditoryDAO;
import ua.com.foxminded.university.controller.dao.TimeTableDAO;
import ua.com.foxminded.university.model.Auditory;
import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Service
public class TimeTableServiceImpl implements TimeTableService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableServiceImpl.class);
    
    private TimeTableDAO timeTableDAO;
    private AuditoryDAO auditoryDAO;
    
    @Autowired
    public TimeTableServiceImpl(TimeTableDAO timeTableDAO, AuditoryDAO auditoryDAO) {
        this.timeTableDAO = timeTableDAO;
        this.auditoryDAO = auditoryDAO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public TimeTable getTimeTable(int id) throws UniversityServiceException {
        LOGGER.debug("Try get timeTable {}", id);
        try {
            LOGGER.debug("TimeTable was successfully got {}", id);
            return timeTableDAO.getById(id);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("TimeTable " + id + " couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TimeTable> getAllTimeTables() throws UniversityServiceException {
        LOGGER.debug("Try get all timeTables");
        try {
            LOGGER.debug("All timeTables was successfully got");
            return timeTableDAO.findAll();
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("TimeTables list couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional
    public List<TimeTable> saveTimeTable(List<TimeTable> timeTablesList) throws UniversityServiceException {
        LOGGER.debug("Try save timeTables list");
        try {
            LOGGER.debug("All timeTables was successfully saved");
            return timeTableDAO.saveAll(timeTablesList);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("TimeTables list " + timeTablesList + " couldn't be saved", exception);
        }
    }
    
    @Override
    @Transactional
    public TimeTable updateTimeTable(TimeTable timeTable) throws UniversityServiceException {
        LOGGER.debug("Try update timeTable {}", timeTable);
        try {
            LOGGER.debug("TimeTable was successfully updated {}", timeTable);
            return  timeTableDAO.save(timeTable);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("TimeTable " + timeTable + " couldn't be updated",
                    exception);
        }
    }
    
    @Override
    @Transactional
    public void delete(TimeTable timeTable) throws UniversityServiceException {
        LOGGER.debug("Try delete timeTable {}", timeTable);
        try {
            timeTableDAO.deleteById(timeTable.getId());
            LOGGER.debug("TimeTable was successfully deleted {}", timeTable);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("TimeTable " + timeTable + " couldn't be deleted", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Auditory getAuditory(int id) throws UniversityServiceException {
        LOGGER.debug("Try get auditory {}", id);
        try {
            LOGGER.debug("Auditory was successfully got {}", id);
            return auditoryDAO.getById(id);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Auditory " + id + " couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Auditory> getAllAuditories() throws UniversityServiceException {
        LOGGER.debug("Try get all auditories");
        try {
            LOGGER.debug("All auditories was successfully got");
            return auditoryDAO.findAll();
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Auditories list couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional
    public List<Auditory> saveAuditory(List<Auditory> auditoriesList) throws UniversityServiceException {
        LOGGER.debug("Try save courses list");
        try {
            LOGGER.debug("All auditories was successfully saved");
            return auditoryDAO.saveAll(auditoriesList);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Auditories list " + auditoriesList + " couldn't be saved", exception);
        }
    }
    
    @Override
    @Transactional
    public Auditory updateAuditory(Auditory auditory) throws UniversityServiceException {
        LOGGER.debug("Try update auditory {}", auditory);
        try {
            LOGGER.debug("Auditory was successfully updated {}", auditory);
            return auditoryDAO.save(auditory);            
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Auditory " + auditory + " couldn't be updated", exception);
        }
    }
    
    @Override
    @Transactional
    public void deleteAuditory(Auditory auditory) throws UniversityServiceException {
        LOGGER.debug("Try delete auditory {}", auditory);
        try {
            auditoryDAO.deleteById(auditory.getId());
            LOGGER.debug("Auditory was successfully deleted {}", auditory);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Auditory " + auditory + " couldn't be deleted", exception);
        }
    }
    
    /**
    @Override
    public TimeTable updateAuditoryInTimeTamle(Auditory auditory, TimeTable timeTable) throws UniversityServiceException {
        LOGGER.debug("Try update auditory to timeTable {}, {}", auditory, timeTable);
        try {
            timeTableDAO.updateAuditoryInTimeTable(auditory, timeTable);
            LOGGER.debug("Auditory was successfully update in timeTable {}, {}", auditory, timeTable);
            return timeTable;
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Auditory couldn't be updated in TimeTable", exception);
        }
    }
    
    @Override
    public TimeTable addAuditoryToTimeTable() throws UniversityServiceException {
        LOGGER.debug("Try add auditory to timeTable");
        try {
            TimeTable timeTable = null;
            List<Auditory> auditoriesList = auditoryDAO.getAll();
            List<TimeTable> timeTablesList = timeTableDAO.getAll();
            for (Auditory auditory : auditoriesList) {
                for (int index = 0; index < auditoriesList.size(); index++) {
                    timeTable = TimeTable.builder().id(timeTablesList.get(index).getId())
                            .startTime(timeTablesList.get(index).getStartTime())
                            .endTime(timeTablesList.get(index).getEndTime())
                            .date(timeTablesList.get(index).getDate()).auditory(auditory).build();
                    timeTableDAO.updateAuditoryInTimeTable(auditory, timeTable);
                }
            }
            LOGGER.debug("Auditory was successfully added to timeTable");
            return timeTable;
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Auditory couldn't be added to TimeTable", exception);
        }
    }
    
    @Override
    public TimeTable deleteAuditoryFromTimeTamle(Auditory auditory, TimeTable timeTable) throws UniversityServiceException {
        LOGGER.debug("Try delete auditory from timeTable {}, {}", auditory, timeTable);
        try {
            LOGGER.debug("Auditory was successfully deleted from timeTable {}, {}", auditory, timeTable);
            return timeTableDAO.deleteAuditoryFromTimeTable(auditory, timeTable);
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Auditory  " + auditory + " couldn't be deleted from TimeTable " + timeTable, exception);
        }
    }
    */

    @Override
    public TimeTable updateAuditoryInTimeTamle(Auditory auditory, TimeTable timeTable)
            throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TimeTable addAuditoryToTimeTable() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TimeTable deleteAuditoryFromTimeTamle(Auditory auditory, TimeTable timeTable)
            throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }
}
