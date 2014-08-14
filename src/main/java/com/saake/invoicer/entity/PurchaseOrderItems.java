/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.entity;

import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "purchase_order_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderItems.findAll", query = "SELECT p FROM PurchaseOrderItems p"),
    @NamedQuery(name = "PurchaseOrderItems.findByPurchaseOrderItemsId", query = "SELECT p FROM PurchaseOrderItems p WHERE p.purchaseOrderItemsId = :purchaseOrderItemsId"),
    @NamedQuery(name = "PurchaseOrderItems.findByQuantity", query = "SELECT p FROM PurchaseOrderItems p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "PurchaseOrderItems.findByDescription", query = "SELECT p FROM PurchaseOrderItems p WHERE p.description = :description"),
    @NamedQuery(name = "PurchaseOrderItems.findByUnitPrice", query = "SELECT p FROM PurchaseOrderItems p WHERE p.unitPrice = :unitPrice"),
    @NamedQuery(name = "PurchaseOrderItems.findByDiscount", query = "SELECT p FROM PurchaseOrderItems p WHERE p.discount = :discount"),
    @NamedQuery(name = "PurchaseOrderItems.findByAmount", query = "SELECT p FROM PurchaseOrderItems p WHERE p.amount = :amount"),
    @NamedQuery(name = "PurchaseOrderItems.findByItemId", query = "SELECT p FROM PurchaseOrderItems p WHERE p.item = :item"),
    @NamedQuery(name = "PurchaseOrderItems.findByCreateTs", query = "SELECT p FROM PurchaseOrderItems p WHERE p.createTs = :createTs"),
    @NamedQuery(name = "PurchaseOrderItems.findByUpdateTs", query = "SELECT p FROM PurchaseOrderItems p WHERE p.updateTs = :updateTs"),
    @NamedQuery(name = "PurchaseOrderItems.findByCreatedBy", query = "SELECT p FROM PurchaseOrderItems p WHERE p.createdBy = :createdBy"),
    @NamedQuery(name = "PurchaseOrderItems.findByUpdatedBy", query = "SELECT p FROM PurchaseOrderItems p WHERE p.updatedBy = :updatedBy"),
    @NamedQuery(name = "PurchaseOrderItems.findByDeleted", query = "SELECT p FROM PurchaseOrderItems p WHERE p.deleted = :deleted")})
public class PurchaseOrderItems implements Serializable,Comparable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "PURCHASE_ORDER_ITEMS_ID")
    private Integer purchaseOrderItemsId;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Column(name = "DESCRIPTION")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UNIT_PRICE")
    private Double unitPrice;
    @Column(name = "DISCOUNT")
    private Double discount;
    @Column(name = "AMOUNT")
    private Double amount;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    
    
    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;
    
    @Column(name = "UPDATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTs;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "DELETED")
    private String deleted;
    @JoinColumn(name = "PURCHASE_ORDER_ID", referencedColumnName = "PURCHASE_ORDER_ID")
    @ManyToOne
    private PurchaseOrder purchaseOrderId;
    
    @Transient
    private boolean addItem = false;
    
    public PurchaseOrderItems() {
    }

    public PurchaseOrderItems(Integer purchaseOrderItemsId) {
        this.purchaseOrderItemsId = purchaseOrderItemsId;
    }

    public PurchaseOrderItems(Integer purchaseOrderItemsId, Date createTs, Date updateTs) {
        this.purchaseOrderItemsId = purchaseOrderItemsId;
        this.createTs = createTs;
        this.updateTs = updateTs;
    }

    public Integer getPurchaseOrderItemsId() {
        return purchaseOrderItemsId;
    }

    public void setPurchaseOrderItemsId(Integer purchaseOrderItemsId) {
        this.purchaseOrderItemsId = purchaseOrderItemsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public PurchaseOrder getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(PurchaseOrder purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purchaseOrderItemsId != null ? purchaseOrderItemsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseOrderItems)) {
            return false;
        }
        PurchaseOrderItems other = (PurchaseOrderItems) object;
        if ((this.purchaseOrderItemsId == null && other.purchaseOrderItemsId != null) || (this.purchaseOrderItemsId != null && !this.purchaseOrderItemsId.equals(other.purchaseOrderItemsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.PurchaseOrderItems[ purchaseOrderItemsId=" + purchaseOrderItemsId + " ]";
    }
    
    public boolean isEmptyForUse() {
        return (amount == null || amount == 0.0)
                //                && (discount == null || discount == 0.0) 
                //                && invoiceItemId == null 
                && Utils.isBlank(description)
                //                && (item == null || item.getItemId() == null ) && 
                //(quantity == null || quantity == 0)  
                && (unitPrice == null || unitPrice == 0.0);
    }
     
    public boolean isEmpty() {
        return (amount == null || amount == 0.0) && purchaseOrderItemsId == null && Utils.isBlank(description) && 
                (quantity == null || quantity == 0)  && 
                 (unitPrice == null || unitPrice == 0.0);
    }

        public boolean isAddItem() {
        return addItem;
    }

    public void setAddItem(boolean addItem) {
        this.addItem = addItem;
    }

    public void reset(){
        this.setDescription(null);
        this.setUnitPrice(0.0);
        this.setAmount(0.0);
        this.setQuantity(1);
        this.setItem(null);
        this.setAddItem(false);
    }
    
    public boolean isDeleted(){
        return Utils.convertYorNToBoolean(deleted);
    }   
    
    @Override
    public int compareTo(Object o) {
        int val = 0;

        if (o instanceof PurchaseOrderItems) {
            PurchaseOrderItems that = (PurchaseOrderItems)o;
            
            if(this.purchaseOrderItemsId != null && that.purchaseOrderItemsId != null ){
                val = that.purchaseOrderItemsId.compareTo(purchaseOrderItemsId);
            }
            else if(this.amount != null && that.amount != null ){
                val = that.amount.compareTo(amount);
            }
            else if(this.description != null && that.description != null ){
                val = that.description.compareTo(description);
            }
            else if(this.deleted != null && that.deleted != null ){
                val = that.deleted.compareTo(deleted);
            }
            else if(this.discount != null && that.amount != null ){
                val = that.amount.compareTo(amount);
            }
            else if(this.quantity != null && that.quantity != null ){
                val = that.quantity.compareTo(quantity);
            }
            else if(this.unitPrice != null && that.unitPrice != null ){
                val = that.unitPrice.compareTo(unitPrice);
            }
            else if(this.purchaseOrderId != null && this.purchaseOrderId.getPurchaseOrderId()!= null && that.purchaseOrderId != null && that.purchaseOrderId.getPurchaseOrderId() != null){
                val = that.purchaseOrderId.getPurchaseOrderId().compareTo(purchaseOrderId.getPurchaseOrderId());
            }
        }
        
        return val;
    }
    
    static PurchaseOrderItems copy(PurchaseOrderItems current, PurchaseOrderItems that) {
        that.purchaseOrderItemsId = current.purchaseOrderItemsId;
        that.quantity = current.quantity;
        that.description = current.description;
        that.unitPrice = current.unitPrice;
        that.discount = current.discount;
        that.amount = current.amount;
        that.item = current.item;
        that.createTs = current.createTs;
        that.updateTs = current.updateTs;
        that.createdBy = current.createdBy;
        that.updatedBy = current.updatedBy;
        that.purchaseOrderId = current.purchaseOrderId;
        
        return that;
    }
}
