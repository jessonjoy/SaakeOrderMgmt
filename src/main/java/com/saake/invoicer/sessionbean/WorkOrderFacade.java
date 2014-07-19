/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Item;
import com.saake.invoicer.entity.Transaction;
import com.saake.invoicer.entity.WorkOrder;
import com.saake.invoicer.entity.WorkOrderItems;
import com.saake.invoicer.util.PaymentStatusEnum;
import com.saake.invoicer.util.TransTypeEnum;
import com.saake.invoicer.util.Utils;
import com.saake.invoicer.util.WorkOrderStatusEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
@Stateless
public class WorkOrderFacade extends AbstractFacade<WorkOrder> {

    @Inject
    private Log log;
    
    @Inject
    private ItemFacade itemSvc;
    
    @Inject
    private CustomerFacade custSvc;
    
    @Inject
    private InvoiceFacade invSvc;

    @Override
    public List<WorkOrder> findAll() {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();

        Root<WorkOrder> root = cq.from(WorkOrder.class);
        cq.select(root);
        
        ParameterExpression<String> status = cb.parameter(String.class);
        
//        cq.where(cb.notEqual(invRoot.get("status"), status));
        cq.where(cb.notEqual(root.get("deleted"), 'Y'));
        cq.orderBy(cb.desc(root.get("workOrderId")));
        Query query = getEntityManager().createQuery(cq);
//        query.setParameter(status, PaymentStatusEnum.DELETE.getValue());

        List<WorkOrder> list = query.getResultList();
        
        for(WorkOrder wo : list){
            List<WorkOrderItems> tempDel = new ArrayList<>();
            for(WorkOrderItems wot : wo.getWorkOrderItems()){            
                if(wot.isDeleted()){
                    tempDel.add(wot);
                }                            
            }
            wo.getWorkOrderItems().removeAll(tempDel);
        }
        return list;
    }

    public WorkOrderFacade() {
        super(WorkOrder.class);
    }

    public WorkOrder createWorkOrder(WorkOrder wo) {
        
        preSaveActions(wo);               

        wo.setDeleted("N");        

        List<WorkOrderItems> items = wo.getWorkOrderItems();
        wo.setWorkOrderItems(null);
        
        create(wo);
        getEntityManager().flush();
        
        for(WorkOrderItems woi: items){
            woi.setWorkOrderId(wo);
            woi.setDeleted("N");
            getEntityManager().persist(woi);
            getEntityManager().flush();
        }
        
        wo.setWorkOrderItems(items);
        
        getEntityManager().merge(wo);
        //getEntityManager().flush();
        
        return wo;

    }

    public WorkOrder updateWorkOrder(WorkOrder wo) {
        preSaveActions(wo);

        //Reset this
        wo.setIsInvoiced("N");
        wo.setInvoicedTs(null);

        wo = edit(wo);

        return wo;
    }
    
    private WorkOrder preSaveActions(WorkOrder wo) {        
        //Save new vehicles, if changed.
        wo.getVehicle().setCustomerId(wo.getCustomerId());
        wo.setVehicle(custSvc.saveCustomerVehicle(wo.getVehicle()));
            
        //Add new items, if needed.
        if (Utils.notEmpty(wo.getWorkOrderItems())) {                      
            wo = addNewItemsIfNeeded(wo);                        
        }
        
        if(wo.getAssignedUser() != null){
            wo.setStatus(WorkOrderStatusEnum.STARTED.name());
        }
        return wo;
    }
    
    private WorkOrder addNewItemsIfNeeded(WorkOrder wo) {
        List<WorkOrderItems> newItemsList = new ArrayList<>();

        for (WorkOrderItems items : wo.getWorkOrderItems()) {
            if (items.isAddItem()) {
                newItemsList.add(items);
            }
        }

        if (Utils.notEmpty(newItemsList)) {
            for (WorkOrderItems invItem : newItemsList) {
                invItem.setItem(itemSvc.saveItem(new Item(invItem.getDescription(), invItem.getUnitPrice(), "product")));
            }
        }

        return wo;
    }
    
    public void softDelete(WorkOrder current) {
        current.setDeleted("Y");

        getEntityManager().merge(current);
    }

    public WorkOrder getWorkOrder(int id) {
        WorkOrder wo = getEntityManager().find(WorkOrder.class,id);
        
        return wo;
    }

    public void convertToInvoice(WorkOrder workOrder) {
        if(workOrder != null){
            
            workOrder.setUpdateTs(new Date());           
            workOrder.setStatus(WorkOrderStatusEnum.INVOICED.name());
            workOrder.setIsInvoiced("Y");
            workOrder.setInvoicedTs(new Date());
            
            getEntityManager().merge(workOrder);            
        }        
    }

    
    public WorkOrder assignWorkOrder(WorkOrder current) {
        return getEntityManager().merge(current);
    }

    public WorkOrder postTransaction(Transaction trans) {
        WorkOrder wo = null;

        if (trans != null) {
            wo = trans.getWorkOrderId();

            if (trans.getTranId() == null) {
                trans.setCreateTs(new Date());
            } else {
                trans.setUpdateTs(new Date());
            }
            wo.setUpdateTs(new Date());
            
            populatePaymentStatusOnWorkOrder(wo);       
        
            getEntityManager().merge(wo);
            getEntityManager().flush();
            
        }
        return wo;
    }

    private void populatePaymentStatusOnWorkOrder(WorkOrder wo) {        
        if(wo.getTransactionAmount().equals(wo.getAmount())){
            wo.setPaymentStatus(PaymentStatusEnum.PAID.name());
        }
        else if(wo.getTransactionAmount() < wo.getAmount()){
            wo.setPaymentStatus(PaymentStatusEnum.PARTIALPAY.name());
        }
        else if(wo.getTransactionAmount() > wo.getAmount()){
            wo.setPaymentStatus(PaymentStatusEnum.OVERPAY.name());
        }       
    }
}
