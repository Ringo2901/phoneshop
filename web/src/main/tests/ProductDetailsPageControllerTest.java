import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.controller.pages.ProductDetailsPageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductDetailsPageControllerTest {
    @InjectMocks
    private ProductDetailsPageController productDetailsPageController;

    @Mock
    private PhoneDao phoneDao;

    @Mock
    private CartService cartService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShowProductWithValidPhoneId() {
        Long phoneId = 1L;
        Phone phone = new Phone();
        when(phoneDao.get(phoneId)).thenReturn(Optional.of(phone));

        String viewName = productDetailsPageController.showProduct(phoneId, model);

        assertEquals("productPage", viewName);
        verify(model).addAttribute("phone", phone);
    }

    @Test
    public void testShowProductWithInvalidPhoneId() {
        Long phoneId = 1L;
        when(phoneDao.get(phoneId)).thenReturn(Optional.empty());

        String viewName = productDetailsPageController.showProduct(phoneId, model);

        assertEquals("productPage", viewName);
        verify(model).addAttribute("phone", null);
    }

    @Test
    public void testCartOnPage() {
        Cart cart = new Cart();
        when(cartService.getCart()).thenReturn(cart);

        Cart result = productDetailsPageController.cartOnPage();

        assertEquals(cart, result);
    }
}