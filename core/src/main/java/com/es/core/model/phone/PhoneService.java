package com.es.core.model.phone;

import com.es.core.model.exceptions.PhoneNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneService {
    @Resource
    private JdbcPhoneDao phoneDao;

    public Phone get(final Long key) throws PhoneNotFoundException {
        Optional<Phone> phone = phoneDao.get(key);
        if (phone.isPresent()) {
            return phone.get();
        } else {
            throw new PhoneNotFoundException();
        }
    }

    public void save(final Phone phone) {
        phoneDao.save(phone);
    }

    public List<Phone> findAll(int offset, int limit) {
        return phoneDao.findAll(offset, limit);
    }
}
