/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.PoTransaction;
import com.saake.invoicer.entity.PurchaseOrder;
import com.saake.invoicer.entity.PurchaseOrderItems;
import com.saake.invoicer.util.PaymentStatusEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import org.apache.commons.logging.Log;

/**
 *
 * @author jn
 */
@Stateless
public class PurchaseOrderFacade extends AbstractFacade<PurchaseOrder> {

    @Inject
    private Log log;
    
    @Inject
    private ItemFacade itemSvc;
    
    @Inject
    private CustomerFacade custSvc;
    
    @Inject
    private InvoiceFacade invSvc;

    @Override
    public List<PurchaseOrder> findAll() {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();

        Root<PurchaseOrder> root = cq.from(PurchaseOrder.class);
        cq.select(root);
        
        ParameterExpression<String> status = cb.parameter(String.class);
        
//        cq.where(cb.notEqual(invRoot.get("status"), status));
        cq.where(cb.notEqual(root.get("deleted"), 'Y'));
        cq.orderBy(cb.desc(root.get("purchaseOrderId")));
        Query query = getEntityManager().createQuery(cq);
//        query.setParameter(status, PaymentStatusEnum.DELETE.getValue());

        List<PurchaseOrder> list = query.getResultList();
        
        for(PurchaseOrder wo : list){
            List<PurchaseOrderItems> tempDel = new ArrayList<>();
            for(PurchaseOrderItems wot : wo.getPurchaseOrderItems()){            
                if(wot.isDeleted()){
                    tempDel.add(wot);
                }                            
            }
            wo.getPurchaseOrderItems().removeAll(tempDel);
        }
        return list;
    }

    public PurchaseOrderFacade() {
        super(PurchaseOrder.class);
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder wo) {
        
        preSaveActions(wo);               

        wo.setDeleted("N");        

        Collection<PurchaseOrderItems> items = wo.getPurchaseOrderItems();
        wo.setPurchaseOrderItems(null);
        
        create(wo);
        getEntityManager().flush();
        
        for(PurchaseOrderItems woi: items){
            woi.setPurchaseOrderId(wo);
            woi.setDeleted("N");
            getEntityManager().persist(woi);
            getEntityManager().flush();
        }
        
        wo.setPurchaseOrderItems(items);
        
        getEntityManager().merge(wo);
        //getEntityManager().flush();
        
        return wo;

    }

    public PurchaseOrder updatePurchaseOrder(PurchaseOrder wo) {
        preSaveActions(wo);

        wo = edit(wo);

        return wo;
    }
    
    private PurchaseOrder preSaveActions(PurchaseOrder wo) {        
            
        //Add new items, if needed.
//        if (Utils.notEmpty(wo.getPurchaseOrderItems())) {                      
//            wo = addNewItemsIfNeeded(wo);                        
//        }
        
        return wo;
    }
//    
//    private PurchaseOrder addNewItemsIfNeeded(PurchaseOrder wo) {
//        List<PurchaseOrderItems> newItemsList = new ArrayList<>();
//
//        for (PurchaseOrderItems items : wo.getPurchaseOrderItems()) {
//            if (items.isAddItem()) {
//                newItemsList.add(items);
//            }
//        }
//
//        if (Utils.notEmpty(newItemsList)) {
//            for (PurchaseOrderItems invItem : newItemsList) {
//                invItem.setItem(itemSvc.saveItem(new Item(invItem.getDescription(), invItem.getUnitPrice(), "product")));
//            }
//        }
//
//        return wo;
//    }
    
    public void softDelete(PurchaseOrder current) {
        current.setDeleted("Y");

        getEntityManager().merge(current);
    }

    public PurchaseOrder getPurchaseOrder(int id) {
        PurchaseOrder wo = getEntityManager().find(PurchaseOrder.class,id);
        
        return wo;
    }
   
    public PurchaseOrder assignPurchaseOrder(PurchaseOrder current) {
        return getEntityManager().merge(current);
    }

    public PurchaseOrder postTransaction(PoTransaction trans) {
        PurchaseOrder wo = null;

        if (trans != null) {
            wo = trans.getPurchaseOrderId();

            if (trans.getPoTransId()== null) {
                trans.setCreateTs(new Date());
            } else {
                trans.setUpdateTs(new Date());
            }
            wo.setUpdateTs(new Date());
            
            populatePaymentStatusOnPurchaseOrder(wo);       
        
            getEntityManager().merge(wo);
            getEntityManager().flush();
            
        }
        return wo;
    }

    private void populatePaymentStatusOnPurchaseOrder(PurchaseOrder wo) {        
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
