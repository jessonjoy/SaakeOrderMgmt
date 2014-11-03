/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.model;

import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.entity.User;
import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jn
 */
public class SearchWorkOrderVO implements Serializable{
    private Date fromDate;
    private Date toDate;
    private Integer invoiceId;
    private String invoiceNum;
    private String status;
    private String searchText;
    private String vin;
    private String telNum;
    private String invoicePeriod;
    private String invoicedPaidAssigned;
    private Customer customer ;
    private User assignedUser ;
    private Double fromAmount;
    private Double toAmount;
       
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public Double getToAmount() {
        return toAmount;
    }

    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }       

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoicePeriod() {
        return invoicePeriod;
    }

    public void setInvoicePeriod(String invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }    

    public String getInvoicedPaidAssigned() {
        return invoicedPaidAssigned;
    }

    public void setInvoicedPaidAssigned(String invoicedPaidAssigned) {
        this.invoicedPaidAssigned = invoicedPaidAssigned;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }
    
    public boolean empty() {
        return (this.customer == null || this.customer.empty()) && this.fromAmount == null && this.toAmount == null
                && this.fromDate == null && this.toDate == null && this.invoiceId == null && Utils.isBlank(this.invoiceNum) 
                && Utils.isBlank(this.searchText) && Utils.isBlank(this.telNum) && Utils.isBlank(this.vin) && Utils.isBlank(this.status) && Utils.isBlank(this.invoicePeriod);
                
    } 
       
}
