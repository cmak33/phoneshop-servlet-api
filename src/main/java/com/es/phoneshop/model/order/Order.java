package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.contactDetails.ContactDetails;
import com.es.phoneshop.model.deliveryDetails.DeliveryDetails;
import com.es.phoneshop.model.entity.Entity;
import com.es.phoneshop.model.idGenerator.UUIDGenerator;
import com.es.phoneshop.model.paymentMethod.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Order extends Cart implements Entity<String> {

    private String id;

    private BigDecimal subtotal;
    private BigDecimal deliveryCost;

    private ContactDetails contactDetails;

    private DeliveryDetails deliveryDetails;

    private PaymentMethod paymentMethod;

    public Order() {
        id = UUIDGenerator.getInstance().generateStringId();
    }
}
