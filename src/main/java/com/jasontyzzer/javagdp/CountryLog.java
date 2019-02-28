package com.jasontyzzer.javagdp;

import java.io.Serializable;

public class CountryLog implements Serializable {
    private final String text;

    public CountryLog(String text) {
        this.text = text;
    }
}