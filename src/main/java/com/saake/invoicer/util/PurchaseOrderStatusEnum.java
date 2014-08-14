/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.util;

/**
 *
 * @author jn
 */
public enum PurchaseOrderStatusEnum {
    DRAFT ("Draft" ),
    STARTED ("Started" ),
    ASSIGNED ("Assigned" ),
    ACCEPTED ("Accepted" ),
    INVOICED ("Invoiced" ),
    COMPLETED ("Completed"),
    CLOSED ("Closed");

    
    private String value;

    private PurchaseOrderStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
