package com.es.core.model.phone;

import com.es.core.model.phone.color.Color;
import com.es.core.model.phone.color.ColorDao;
import com.es.core.model.phone.color.ColorExtractor;
import com.es.core.model.phone.color.JdbcColorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PhonesExtractor implements ResultSetExtractor<List<Phone>> {
    @Autowired
    private ColorDao colorDao;

    @Override
    public List<Phone> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Phone> phones = new ArrayList<>();

        while (resultSet.next()) {
            Phone phone = new Phone();
            phone.setId(resultSet.getLong("ID"));
            phone.setBrand(resultSet.getString("BRAND"));
            phone.setModel(resultSet.getString("MODEL"));
            phone.setPrice(resultSet.getBigDecimal("PRICE"));
            phone.setDisplaySizeInches(resultSet.getBigDecimal("DISPLAYSIZEINCHES"));
            phone.setWeightGr(resultSet.getInt("WEIGHTGR"));
            phone.setLengthMm(resultSet.getBigDecimal("LENGTHMM"));
            phone.setWidthMm(resultSet.getBigDecimal("WIDTHMM"));
            phone.setHeightMm(resultSet.getBigDecimal("HEIGHTMM"));
            phone.setAnnounced(resultSet.getDate("ANNOUNCED"));
            phone.setDeviceType(resultSet.getString("DEVICETYPE"));
            phone.setOs(resultSet.getString("OS"));
            phone.setDisplayResolution(resultSet.getString("DISPLAYRESOLUTION"));
            phone.setPixelDensity(resultSet.getInt("PIXELDENSITY"));
            phone.setDisplayTechnology(resultSet.getString("DISPLAYTECHNOLOGY"));
            phone.setBackCameraMegapixels(resultSet.getBigDecimal("BACKCAMERAMEGAPIXELS"));
            phone.setFrontCameraMegapixels(resultSet.getBigDecimal("FRONTCAMERAMEGAPIXELS"));
            phone.setRamGb(resultSet.getBigDecimal("RAMGB"));
            phone.setInternalStorageGb(resultSet.getBigDecimal("INTERNALSTORAGEGB"));
            phone.setBatteryCapacityMah(resultSet.getInt("BATTERYCAPACITYMAH"));
            phone.setTalkTimeHours(resultSet.getBigDecimal("TALKTIMEHOURS"));
            phone.setStandByTimeHours(resultSet.getBigDecimal("STANDBYTIMEHOURS"));
            phone.setBluetooth(resultSet.getString("BLUETOOTH"));
            phone.setPositioning(resultSet.getString("POSITIONING"));
            phone.setImageUrl(resultSet.getString("IMAGEURL"));
            phone.setDescription(resultSet.getString("DESCRIPTION"));
            List<Color> colorList = colorDao.getColors(phone.getId());
            Set<Color> colors;
            if (colorList == null) {
                colors = null;
            } else {
                colors = new HashSet<>(colorList);
            }
            phone.setColors(colors);
            phones.add(phone);
        }

        return phones;
    }
}
