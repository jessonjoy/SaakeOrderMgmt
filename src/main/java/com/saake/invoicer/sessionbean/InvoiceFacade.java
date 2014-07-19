/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Item;
import com.saake.invoicer.entity.Transaction;
import com.saake.invoicer.util.PaymentStatusEnum;
import com.saake.invoicer.util.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
public class InvoiceFacade {

   // private static final Log log = LogFactory.getLog(InvoiceFacade.class);

//    @EJB
//    ItemFacade itemSvc;
//    
//    @EJB
//    CustomerFacade custSvc;
//    
//   
//    public InvoiceFacade() {
//        super(Invoice.class);
//    }
//    
//    @Override
//    public List<Invoice> findAll() {
//        
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//
//        Root<Invoice> invRoot = cq.from(Invoice.class);
//        cq.select(invRoot);
//
//        ParameterExpression<String> status = cb.parameter(String.class);
////        cq.where(cb.notEqual(invRoot.get("status"), status));
//        cq.where(cb.notEqual(invRoot.get("deleted"), 'Y'));
//        cq.orderBy(cb.desc(invRoot.get("invoiceId")));
//        Query query = getEntityManager().createQuery(cq);
////        query.setParameter(status, PaymentStatusEnum.DELETE.getValue());
//
//        List<Invoice> list = query.getResultList();
//        
//        for(Invoice wo : list){
//            List<InvoiceItems> tempDel = new ArrayList<>();
//            for(InvoiceItems wot : wo.getInvoiceItems()){            
//                if(wot.isDeleted()){
//                    tempDel.add(wot);
//                }                            
//            }
//            wo.getInvoiceItems().removeAll(tempDel);
//        }
//        return list;
//    }
//
//    public Invoice createInvoice(Invoice invoice){    
//        preSaveActions(invoice);
//        
//        invoice.setStatus(PaymentStatusEnum.DRAFT.name());
//        invoice.setDeleted("N");
//        //create(invoice);   
//        
//        List<InvoiceItems> items = invoice.getInvoiceItemsAsList();
//        invoice.setInvoiceItems(null);
//        
//        create(invoice);
//        getEntityManager().flush();
//        
//        for(InvoiceItems woi: items){
//            woi.setInvoice(invoice);
//            woi.setDeleted("N");
//            getEntityManager().persist(woi);
//            getEntityManager().flush();
//        }
//        
//        invoice.setInvoiceItems(items);
//        
//        getEntityManager().merge(invoice);
//        
//        return invoice;
//        
//    }
//    
//    
//    public Invoice updateInvoice(Invoice invoice) {
//        preSaveActions(invoice);
//        
//        invoice = edit(invoice);            
//        
//        return invoice;
//    }
//    
//    public void softDelete(Invoice current) {
//        current.setStatus(PaymentStatusEnum.DELETED.name());
//
//        getEntityManager().merge(current);
//    }
//
//    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//    public Transaction addTransaction(Transaction trans) {
//        if (trans != null) {
//            if (trans.getTranId() == null) {
//                trans.setCreateTs(new Date());
//                getEntityManager().persist(trans);
//            } else {
//                trans.setUpdateTs(new Date());
//                trans = getEntityManager().merge(trans);
//            }
//            
//            Invoice invoice = getEntityManager().find(Invoice.class,trans.getInvoiceId().getInvoiceId());
//            
//            if(invoice.getAmount() == invoice.getTransactionAmount()){
//                invoice.setStatus(PaymentStatusEnum.PAID.name());
//            }
//            else if(invoice.getAmount() > invoice.getTransactionAmount()){
//                invoice.setStatus(PaymentStatusEnum.PARTIALPAY.name());
//            }
//            else if(invoice.getAmount() < invoice.getTransactionAmount()){
//                invoice.setStatus(PaymentStatusEnum.OVERPAY.name());
//            }            
//            
//            getEntityManager().merge(invoice);
//            
//        }
//        return trans;
//    }
//    
//    public Invoice postTransaction(Transaction trans) {
//        Invoice invoice = null;
//
//        if (trans != null) {
//            invoice = trans.getInvoiceId();
//
//            if (trans.getTranId() == null) {
//                trans.setCreateTs(new Date());
//            } else {
//                trans.setUpdateTs(new Date());
//            }
//            
//            
//            if(invoice.getAmount().equals(invoice.getTransactionAmount())){
//                invoice.setStatus(PaymentStatusEnum.PAID.name());
//            }
//            else if(invoice.getAmount() > invoice.getTransactionAmount()){
//                invoice.setStatus(PaymentStatusEnum.PARTIALPAY.name());
//            }
//            else if(invoice.getAmount() < invoice.getTransactionAmount()){
//                invoice.setStatus(PaymentStatusEnum.OVERPAY.name());
//            }            
//            
//            getEntityManager().merge(invoice);
//            getEntityManager().flush();
//            
//        }
//        return invoice;
//    }
//
//    public Invoice getInvoice(int invoiceId) {
//        Invoice invoice = getEntityManager().find(Invoice.class,invoiceId);
////        invoice.getTransactions();
//        
//        return invoice;
//    }
//
//    private Invoice addNewItemsIfNeeded(Invoice invoice) {
//        List<InvoiceItems> newItemsList = new ArrayList<>();
//
//        for (InvoiceItems items : invoice.getInvoiceItems()) {
//            if (items.isAddItem()) {
//                newItemsList.add(items);
//            }
//        }
//
//        if (Utils.notEmpty(newItemsList)) {
//            for (InvoiceItems invItem : newItemsList) {
//                invItem.setItem(itemSvc.saveItem(new Item(invItem.getDescription(), invItem.getUnitPrice(), "product")));
//            }
//        }
//
//        return invoice;
//    }
//
//    private Invoice preSaveActions(Invoice invoice) {        
//        //Save new vehicles.
//        invoice.getVehicle().setCustomerId(invoice.getCustomerId());
//        invoice.setVehicle(custSvc.saveCustomerVehicle(invoice.getVehicle()));
//            
//        //Add new items, if needed.
//        if (Utils.notEmpty(invoice.getInvoiceItems())) {                      
//            invoice = addNewItemsIfNeeded(invoice);                        
//        }
//        
//        return invoice;
//    }

}
