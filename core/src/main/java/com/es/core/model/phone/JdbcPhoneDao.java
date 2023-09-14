package com.es.core.model.phone;

import com.es.core.enums.SortField;
import com.es.core.enums.SortOrder;
import com.es.core.model.phone.color.Color;
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
    private final String SELECT_PHONE_BY_ID = "SELECT * FROM phones WHERE id = ?";

    private final String SELECT_PHONE_WITH_CLR_BY_ID = "SELECT phone.*, colors.id AS colorId, colors.code AS colorCode FROM " +
            "(" + SELECT_PHONE_BY_ID + ") AS phone LEFT JOIN phone2color " +
            "ON phone.id = phone2color.phoneId" +
            " LEFT JOIN colors ON phone2color.colorId = colors.id";
    ;
    private static final String SIMPLE_FIND_ALL_QUERY = "select ph.* " +
            "from (select PHONES.* from PHONES " +
            "join STOCKS on PHONES.ID = STOCKS.PHONEID where STOCKS.STOCK - STOCKS.RESERVED > 0 offset ? limit ?) ph";
    private static final String NUMBER_OF_PHONES_QUERY = "SELECT count(*) FROM PHONES JOIN STOCKS ON PHONES.ID = STOCKS.PHONEID WHERE STOCKS.STOCK - STOCKS.RESERVED > 0";
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

    @Override
    public Long numberByQuery(String query) {
        if (query == null || query.equals(""))
            return jdbcTemplate.queryForObject(NUMBER_OF_PHONES_QUERY, Long.class);
        return jdbcTemplate.queryForObject(NUMBER_OF_PHONES_QUERY + " AND " +
                "(LOWER(PHONES.BRAND) LIKE LOWER('" + query + "%') " +
                "OR LOWER(PHONES.BRAND) LIKE LOWER('% " + query + "%') " +
                "OR LOWER(PHONES.MODEL) LIKE LOWER('" + query + "%') " +
                "OR LOWER(PHONES.MODEL) LIKE LOWER('% " + query + "%'))", Long.class);
    }

    @Override
    public List<Phone> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query) {
        return jdbcTemplate.query(makeFindAllSQL(sortField, sortOrder, query), new PhonesExtractor(), offset, limit);
    }

    private String makeFindAllSQL(SortField sortField, SortOrder sortOrder, String query) {
        if (sortField != null || query != null && !query.equals("")) {
            String sql = "SELECT ph.* from (select PHONES.* from PHONES " +
                    "join STOCKS on PHONES.ID = STOCKS.PHONEID where STOCKS.STOCK - STOCKS.RESERVED > 0 ";

            if (query != null && !query.equals("")) {
                sql += "AND (" +
                        "LOWER(PHONES.BRAND) LIKE LOWER('" + query + "%') " +
                        "OR LOWER(PHONES.BRAND) LIKE LOWER('% " + query + "%') " +
                        "OR LOWER(PHONES.MODEL) LIKE LOWER('" + query + "%') " +
                        "OR LOWER(PHONES.MODEL) LIKE LOWER('% " + query + "%')" +
                        ") ";
            }

            if (sortField != null) {
                sql += "ORDER BY " + sortField.name() + " ";
                if (sortOrder != null) {
                    sql += sortOrder.name() + " ";
                } else {
                    sql += "ASC ";
                }
            }

            sql += "offset ? limit ?) ph";
            return sql;
        } else {
            return SIMPLE_FIND_ALL_QUERY;
        }
    }
}