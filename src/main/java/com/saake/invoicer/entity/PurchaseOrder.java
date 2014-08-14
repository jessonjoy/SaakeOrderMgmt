/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.entity;

import com.google.common.base.Objects;
import com.saake.invoicer.util.PaymentStatusEnum;
import com.saake.invoicer.util.TransTypeEnum;
import com.saake.invoicer.util.Utils;
import com.saake.invoicer.util.PurchaseOrderStatusEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "purchase_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrder.findAll", query = "SELECT p FROM PurchaseOrder p"),
    @NamedQuery(name = "PurchaseOrder.findByPurchaseOrderId", query = "SELECT p FROM PurchaseOrder p WHERE p.purchaseOrderId = :purchaseOrderId"),
    @NamedQuery(name = "PurchaseOrder.findByPurchaseOrderNum", query = "SELECT p FROM PurchaseOrder p WHERE p.purchaseOrderNum = :purchaseOrderNum"),
    @NamedQuery(name = "PurchaseOrder.findByPurchaseOrderDate", query = "SELECT p FROM PurchaseOrder p WHERE p.purchaseOrderDate = :purchaseOrderDate"),
    @NamedQuery(name = "PurchaseOrder.findByDiscount", query = "SELECT p FROM PurchaseOrder p WHERE p.discount = :discount"),
    @NamedQuery(name = "PurchaseOrder.findByAmount", query = "SELECT p FROM PurchaseOrder p WHERE p.amount = :amount"),
    @NamedQuery(name = "PurchaseOrder.findByCustomerId", query = "SELECT p FROM PurchaseOrder p WHERE p.customerId = :customerId"),
    @NamedQuery(name = "PurchaseOrder.findByStatus", query = "SELECT p FROM PurchaseOrder p WHERE p.status = :status"),
    @NamedQuery(name = "PurchaseOrder.findByCreateTs", query = "SELECT p FROM PurchaseOrder p WHERE p.createTs = :createTs"),
    @NamedQuery(name = "PurchaseOrder.findByUpdateTs", query = "SELECT p FROM PurchaseOrder p WHERE p.updateTs = :updateTs"),
    @NamedQuery(name = "PurchaseOrder.findByCreatedBy", query = "SELECT p FROM PurchaseOrder p WHERE p.createdBy = :createdBy"),
    @NamedQuery(name = "PurchaseOrder.findByUpdatedBy", query = "SELECT p FROM PurchaseOrder p WHERE p.updatedBy = :updatedBy"),
    @NamedQuery(name = "PurchaseOrder.findByDeleted", query = "SELECT p FROM PurchaseOrder p WHERE p.deleted = :deleted"),
    @NamedQuery(name = "PurchaseOrder.findByPaymentStatus", query = "SELECT p FROM PurchaseOrder p WHERE p.paymentStatus = :paymentStatus")})
public class PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "PURCHASE_ORDER_ID")
    private Integer purchaseOrderId;
    @Column(name = "PURCHASE_ORDER_NUM")
    private String purchaseOrderNum;
    @Column(name = "PO_DESC")
    private String poDesc;
    @Column(name = "PURCHASE_ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseOrderDate;
    @Column(name = "DISCOUNT")
    private Integer discount;
    @Column(name = "AMOUNT")
    private Double amount;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne
    private Customer customerId;
    @Column(name = "STATUS")
    private String status;
    
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
    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;
    @OneToMany(mappedBy = "purchaseOrderId")
    private Collection<PurchaseOrderItems> purchaseOrderItems;
    
    @OneToMany(mappedBy = "purchaseOrderId")
    private Collection<PoTransaction> transactions;

    @OneToMany(mappedBy = "purchaseOrderId")
    private Collection<PurchaseOrderNotes> notes;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public PurchaseOrder(Integer purchaseOrderId, Date purchaseOrderDate, Date createTs, Date updateTs) {
        this.purchaseOrderId = purchaseOrderId;
        this.purchaseOrderDate = purchaseOrderDate;
        this.createTs = createTs;
        this.updateTs = updateTs;
    }

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPurchaseOrderNum() {
        return purchaseOrderNum;
    }

    public void setPurchaseOrderNum(String purchaseOrderNum) {
        this.purchaseOrderNum = purchaseOrderNum;
    }

    public Date getPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    public void setPurchaseOrderDate(Date purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoDesc() {
        return poDesc;
    }

    public void setPoDesc(String poDesc) {
        this.poDesc = poDesc;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @XmlTransient
    public Collection<PurchaseOrderItems> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public void setPurchaseOrderItems(Collection<PurchaseOrderItems> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public Collection<PoTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<PoTransaction> transactions) {
        this.transactions = transactions;
    }

    public Collection<PurchaseOrderNotes> getNotes() {
        return notes;
    }

    public void setNotes(Collection<PurchaseOrderNotes> notes) {
        this.notes = notes;
    }        

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purchaseOrderId != null ? purchaseOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseOrder)) {
            return false;
        }
        PurchaseOrder other = (PurchaseOrder) object;
        if ((this.purchaseOrderId == null && other.purchaseOrderId != null) || (this.purchaseOrderId != null && !this.purchaseOrderId.equals(other.purchaseOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.PurchaseOrder[ purchaseOrderId=" + purchaseOrderId + " ]";
    }
    
    public boolean isEmpty() {
        return this.customerId == null  && (this.amount == null || this.amount == 0)                
                && this.purchaseOrderDate == null 
                /*&& Utils.isEmpty(this.notes) */ && this.purchaseOrderId == null && Utils.isEmpty(this.purchaseOrderItems) 
                && Utils.isBlank(this.status);
    }
     
     public Double getItemTotalAmount() {
        Double tot = 0.0;
        if(getPurchaseOrderItems() != null ){
            for (PurchaseOrderItems oItm : getPurchaseOrderItems()) {
                if (oItm.getAmount() != null) {
                    tot = tot + oItm.getAmount();
                }            
            }
        }
                
        return tot;
    }
     
      public Double getTransactionAmount() {
        Double tot = 0.0;
        if (getTransactions() != null) {
            for (PoTransaction trans : getTransactions()) {
                if (trans.getTransType().equals(TransTypeEnum.PAYMENT.getValue())) {
                    if (trans.getAmount() != null) {
                        tot = tot + trans.getAmount();
                    }
                }
                if (trans.getTransType().equals(TransTypeEnum.REFUND.getValue())) {
                    tot = tot - trans.getAmount();
                }
            }
        }

        return tot;
    }        
    
    public boolean isAccepted(){
        return status.equals(PurchaseOrderStatusEnum.ACCEPTED.name());
    }

    public boolean isCompleted(){
        return status.equals(PurchaseOrderStatusEnum.COMPLETED.name());
    }

    public boolean isClosed(){
        return status.equals(PurchaseOrderStatusEnum.CLOSED.name());
    }
    
    public boolean isDraft(){
        return Objects.equal(status,PurchaseOrderStatusEnum.STARTED.name());
    }

    public boolean isPaid(){
        return Objects.equal(paymentStatus,PaymentStatusEnum.PAID.name());
    }
    
    public boolean isDeleted(){
        return Utils.convertYorNToBoolean(deleted);
    }
    
    public boolean isAnyPaymentMade(){
        return Objects.equal(paymentStatus,PaymentStatusEnum.PAID.name())
                || Objects.equal(paymentStatus,PaymentStatusEnum.OVERPAY.name())
                || Objects.equal(paymentStatus,PaymentStatusEnum.PARTIALPAY.name());
    }
    
    public boolean isOverOrPartialPaymentMade(){
        return Objects.equal(paymentStatus,PaymentStatusEnum.OVERPAY.name())
                || Objects.equal(paymentStatus,PaymentStatusEnum.PARTIALPAY.name());
    }
         
    public Double getPaymentDifference(){
        return this.amount - this.getTransactionAmount();
    }
         
    public String getStatusDisplay() {
        return Utils.notBlank(status)? PurchaseOrderStatusEnum.valueOf(status).getValue() : "";
    }

    public String getStatusColor(){
        return Utils.notBlank(status)? 
                status.equals(PurchaseOrderStatusEnum.DRAFT.name())? "gray":
                status.equals(PurchaseOrderStatusEnum.STARTED.name())? "darkgray":
                status.equals(PurchaseOrderStatusEnum.CLOSED.name())? "darkgreen":
                status.equals(PurchaseOrderStatusEnum.COMPLETED.name())? "green": 
                status.equals(PurchaseOrderStatusEnum.INVOICED.name())? "orange": 
                status.equals(PurchaseOrderStatusEnum.ACCEPTED.name())? "limegreen": "blue":"";
    }

    public void addNote(PurchaseOrderNotes currentNotes) {
        if(this.getNotes() == null){
            this.setNotes(new HashSet<PurchaseOrderNotes>());
        }
        this.getNotes().add(currentNotes);
    }
     public static PurchaseOrder copy(PurchaseOrder that, PurchaseOrder current) {
        
        current.purchaseOrderId = that.purchaseOrderId;
        current.purchaseOrderNum = that.purchaseOrderNum;
        current.notes = that.notes;
        current.purchaseOrderDate = that.purchaseOrderDate;
        current.discount = that.discount;
        current.amount = that.amount;
        current.customerId = that.customerId;
        current.status = that.status;
        current.createTs = that.createTs;
        current.updateTs = that.updateTs;
        current.createdBy = that.createdBy;
        current.updatedBy = that.updatedBy;
        current.paymentStatus = that.paymentStatus;
        current.transactions = that.transactions;
        
        if(that.purchaseOrderItems != null){
            current.purchaseOrderItems = new ArrayList<>();
            for(PurchaseOrderItems woItems : that.purchaseOrderItems){
                PurchaseOrderItems thatItem = PurchaseOrderItems.copy(woItems, new PurchaseOrderItems());
                thatItem.setPurchaseOrderItemsId(null);
                thatItem.setAddItem(false);
                thatItem.setCreateTs(new Date());
                thatItem.setUpdateTs(null);
                thatItem.setUpdatedBy(null);
                current.purchaseOrderItems.add(thatItem) ;                
            }
        }
        
        return current;        
    }
}
