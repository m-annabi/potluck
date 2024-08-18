package com.potluck.buffet.domain.model;


public record Address(
        String country,
        String state,
        String city,
        String street,
        String streetNumber,
        String zipCode
) {}
