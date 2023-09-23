import com.es.core.cart.Cart;
import com.es.core.cart.CartItemDto;
import com.es.core.cart.CartItemsUpdateDto;
import com.es.core.cart.CartService;
import com.es.phoneshop.web.controller.pages.CartPageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CartPageControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartPageController cartPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCart() {
        CartItemsUpdateDto cartItemsUpdateDto = new CartItemsUpdateDto();
        Cart cart = new Cart();

        when(cartService.getCart()).thenReturn(cart);

        String viewName = cartPageController.getCart(cartItemsUpdateDto);

        assertEquals("cart", viewName);
        verify(cartService, times(1)).getCart();
    }

    @Test
    void testUpdateCart() {
        CartItemsUpdateDto cartItemsUpdateDto = new CartItemsUpdateDto();
        cartItemsUpdateDto.setItems(Collections.singletonList(new CartItemDto(1L, 2L)));

        when(cartService.getCart()).thenReturn(new Cart());

        String viewName = cartPageController.updateCart(cartItemsUpdateDto, mock(BindingResult.class), mock(Model.class));

        assertEquals("cart", viewName);
        verify(cartService, times(1)).update(anyMap());
    }

    @Test
    void testDeleteFromCart() {
        Long phoneId = 1L;
        Cart cart = new Cart();

        when(cartService.getCart()).thenReturn(cart);

        String viewName = cartPageController.deleteFromCart(phoneId, mock(Model.class));

        assertEquals("cart", viewName);
        verify(cartService, times(1)).remove(phoneId);
    }
}
