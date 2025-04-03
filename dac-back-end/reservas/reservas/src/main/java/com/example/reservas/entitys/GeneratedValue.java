package com.example.reservas.entitys;

import jakarta.persistence.GenerationType;

public @interface GeneratedValue {

    GenerationType strategy();

}
