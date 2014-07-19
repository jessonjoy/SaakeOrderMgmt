/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.util;

/**
 *
 * @author jn
 */
public enum PaymentStatusEnum {
    
    PAID ("Paid" ),
    PARTIALPAY ("Partial Paid"),
    OVERPAY ("Over Paid");
    
    private String value;

    private PaymentStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
