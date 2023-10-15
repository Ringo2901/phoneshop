package com.es.core.model.phone;

import com.es.core.enums.SortField;
import com.es.core.enums.SortOrder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private PhonesExtractor phonesExtractor;

    private static final String GET_QUERY = "SELECT ph.*, colors.code AS color " +
            "FROM (SELECT phones.* FROM phones WHERE phones.id = :id) AS ph " +
            "LEFT JOIN phone2color ON ph.id = phone2color.phoneId " +
            "LEFT JOIN colors ON phone2color.colorId = colors.id";
    private static final String SAVE_INSERT_QUERY = "INSERT INTO phones (Id, Brand, Model, Price, DisplaySizeInches, " +
            "WeightGr, LengthMm, WidthMm, HeightMm, Announced, DeviceType, Os, DisplayResolution, PixelDensity, " +
            "DisplayTechnology, BackCameraMegapixels, FrontCameraMegapixels, RamGb, InternalStorageGb, BatteryCapacityMah, " +
            "TalkTimeHours, StandByTimeHours, Bluetooth, Positioning, ImageUrl, Description) " +
            "VALUES (:id, :brand, :model, :price, :displaySizeInches, :weightGr, :lengthMm, :widthMm, :heightMm, " +
            ":announced, :deviceType, :os, :displayResolution, :pixelDensity, :displayTechnology, :backCameraMegapixels, " +
            ":frontCameraMegapixels, :ramGb, :internalStorageGb, :batteryCapacityMah, :talkTimeHours, :standByTimeHours, " +
            ":bluetooth, :positioning, :imageUrl, :description)";
    private static final String SIMPLE_FIND_ALL_QUERY = "select ph.* " +
            "from (select PHONES.* from PHONES " +
            "left join STOCKS on PHONES.ID = STOCKS.PHONEID where STOCKS.STOCK - STOCKS.RESERVED > 0 offset :offset limit :limit) ph";
    private static final String FIND_WITHOUT_OFFSET_AND_LIMIT = "SELECT ph.* " +
            "FROM (SELECT phones.* FROM phones " +
            "LEFT JOIN stocks ON phones.id = stocks.phoneId WHERE stocks.stock - stocks.reserved > 0 ";
    private static final String NUMBER_OF_PHONES_QUERY = "SELECT count(*) FROM PHONES LEFT JOIN STOCKS ON PHONES.ID = STOCKS.PHONEID WHERE STOCKS.STOCK - STOCKS.RESERVED > 0";
    private static final String SELECT_COLOR_BY_CODE_QUERY = "SELECT colors.id FROM colors WHERE colors.code = :code";
    private static final String INSERT_NEW_COLOR_DEPENDENCE_QUERY = "INSERT INTO phone2color (phoneId, colorId) VALUES (:phoneId, :colorId)";

    @Override
    public Optional<Phone> get(final Long key) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", key);
        return namedParameterJdbcTemplate.query(GET_QUERY, paramMap, phonesExtractor).stream().findAny();
    }

    @Override
    public void save(final Phone phone) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", phone.getId());
        paramMap.put("brand", phone.getBrand());
        paramMap.put("model", phone.getModel());
        paramMap.put("price", phone.getPrice());
        paramMap.put("displaySizeInches", phone.getDisplaySizeInches());
        paramMap.put("weightGr", phone.getWeightGr());
        paramMap.put("lengthMm", phone.getLengthMm());
        paramMap.put("widthMm", phone.getWidthMm());
        paramMap.put("heightMm", phone.getHeightMm());
        paramMap.put("announced", phone.getAnnounced());
        paramMap.put("deviceType", phone.getDeviceType());
        paramMap.put("os", phone.getOs());
        paramMap.put("displayResolution", phone.getDisplayResolution());
        paramMap.put("pixelDensity", phone.getPixelDensity());
        paramMap.put("displayTechnology", phone.getDisplayTechnology());
        paramMap.put("backCameraMegapixels", phone.getBackCameraMegapixels());
        paramMap.put("frontCameraMegapixels", phone.getFrontCameraMegapixels());
        paramMap.put("ramGb", phone.getRamGb());
        paramMap.put("internalStorageGb", phone.getInternalStorageGb());
        paramMap.put("batteryCapacityMah", phone.getBatteryCapacityMah());
        paramMap.put("talkTimeHours", phone.getTalkTimeHours());
        paramMap.put("standByTimeHours", phone.getStandByTimeHours());
        paramMap.put("bluetooth", phone.getBluetooth());
        paramMap.put("positioning", phone.getPositioning());
        paramMap.put("imageUrl", phone.getImageUrl());
        paramMap.put("description", phone.getDescription());

        namedParameterJdbcTemplate.update(SAVE_INSERT_QUERY, paramMap);

        phone.getColors().forEach(color -> {
            paramMap.clear();
            paramMap.put("code", color.getCode());
            Long colorId = namedParameterJdbcTemplate.queryForObject(SELECT_COLOR_BY_CODE_QUERY, paramMap, Long.class);
            paramMap.clear();
            paramMap.put("phoneId", phone.getId());
            paramMap.put("colorId", colorId);
            namedParameterJdbcTemplate.update(INSERT_NEW_COLOR_DEPENDENCE_QUERY, paramMap);
        });
    }

    @Override
    public Long numberByQuery(String query) {
        if (query == null || query.equals(""))
            return namedParameterJdbcTemplate.queryForObject(NUMBER_OF_PHONES_QUERY, new HashMap<>(), Long.class);
        else {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("query", query);
            return namedParameterJdbcTemplate.queryForObject(NUMBER_OF_PHONES_QUERY + " AND " +
                    "(LOWER(PHONES.BRAND) LIKE LOWER(:query) " +
                    "OR LOWER(PHONES.BRAND) LIKE LOWER('% ' || :query) " +
                    "OR LOWER(PHONES.MODEL) LIKE LOWER(:query) " +
                    "OR LOWER(PHONES.MODEL) LIKE LOWER('% ' || :query))", paramMap, Long.class);
        }
    }

    @Override
    public List<Phone> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("offset", offset);
        paramMap.put("limit", limit);

        return namedParameterJdbcTemplate.query(makeFindAllSQL(sortField, sortOrder, query), paramMap, phonesExtractor);
    }

    private String makeFindAllSQL(SortField sortField, SortOrder sortOrder, String query) {
        if (sortField != null || query != null && !query.equals("")) {
            String sql = FIND_WITHOUT_OFFSET_AND_LIMIT;

            if (query != null && !query.equals("")) {
                sql += "AND (" +
                        "LOWER(PHONES.BRAND) LIKE LOWER(:query) " +
                        "OR LOWER(PHONES.BRAND) LIKE LOWER('% ' || :query) " +
                        "OR LOWER(PHONES.MODEL) LIKE LOWER(:query) " +
                        "OR LOWER(PHONES.MODEL) LIKE LOWER('% ' || :query)" +
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

            sql += "offset :offset limit :limit) ph";
            return sql;
        } else {
            return SIMPLE_FIND_ALL_QUERY;
        }
    }
}
