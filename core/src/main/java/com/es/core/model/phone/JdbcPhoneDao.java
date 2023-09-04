package com.es.core.model.phone;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String PHONES_AND_COLORS_QUERY = "SELECT phones.id, phones.brand, phones.model, phones.price, " +
            "phones.imageUrl, colors.code, phone2color.colorId " +
            "FROM phones " +
            "JOIN phone2color ON phones.id=phone2color.phoneId " +
            "JOIN colors ON phone2color.colorId=colors.id";
    private static final String PHONES_AND_COLORS_QUERY_WITH_LIMIT_AND_OFFSET = PHONES_AND_COLORS_QUERY + " offset ? limit ?";
    private static final String INSERT_INTO_PHONES2COLORS = "INSERT INTO phone2color VALUES (?, ?)";
    private static final String DELETE_FROM_PHONES2COLORS = "DELETE FROM phone2color WHERE colorId = ? and phoneId = ?";
    private static final String UPDATE_PHONE_PRICE = "UPDATE phones SET price = ?";
    private static final String INSERT_NEW_PHONE = "INSERT INTO phones (brand, model, price, imageUrl) values (?, ?, ?, ?)";

    public Optional<Phone> get(final Long key) {
        return getPhoneListWithColors().stream()
                .filter(phone -> phone.getId().equals(key))
                .findAny();
    }

    private List<Phone> getPhoneListWithColors() {
        return jdbcTemplate.query(PHONES_AND_COLORS_QUERY, new PhoneExtractor());
    }

    public void save(final Phone phone) {
        List<Phone> phoneList = getPhoneListWithColors();
        if (phoneList.contains(phone)) {
            jdbcTemplate.update(UPDATE_PHONE_PRICE, phone.getPrice());
            int index = phoneList.indexOf(phone);
            updateColors(phone, phoneList.get(index));
        } else {
            jdbcTemplate.update(INSERT_NEW_PHONE, phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getImageUrl());
            insertColorsIfExists(phone);

        }
    }

    private void insertColorsIfExists(Phone phone) {
        for (Color currentColor : phone.getColors()) {
            jdbcTemplate.update(INSERT_INTO_PHONES2COLORS, phone.getId(), currentColor.getId());
        }
    }

    private void insertNewColors(Phone savedPhone, Phone currentPhone) {
        for (Color currentColor : currentPhone.getColors()) {
            if (!savedPhone.getColors().contains(currentColor)) {
                jdbcTemplate.update(INSERT_INTO_PHONES2COLORS, currentPhone.getId(), currentColor.getId());
            }
        }
    }

    private void deleteUnusedColors(Phone savedPhone, Phone currentPhone) {
        for (Color currentColor : savedPhone.getColors()) {
            if (!currentPhone.getColors().contains(currentColor)) {
                jdbcTemplate.update(DELETE_FROM_PHONES2COLORS, currentColor.getId(), currentPhone.getId());
            }
        }
    }

    private void updateColors(Phone currentPhone, Phone savedPhone) {
        deleteUnusedColors(savedPhone, currentPhone);
        insertNewColors(savedPhone, currentPhone);
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(PHONES_AND_COLORS_QUERY_WITH_LIMIT_AND_OFFSET, new Object[]{offset, limit}, new PhoneExtractor());
    }
}
