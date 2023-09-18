import com.es.core.model.phone.color.Color;
import com.es.core.model.phone.color.ColorDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:resources/context/test-applicationContext.xml")
public class JdbcColorDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ColorDao colorDao;

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
    public void testGetColorsWithValidParam() {
        Long validPhoneId = 1001L;
        List<Color> validColors = colorDao.getColors(validPhoneId);
        assertEquals(1, validColors.size());
        assertEquals("White", validColors.get(0).getCode());
    }

    @Test
    public void testGetColorsWithInvalidParam(){
        Long invalidPhoneId = -1L;
        List<Color> invalidColors = colorDao.getColors(invalidPhoneId);
        assertEquals(0, invalidColors.size());
    }
}