package com.es.core.model.phone;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final String SELECT_PHONE_BY_ID = "SELECT * FROM phones WHERE id = ?";

    private final String SELECT_PHONE_WITH_CLR_BY_ID = "SELECT phone.*, colors.id AS colorId, colors.code AS colorCode FROM " +
            "(" + SELECT_PHONE_BY_ID + ") AS phone LEFT JOIN phone2color " +
            "ON phone.id = phone2color.phoneId" +
            " LEFT JOIN colors ON phone2color.colorId = colors.id";
    ;
    private static final String PHONES_AND_COLORS_QUERY_WITH_LIMIT_AND_OFFSET = PHONES_AND_COLORS_QUERY + " offset ? limit ?";

    private static final String INSERT_PHONE = "INSERT INTO phones (brand, model, price, imageUrl) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_PHONE = "UPDATE phones SET brand = ?, model = ?, price = ?, imageUrl = ? WHERE id = ?";
    private static final String DELETE_COLORS = "DELETE FROM phone2color WHERE phoneId = ?";
    private static final String INSERT_COLORS = "INSERT INTO phone2color (phoneId, colorId) VALUES (?, ?)";


    public Optional<Phone> get(final Long key) {
        return jdbcTemplate.query(SELECT_PHONE_WITH_CLR_BY_ID, new Object[]{key}, new PhonesExtractor()).stream().findAny();
    }
    @Transactional
    public void save(final Phone phone) {
        if (phone.getId() == null) {
            insertPhone(phone);
        } else {
            updatePhone(phone);
        }
        updatePhoneColors(phone);
    }

    private void insertPhone(Phone phone) {
        jdbcTemplate.update(INSERT_PHONE, phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getImageUrl());
    }

    private void updatePhone(Phone phone) {
        jdbcTemplate.update(UPDATE_PHONE, phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getImageUrl(), phone.getId());
    }

    private void updatePhoneColors(Phone phone) {
        jdbcTemplate.update(DELETE_COLORS, phone.getId());
        for (Color color : phone.getColors()) {
            jdbcTemplate.update(INSERT_COLORS, phone.getId(), color.getId());
        }
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(PHONES_AND_COLORS_QUERY_WITH_LIMIT_AND_OFFSET, new Object[]{offset, limit}, new PhonesExtractor());
    }
}
