package com.es.phoneshop.model.deliveryDetails;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeliveryDetails {

    private LocalDate deliveryDate;
    private String deliveryAddress;
}
