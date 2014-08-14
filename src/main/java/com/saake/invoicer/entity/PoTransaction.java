/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.entity;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "po_transaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PoTransaction.findAll", query = "SELECT p FROM PoTransaction p"),
    @NamedQuery(name = "PoTransaction.findByPoTransId", query = "SELECT p FROM PoTransaction p WHERE p.poTransId = :poTransId"),
    @NamedQuery(name = "PoTransaction.findByTransType", query = "SELECT p FROM PoTransaction p WHERE p.transType = :transType"),
    @NamedQuery(name = "PoTransaction.findByPurchaseOrderId", query = "SELECT p FROM PoTransaction p WHERE p.purchaseOrderId = :purchaseOrderId"),
    @NamedQuery(name = "PoTransaction.findByTransDate", query = "SELECT p FROM PoTransaction p WHERE p.transDate = :transDate"),
    @NamedQuery(name = "PoTransaction.findByAmount", query = "SELECT p FROM PoTransaction p WHERE p.amount = :amount"),
    @NamedQuery(name = "PoTransaction.findByComments", query = "SELECT p FROM PoTransaction p WHERE p.comments = :comments"),
    @NamedQuery(name = "PoTransaction.findByStatus", query = "SELECT p FROM PoTransaction p WHERE p.status = :status"),
    @NamedQuery(name = "PoTransaction.findByCreateTs", query = "SELECT p FROM PoTransaction p WHERE p.createTs = :createTs"),
    @NamedQuery(name = "PoTransaction.findByCreatedBy", query = "SELECT p FROM PoTransaction p WHERE p.createdBy = :createdBy"),
    @NamedQuery(name = "PoTransaction.findByUpdateTs", query = "SELECT p FROM PoTransaction p WHERE p.updateTs = :updateTs"),
    @NamedQuery(name = "PoTransaction.findByUpdatedBy", query = "SELECT p FROM PoTransaction p WHERE p.updatedBy = :updatedBy")})
public class PoTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "PO_TRANS_ID")
    private Integer poTransId;
    
    @Size(min = 1, max = 20)
    @Column(name = "TRANS_TYPE")
    private String transType;
    @JoinColumn(name = "PURCHASE_ORDER_ID", referencedColumnName = "PURCHASE_ORDER_ID")
    @ManyToOne
    private PurchaseOrder purchaseOrderId;
    @Column(name = "TRANS_DATE")
    @Temporal(TemporalType.DATE)
    private Date transDate;
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "COMMENTS")
    private String comments;
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;
    @Column(name = "CREATED_BY")
    private String createdBy;
    
    @Column(name = "UPDATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTs;
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    public PoTransaction() {
    }

    public PoTransaction(Integer poTransId) {
        this.poTransId = poTransId;
    }

    public PoTransaction(Integer poTransId, String transType, Date createTs, Date updateTs) {
        this.poTransId = poTransId;
        this.transType = transType;
        this.createTs = createTs;
        this.updateTs = updateTs;
    }

    public Integer getPoTransId() {
        return poTransId;
    }

    public void setPoTransId(Integer poTransId) {
        this.poTransId = poTransId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public PurchaseOrder getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(PurchaseOrder purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }    

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (poTransId != null ? poTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PoTransaction)) {
            return false;
        }
        PoTransaction other = (PoTransaction) object;
        if ((this.poTransId == null && other.poTransId != null) || (this.poTransId != null && !this.poTransId.equals(other.poTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.PoTransaction[ poTransId=" + poTransId + " ]";
    }
    
}
