/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.model;

/**
 *
 * @author jn
 */
public class ReportViewOptions {
    public Boolean showItemAmounts;
    public Boolean showTotalAmount;
    public Boolean showUnitPrice;
    public Boolean showUserAssigned;

    public Boolean getShowItemAmounts() {
        return showItemAmounts;
    }

    public void setShowItemAmounts(Boolean showItemAmounts) {
        this.showItemAmounts = showItemAmounts;
    }

    public Boolean getShowTotalAmount() {
        return showTotalAmount;
    }

    public void setShowTotalAmount(Boolean showTotalAmount) {
        this.showTotalAmount = showTotalAmount;
    }

    public Boolean getShowUnitPrice() {
        return showUnitPrice;
    }

    public void setShowUnitPrice(Boolean showUnitPrice) {
        this.showUnitPrice = showUnitPrice;
    }

    public Boolean getShowUserAssigned() {
        return showUserAssigned;
    }

    public void setShowUserAssigned(Boolean showUserAssigned) {
        this.showUserAssigned = showUserAssigned;
    }

        
}
