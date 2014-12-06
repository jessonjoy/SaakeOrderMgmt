/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import com.google.common.base.Objects;
import com.saake.invoicer.util.PaymentStatusEnum;
import com.saake.invoicer.util.TransTypeEnum;
import com.saake.invoicer.util.Utils;
import com.saake.invoicer.util.WorkOrderStatusEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "work_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkOrder.findAll", query = "SELECT w FROM WorkOrder w"),
    @NamedQuery(name = "WorkOrder.findByWorkOrderId", query = "SELECT w FROM WorkOrder w WHERE w.workOrderId = :workOrderId"),
    @NamedQuery(name = "WorkOrder.findByWorkOrderNum", query = "SELECT w FROM WorkOrder w WHERE w.workOrderNum = :workOrderNum")})
public class WorkOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    public static WorkOrder copy(WorkOrder that, WorkOrder current) {
        
        current.workOrderId = that.workOrderId;
        current.workOrderNum = that.workOrderNum;
        current.notes = that.notes;
        current.workOrderDate = that.workOrderDate;
        current.discount = that.discount;
        current.amount = that.amount;
        current.advanceAmt = that.advanceAmt;
        current.laborAmt = that.laborAmt;
        current.customerId = that.customerId;
        current.assignedUser = that.assignedUser;
        current.vehicle = that.vehicle;
        current.status = that.status;
        current.isInvoiced = that.isInvoiced;
        current.createTs = that.createTs;
        current.updateTs = that.updateTs;
        current.invoicedTs = that.invoicedTs;
        current.createdBy = that.createdBy;
        current.updatedBy = that.updatedBy;
        current.paymentStatus = that.paymentStatus;
        current.transactions = that.transactions;
        
        if(that.workOrderItems != null){
            current.workOrderItems = new ArrayList<>();
            for(WorkOrderItems woItems : that.workOrderItems){
                WorkOrderItems thatItem = WorkOrderItems.copy(woItems, new WorkOrderItems());
                thatItem.setWorkOrderItemsId(null);
                thatItem.setAddItem(false);
                thatItem.setCreateTs(new Date());
                thatItem.setUpdateTs(null);
                thatItem.setUpdatedBy(null);
                current.workOrderItems.add(thatItem) ;                
            }
        }
        
        return current;        
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_ORDER_ID")
    private Integer workOrderId;
    
    @Column(name = "WORK_ORDER_NUM")
    private String workOrderNum;
    
    @Column(name = "CUST_WORK_DESC")
    private String custWorkDesc;
        
    @Column(name = "WORK_PERF_DESC")
    private String workPerfDesc;
    
    @Column(name = "WORK_ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workOrderDate;
    
    @Column(name = "DISCOUNT")
    private Double discount;
    
    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "ADVANCE_AMT")
    private Double advanceAmt;
     
    @Column(name = "LABOR_AMT")
    private Double laborAmt;

    @Column(name = "LABOR_ADJ_AMT")
    private Double laborAdjAmt;
        
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne
    private Customer customerId;

    @JoinColumn(name = "ASSIGNED_USER_ID", referencedColumnName = "user_id")
    @OneToOne
    private User assignedUser;

    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    @ManyToOne
    private Vehicle vehicle;
    
    @Column(name = "STATUS")
    private String status;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "IS_INVOICED")
    private String isInvoiced;

    @Column(name = "DELETED")
    private String deleted;
    
    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;
    
    @Column(name = "UPDATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTs;
    
    @Column(name = "INVOICED_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoicedTs;
    
    @Column(name = "CREATED_BY")
    private String createdBy;
    
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    
    @Column(name = "OWN_PARTS_COST_AMT")
    private Double ownPartsCostAmt;
            
    @Column(name = "OTHER_PARTS_COST_AMT")
    private Double otherPartsCostAmt;
            
    @Column(name = "OWN_PARTS_DISC_AMT")
    private Double ownPartsDiscAmt;
            
    @Column(name = "OTHER_PARTS_DISC_AMT")
    private Double otherPartsDiscAmt;            
    
    @OneToMany(mappedBy = "workOrderId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<WorkOrderItems> workOrderItems;
           
    @OneToMany(mappedBy = "workOrderId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;

    @OneToMany(mappedBy = "workOrderId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<WorkOrderNotes> notes;
    
    public WorkOrder() {
    }

    public WorkOrder(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public WorkOrder(Integer workOrderId, Date workOrderDate, Date createTs, Date updateTs) {
        this.workOrderId = workOrderId;
        this.workOrderDate = workOrderDate;
        this.createTs = createTs;
        this.updateTs = updateTs;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public Collection<WorkOrderNotes> getNotes() {
        return notes;
    }

    public void setNotes(Collection<WorkOrderNotes> notes) {
        this.notes = notes;
    }

    public String getCustWorkDesc() {
        return custWorkDesc;
    }

    public void setCustWorkDesc(String custWorkDesc) {
        this.custWorkDesc = custWorkDesc;
    }

    public String getWorkPerfDesc() {
        return workPerfDesc;
    }

    public void setWorkPerfDesc(String workPerfDesc) {
        this.workPerfDesc = workPerfDesc;
    }
    
    public Date getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(Date workOrderDate) {
        this.workOrderDate = workOrderDate;
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

    public Double getAdvanceAmt() {
        return advanceAmt;
    }

    public void setAdvanceAmt(Double advanceAmt) {
        this.advanceAmt = advanceAmt;
    }

    public Double getLaborAmt() {
        return laborAmt;
    }

    public void setLaborAmt(Double laborAmt) {
        this.laborAmt = laborAmt;
    }    

    public Double getLaborAdjAmt() {
        return laborAdjAmt;
    }

    public void setLaborAdjAmt(Double laborAdjAmt) {
        this.laborAdjAmt = laborAdjAmt;
    }       

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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

    public String getIsInvoiced() {
        return isInvoiced;
    }

    public void setIsInvoiced(String isInvoiced) {
        this.isInvoiced = isInvoiced;
    }
    
    public Date getInvoicedTs() {
        return invoicedTs;
    }

    public void setInvoicedTs(Date invoicedTs) {
        this.invoicedTs = invoicedTs;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (workOrderId != null ? workOrderId.hashCode() : 0);
        return hash;
    }

    @XmlTransient
    public List<WorkOrderItems> getWorkOrderItems() {
        return workOrderItems;
    }

    public void setWorkOrderItems(List<WorkOrderItems> workOrderItems) {
        this.workOrderItems = workOrderItems;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }       

    public Double getOwnPartsCostAmt() {
        return ownPartsCostAmt;
    }

    public void setOwnPartsCostAmt(Double ownPartsCostAmt) {
        this.ownPartsCostAmt = ownPartsCostAmt;
    }

    public Double getOtherPartsCostAmt() {
        return otherPartsCostAmt;
    }

    public void setOtherPartsCostAmt(Double otherPartsCostAmt) {
        this.otherPartsCostAmt = otherPartsCostAmt;
    }

    public Double getOwnPartsDiscAmt() {
        return ownPartsDiscAmt;
    }

    public void setOwnPartsDiscAmt(Double ownPartsDiscAmt) {
        this.ownPartsDiscAmt = ownPartsDiscAmt;
    }

    public Double getOtherPartsDiscAmt() {
        return otherPartsDiscAmt;
    }

    public void setOtherPartsDiscAmt(Double otherPartsDiscAmt) {
        this.otherPartsDiscAmt = otherPartsDiscAmt;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkOrder)) {
            return false;
        }
        WorkOrder other = (WorkOrder) object;
        if ((this.workOrderId == null && other.workOrderId != null) || (this.workOrderId != null && !this.workOrderId.equals(other.workOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WorkOrder{" + "workOrderId=" + workOrderId + ", workOrderNum=" + workOrderNum + ", custWorkDesc=" + custWorkDesc + ", workPerfDesc=" + workPerfDesc + ", workOrderDate=" + workOrderDate + ", discount=" + discount + ", amount=" + amount + ", advanceAmt=" + advanceAmt + ", laborAmt=" + laborAmt + ", laborAdjAmt=" + laborAdjAmt + ", customerId=" + customerId + ", assignedUser=" + assignedUser + ", vehicle=" + vehicle + ", status=" + status + ", paymentStatus=" + paymentStatus + ", isInvoiced=" + isInvoiced + ", deleted=" + deleted + ", createTs=" + createTs + ", updateTs=" + updateTs + ", invoicedTs=" + invoicedTs + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", workOrderItems=" + workOrderItems + ", transactions=" + transactions + ", notes=" + notes + '}';
    }    
    
     public boolean isEmpty() {
        return this.customerId == null && this.vehicle == null && (this.amount == null || this.amount == 0) 
                && (this.laborAmt == null || this.laborAmt == 0) 
                && (this.advanceAmt == null || this.advanceAmt == 0) 
                && this.workOrderDate == null 
                && Utils.isEmpty(this.notes) && this.workOrderId == null && Utils.isEmpty(this.workOrderItems) 
                && Utils.isBlank(this.status);
    }
     
     public Double getItemTotalAmount() {
        Double tot = 0.0;
        if(getWorkOrderItems() != null ){
            for (WorkOrderItems oItm : getWorkOrderItems()) {
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
            for (Transaction trans : getTransactions()) {
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
      
    public boolean isInvoiced(){
        return Utils.convertYorNToBoolean(isInvoiced);
    }   
    
    public boolean isAssigned(){
        return assignedUser != null;
    }
    
    public boolean isAccepted(){
        return status.equals(WorkOrderStatusEnum.ACCEPTED.name());
    }

    public boolean isCompleted(){
        return status.equals(WorkOrderStatusEnum.COMPLETED.name());
    }

    public boolean isClosed(){
        return status.equals(WorkOrderStatusEnum.CLOSED.name());
    }
    
    public boolean isDraft(){
        return Objects.equal(status,WorkOrderStatusEnum.STARTED.name());
    }

    public boolean isPaid(){
        return Objects.equal(paymentStatus,PaymentStatusEnum.PAID.name());
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
        return Utils.notBlank(status)? WorkOrderStatusEnum.valueOf(status).getValue() : "";
    }

    public String getStatusColor(){
        return Utils.notBlank(status)? 
                status.equals(WorkOrderStatusEnum.DRAFT.name())? "gray":
                status.equals(WorkOrderStatusEnum.STARTED.name())? "darkgray":
                status.equals(WorkOrderStatusEnum.CLOSED.name())? "darkgreen":
                status.equals(WorkOrderStatusEnum.COMPLETED.name())? "green": 
                status.equals(WorkOrderStatusEnum.INVOICED.name())? "orange": 
                status.equals(WorkOrderStatusEnum.ACCEPTED.name())? "limegreen": "blue":"";
    }

    public void addNote(WorkOrderNotes currentNotes) {
        if(this.getNotes() == null){
            this.setNotes(new HashSet<WorkOrderNotes>());
        }
        this.getNotes().add(currentNotes);
    }
    
}
