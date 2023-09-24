import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.HttpSessionCartService;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.stock.StockDao;
import com.es.core.order.OutOfStockException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class HttpSessionCartServiceTest {
    @InjectMocks
    private HttpSessionCartService cartService;

    @Mock
    private PhoneDao phoneDao;

    @Mock
    private StockDao stockDao;

    private HttpSession httpSession;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        httpSession = new MockHttpSession();
        cartService.setHttpSession(httpSession);
    }

    @Test
    public void testAddPhoneWithNewCartItem() throws OutOfStockException {
        Long phoneId = 1L;
        Long quantity = 2L;
        Cart cart = cartService.getCart();
        Phone phone = new Phone();
        when(phoneDao.get(phoneId)).thenReturn(Optional.of(phone));
        when(stockDao.availableStock(phoneId)).thenReturn(Math.toIntExact(quantity));

        cartService.addPhone(phoneId, quantity);

        assertEquals(1, cart.getItems().size());
        assertEquals(phone, cart.getItems().get(0).getPhone());
        assertEquals(quantity, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testAddPhoneWithExistingCartItem() throws OutOfStockException {
        Long phoneId = 1L;
        Long quantity = 2L;
        Cart cart = cartService.getCart();
        Phone phone = new Phone(1L, null, null, null, null);
        when(phoneDao.get(phoneId)).thenReturn(Optional.of(phone));
        when(stockDao.availableStock(phoneId)).thenReturn(5);

        cartService.addPhone(phoneId, quantity);
        cartService.addPhone(phoneId, quantity);

        assertEquals(1, cart.getItems().size());
        assertEquals(phone, cart.getItems().get(0).getPhone());
        assertEquals(Optional.of(quantity * 2), Optional.ofNullable(cart.getItems().get(0).getQuantity()));
    }

    @Test
    public void testAddPhoneOutOfStock() {
        Long phoneId = 1L;
        Long quantity = 10L;
        Cart cart = cartService.getCart();
        when(stockDao.availableStock(phoneId)).thenReturn(5);

        assertThrows(OutOfStockException.class, () -> cartService.addPhone(phoneId, quantity));

        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void testUpdateCart() throws OutOfStockException {
        Long phoneId = 1L;
        Long newQuantity = 3L;
        Cart cart = cartService.getCart();
        CartItem cartItem = new CartItem();
        cartItem.setPhone(new Phone(1L, null, null, null, null));
        cartItem.setQuantity(1L);
        cart.getItems().add(cartItem);
        when(stockDao.availableStock(phoneId)).thenReturn(Math.toIntExact(newQuantity));

        cartService.update(phoneId, newQuantity);

        assertEquals(1, cart.getItems().size());
        assertEquals(newQuantity, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testUpdateCartOutOfStock() {
        Long phoneId = 1L;
        Long newQuantity = 10L;
        Cart cart = cartService.getCart();
        CartItem cartItem = new CartItem();
        cartItem.setPhone(new Phone(1L, null, null, null, null));
        cartItem.setQuantity(1L);
        cart.getItems().add(cartItem);
        when(stockDao.availableStock(phoneId)).thenReturn(5);

        assertThrows(OutOfStockException.class, () -> cartService.update(phoneId, newQuantity));

        assertEquals(1, cart.getItems().size());
        assertEquals(Optional.of(1L), Optional.of(cart.getItems().get(0).getQuantity()));
    }

    @Test
    public void testRemoveCartItem() {
        Long phoneId = 1L;
        Cart cart = cartService.getCart();
        CartItem cartItem = new CartItem();
        cartItem.setPhone(new Phone(1L, null, null, null, null));
        cartItem.setQuantity(1L);
        cart.getItems().add(cartItem);

        cartService.remove(phoneId);

        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void testGetTotalQuantity() {
        Cart cart = cartService.getCart();
        CartItem cartItem1 = new CartItem();
        cartItem1.setQuantity(2L);
        Phone phone1 = new Phone();
        phone1.setPrice(BigDecimal.valueOf(100));
        cartItem1.setPhone(phone1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setQuantity(3L);
        Phone phone2 = new Phone();
        phone2.setPrice(BigDecimal.valueOf(50));
        cartItem2.setPhone(phone2);

        cart.getItems().addAll(Arrays.asList(cartItem1, cartItem2));
        cartService.recalculateCart(cart);
        Long totalQuantity = cartService.getTotalQuantity();

        assertEquals(Optional.of(5L), Optional.of(totalQuantity));
    }

    @Test
    public void testGetTotalCost() {
        Cart cart = cartService.getCart();
        CartItem cartItem1 = new CartItem();
        cartItem1.setQuantity(2L);
        Phone phone1 = new Phone();
        phone1.setPrice(BigDecimal.valueOf(100));
        cartItem1.setPhone(phone1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setQuantity(3L);
        Phone phone2 = new Phone();
        phone2.setPrice(BigDecimal.valueOf(50));
        cartItem2.setPhone(phone2);

        cart.getItems().addAll(Arrays.asList(cartItem1, cartItem2));
        cartService.recalculateCart(cart);
        BigDecimal totalCost = cartService.getTotalCost();

        assertEquals(BigDecimal.valueOf(350), totalCost);
    }

    @Test
    public void testRecalculateCartWithNullPhonePrice() {
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2L);
        Phone phone = new Phone();
        phone.setPrice(null);
        cartItem.setPhone(phone);
        cart.getItems().add(cartItem);

        cartService.recalculateCart(cart);

        assertEquals(2L, cart.getTotalQuantity());
        assertEquals(BigDecimal.ZERO, cart.getTotalCost());
    }

    @Test
    public void testRecalculateCartWithMixedItems() {
        Cart cart = new Cart();
        CartItem cartItem1 = new CartItem();
        cartItem1.setQuantity(2L);
        Phone phone1 = new Phone();
        phone1.setPrice(BigDecimal.valueOf(100));
        cartItem1.setPhone(phone1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setQuantity(3L);
        Phone phone2 = new Phone();
        phone2.setPrice(BigDecimal.valueOf(50));
        cartItem2.setPhone(phone2);

        cart.getItems().addAll(Arrays.asList(cartItem1, cartItem2));

        cartService.recalculateCart(cart);

        assertEquals(5L, cart.getTotalQuantity());
        assertEquals(BigDecimal.valueOf(350), cart.getTotalCost());
    }

}
