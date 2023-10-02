package com.es.core.model.phone;

import com.es.core.enums.SortField;
import com.es.core.enums.SortOrder;
import com.es.core.model.phone.color.Color;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Resource
    private PhonesExtractor phonesExtractor;

    private static final String GET_QUERY = "SELECT ph.*, colors.code AS color " +
            "FROM (SELECT phones.* FROM phones WHERE phones.id = ?) AS ph " +
            "LEFT JOIN phone2color ON ph.id = phone2color.phoneId " +
            "LEFT JOIN colors ON phone2color.colorId = colors.id";
    private static final String SAVE_INSERT_QUERY = "INSERT INTO phones (Id, Brand, Model, Price, DisplaySizeInches, " +
            "WeightGr, LengthMm, WidthMm, HeightMm, Announced, DeviceType, Os, DisplayResolution, PixelDensity, " +
            "DisplayTechnology, BackCameraMegapixels, FrontCameraMegapixels, RamGb, InternalStorageGb, BatteryCapacityMah, " +
            "TalkTimeHours, StandByTimeHours, Bluetooth, Positioning, ImageUrl, Description) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SIMPLE_FIND_ALL_QUERY = "select ph.* " +
            "from (select PHONES.* from PHONES " +
            "left join STOCKS on PHONES.ID = STOCKS.PHONEID where STOCKS.STOCK - STOCKS.RESERVED > 0 offset ? limit ?) ph";
    private static final String FIND_WITHOUT_OFFSET_AND_LIMIT = "SELECT ph.* " +
            "FROM (SELECT phones.* FROM phones " +
            "LEFT JOIN stocks ON phones.id = stocks.phoneId WHERE stocks.stock - stocks.reserved > 0 ";
    private static final String NUMBER_OF_PHONES_QUERY = "SELECT count(*) FROM PHONES LEFT JOIN STOCKS ON PHONES.ID = STOCKS.PHONEID WHERE STOCKS.STOCK - STOCKS.RESERVED > 0";
    private static final String SELECT_COLOR_BY_CODE_QUERY = "SELECT colors.id FROM colors WHERE colors.code = ?";
    private static final String INSERT_NEW_COLOR_DEPENDENCE_QUERY = "INSERT INTO phone2color (phoneId, colorId) VALUES (?, ?)";

    @Override
    public Optional<Phone> get(final Long key) {
        return jdbcTemplate.query(GET_QUERY, phonesExtractor, key).stream().findAny();
    }

    @Override
    public void save(final Phone phone) {
        jdbcTemplate.update(SAVE_INSERT_QUERY,
                phone.getId(), phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getDisplaySizeInches(),
                phone.getWeightGr(), phone.getLengthMm(), phone.getWidthMm(), phone.getHeightMm(),
                phone.getAnnounced(), phone.getDeviceType(), phone.getOs(), phone.getDisplayResolution(),
                phone.getPixelDensity(), phone.getDisplayTechnology(), phone.getBackCameraMegapixels(),
                phone.getFrontCameraMegapixels(), phone.getRamGb(), phone.getInternalStorageGb(),
                phone.getBatteryCapacityMah(), phone.getTalkTimeHours(), phone.getStandByTimeHours(),
                phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(), phone.getDescription());

        phone.getColors().stream()
                .forEach(color -> {
                    Long colorId = jdbcTemplate.queryForObject(SELECT_COLOR_BY_CODE_QUERY, Long.class, color.getCode());
                    jdbcTemplate.update(INSERT_NEW_COLOR_DEPENDENCE_QUERY, phone.getId(), colorId);
                });
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
        return jdbcTemplate.query(makeFindAllSQL(sortField, sortOrder, query), phonesExtractor, offset, limit);
    }

    private String makeFindAllSQL(SortField sortField, SortOrder sortOrder, String query) {
        if (sortField != null || query != null && !query.equals("")) {
            String sql = FIND_WITHOUT_OFFSET_AND_LIMIT;

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