import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.enums.SortField;
import com.es.core.enums.SortOrder;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.controller.pages.ProductListPageController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductListPageControllerTest {

    @Mock
    private PhoneDao phoneDao;

    @Mock
    private CartService cartService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductListPageController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShowProductList() {
        // Mock data
        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone()); // Add sample phone objects
        when(phoneDao.findAll(anyInt(), anyInt(), any(), any(), any())).thenReturn(phones);
        when(phoneDao.numberByQuery(any())).thenReturn(phones.size() * 2L);

        String viewName = controller.showProductList(1, "model", "asc", "query", model);

        verify(phoneDao).findAll(0, 10, SortField.model, SortOrder.asc, "query");
        verify(phoneDao).numberByQuery("query");
        verify(model).addAttribute("phones", phones);
        verify(model).addAttribute("numberOfPhones", phones.size() * 2L);
        verify(model).addAttribute("numberOfPages", 1L);
        assertEquals("productList", viewName);
    }

    @Test
    public void testShowProductListWithNullParams() {
        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone());
        when(phoneDao.findAll(anyInt(), anyInt(), any(), any(), any())).thenReturn(phones);
        when(phoneDao.numberByQuery(any())).thenReturn(phones.size() * 2L);

        String viewName = controller.showProductList(null, null, null, null, model);

        verify(phoneDao).findAll(0, 10, null, null, null);
        verify(phoneDao).numberByQuery(null);
        verify(model).addAttribute("phones", phones);
        verify(model).addAttribute("numberOfPhones", phones.size() * 2L);
        verify(model).addAttribute("numberOfPages", 1L);
        assertEquals("productList", viewName);
    }
}
