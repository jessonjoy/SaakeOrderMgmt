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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "purchase_order_notes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderNotes.findAll", query = "SELECT p FROM PurchaseOrderNotes p"),
    @NamedQuery(name = "PurchaseOrderNotes.findByNotesId", query = "SELECT p FROM PurchaseOrderNotes p WHERE p.notesId = :notesId"),
    @NamedQuery(name = "PurchaseOrderNotes.findByPurchaseOrderId", query = "SELECT p FROM PurchaseOrderNotes p WHERE p.purchaseOrderId = :purchaseOrderId"),
    @NamedQuery(name = "PurchaseOrderNotes.findByNotes", query = "SELECT p FROM PurchaseOrderNotes p WHERE p.notes = :notes"),
    @NamedQuery(name = "PurchaseOrderNotes.findByCreatedBy", query = "SELECT p FROM PurchaseOrderNotes p WHERE p.createdBy = :createdBy"),
    @NamedQuery(name = "PurchaseOrderNotes.findByCreatedOn", query = "SELECT p FROM PurchaseOrderNotes p WHERE p.createdOn = :createdOn")})
public class PurchaseOrderNotes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "notes_id")
    private Integer notesId;
    
    @JoinColumn(name = "PURCHASE_ORDER_ID", referencedColumnName = "PURCHASE_ORDER_ID")
    @ManyToOne
    private PurchaseOrder purchaseOrderId;
    @Column(name = "notes")
    private String notes;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    public PurchaseOrderNotes() {
    }

    public PurchaseOrderNotes(Integer notesId) {
        this.notesId = notesId;
    }

    public PurchaseOrderNotes(Integer notesId, PurchaseOrder purchaseOrderId) {
        this.notesId = notesId;
        this.purchaseOrderId = purchaseOrderId;
    }

    public Integer getNotesId() {
        return notesId;
    }

    public void setNotesId(Integer notesId) {
        this.notesId = notesId;
    }

    public PurchaseOrder getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(PurchaseOrder purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notesId != null ? notesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseOrderNotes)) {
            return false;
        }
        PurchaseOrderNotes other = (PurchaseOrderNotes) object;
        if ((this.notesId == null && other.notesId != null) || (this.notesId != null && !this.notesId.equals(other.notesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.PurchaseOrderNotes[ notesId=" + notesId + " ]";
    }
    
    
    public boolean isEmpty() {
        return Utils.isBlank(this.notes );
    }  
    
}
