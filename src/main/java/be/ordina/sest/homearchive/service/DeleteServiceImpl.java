package be.ordina.sest.homearchive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ordina.sest.homearchive.dao.MongoDao;

@Service
public class DeleteServiceImpl implements DeleteService {

    @Autowired
    private MongoDao mongoDao;

    @Override
    public void deleteDocument(final String id) {
        mongoDao.deleteDocument(id);
    }
}
