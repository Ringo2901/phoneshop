import com.es.core.model.phone.color.Color;
import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.Phone;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:resources/context/test-applicationContext.xml")
public class JdbcPhoneDaoTest {

    @Resource
    private JdbcPhoneDao jdbcPhoneDao;

    @Resource
    private DataSource dataSource;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/resources/context/test-applicationContext.xml");
        dataSource = (DataSource) applicationContext.getBean("dataSource");
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
    }

    @After
    public void tearDown() {
        JdbcTestUtils.dropTables(jdbcTemplate, "colors", "phones");
    }

    @Test
    public void notEmptyDataBaseWhenPhoneDaoTestFindAll() {
              assertFalse(jdbcPhoneDao.findAll(0,5, null,null,null).isEmpty());
    }

    @Test
    public void emptyDataBaseWhenPhoneDaoTestFindAll() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "colors", "phones");
        assertTrue(jdbcPhoneDao.findAll(0, 5,null,null,null).isEmpty());
    }

    @Test
    public void getShouldReturnCorrectPhoneById() {
        Phone expectedPhone = getPhone();
        Phone actualParameterPhone = jdbcPhoneDao.get(1000L).get();
        assertEquals(expectedPhone, actualParameterPhone);
    }

    @Test
    public void getShouldReturnEmptyOptionalForNonExistingPhone() {
        Optional<Phone> phone = jdbcPhoneDao.get(9999L);
        assertFalse(phone.isPresent());
    }

    private static Phone getPhone() {
        Phone actualParameterPhone = new Phone();
        Color color = new Color();
        color.setId(1000L);
        color.setCode("Black");
        Set<Color> colors = new HashSet<>();
        colors.add(color);
        color.setId(1001L);
        color.setCode("White");
        colors.add(color);
        actualParameterPhone.setId(1000L);
        actualParameterPhone.setModel("ARCHOS 101 G9");
        actualParameterPhone.setPrice(null);
        actualParameterPhone.setBrand("ARCHOS");
        actualParameterPhone.setImageUrl(null);
        actualParameterPhone.setColors(colors);
        return actualParameterPhone;
    }

    @Test
    public void savePhoneWithExistingId() {
        Phone savingPhone = new Phone();

        savingPhone.setId(1002L);
        savingPhone.setModel("ZTE Z6");
        savingPhone.setPrice(BigDecimal.TEN);
        savingPhone.setBrand("ZTE");
        savingPhone.setImageUrl("xxx.jpg");
        savingPhone.getColors().add(new Color(1001L, "White"));

        jdbcPhoneDao.save(savingPhone);
        Phone foundPhoneAfterSaving = jdbcPhoneDao.get(1002L).get();

        Assert.assertEquals(savingPhone, foundPhoneAfterSaving);
        assertEquals(savingPhone.getColors(), foundPhoneAfterSaving.getColors());

    }
}
