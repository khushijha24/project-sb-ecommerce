package com.pro_sb_ecommerce.cart.service;

import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.cart.dto.CartItemResponse;
import com.pro_sb_ecommerce.cart.dto.CartResponse;
import com.pro_sb_ecommerce.cart.mapper.CartMapper;
import com.pro_sb_ecommerce.cart.model.Cart;
import com.pro_sb_ecommerce.cart.model.CartItem;
import com.pro_sb_ecommerce.cart.repository.CartItemRepository;
import com.pro_sb_ecommerce.cart.repository.CartRepository;
import com.pro_sb_ecommerce.exception.ResourceNotFoundException;
import com.pro_sb_ecommerce.product.model.Product;
import com.pro_sb_ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
//        this.userRepository = userRepository;
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    public CartResponse addItemToCart(User user, Long productId, int quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = getOrCreateCart(user);

        // Check if product already in cart
        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .priceAtTime(product.getPrice())
                    .build();
        }

        cartItemRepository.save(item);

        return CartMapper.toResponse(cart);
    }

    public CartResponse viewCart(User user) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getPriceAtTime(),
                        item.getQuantity(),
                        item.getPriceAtTime() * item.getQuantity()
                ))
                .toList();

        double totalAmount = items.stream()
                .mapToDouble(CartItemResponse::getTotalPrice)
                .sum();

        return new CartResponse(
                cart.getId(),
                user.getId(),
                items,
                totalAmount
        );
    }

    public CartResponse removeItemFromCart(User user, Long productId) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        return CartMapper.toResponse(cart);

    }

    public void clearCart(User user) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteByCart(cart);
    }


}
