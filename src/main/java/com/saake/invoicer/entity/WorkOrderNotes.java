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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "work_order_notes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkOrderNotes.findAll", query = "SELECT w FROM WorkOrderNotes w"),
    @NamedQuery(name = "WorkOrderNotes.findByNotesId", query = "SELECT w FROM WorkOrderNotes w WHERE w.notesId = :notesId"),
    @NamedQuery(name = "WorkOrderNotes.findByNotes", query = "SELECT w FROM WorkOrderNotes w WHERE w.notes = :notes"),
    @NamedQuery(name = "WorkOrderNotes.findByCreatedBy", query = "SELECT w FROM WorkOrderNotes w WHERE w.createdBy = :createdBy"),
    @NamedQuery(name = "WorkOrderNotes.findByCreatedOn", query = "SELECT w FROM WorkOrderNotes w WHERE w.createdOn = :createdOn")})
public class WorkOrderNotes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "notes_id")
    private Integer notesId;
    @Size(max = 200)
    @Column(name = "notes")
    private String notes;
    @Size(max = 20)
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
        
    @JoinColumn(name = "WORK_ORDER_ID", referencedColumnName = "WORK_ORDER_ID")
    @ManyToOne
    private WorkOrder workOrderId;

    public WorkOrderNotes() {
    }

    public WorkOrderNotes(Integer notesId) {
        this.notesId = notesId;
    }

    public Integer getNotesId() {
        return notesId;
    }

    public void setNotesId(Integer notesId) {
        this.notesId = notesId;
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
        if (!(object instanceof WorkOrderNotes)) {
            return false;
        }
        WorkOrderNotes other = (WorkOrderNotes) object;
        if ((this.notesId == null && other.notesId != null) || (this.notesId != null && !this.notesId.equals(other.notesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.WorkOrderNotes[ notesId=" + notesId + " ]";
    }

    public boolean isEmpty() {
        return Utils.isBlank(this.notes );
    }    
}
