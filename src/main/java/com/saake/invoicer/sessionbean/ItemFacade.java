/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Item;
import com.saake.invoicer.util.Utils;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author jn
 */
@Stateless
public class ItemFacade extends AbstractFacade<Item> {


    public ItemFacade() {
        super(Item.class);
    }

    @Override
    public List<Item> findAll() {
//
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//
//        Root<Item> root = cq.from(Item.class);
//        cq.select(root);
//
//        ParameterExpression<String> status = cb.parameter(String.class);
////        cq.where(cb.notEqual(invRoot.get("status"), status));
//        cq.orderBy(cb.desc(root.get("itemId")));
//        Query query = getEntityManager().createQuery(cq);
////        query.setParameter(status, InvoiceStatusEnum.DELETE.getValue());

        return (List<Item>) getEntityManager().createQuery("select t from Item t where t.status is null or t.status = 'INACTIVE' order by t.itemId desc", Item.class).getResultList();
    }
    
    public List<Item> findSugItems(String inp) {
        String qryStr="select t from Item t where " +
            "upper(t.itemCode) like '%" + inp.toUpperCase() + "%' or upper(t.description) like '%" + inp.toUpperCase() + 
            "%' or upper(t.name) like '%" + inp.toUpperCase() + "%' order by t.itemId desc";
        return (List<Item>) getEntityManager().createQuery(qryStr, Item.class).getResultList();
    }

    public void softDelete(Item item) {
        item.setStatus("DELETED");

        getEntityManager().merge(item);
    }

    public void deactivate(Item item) {
        item.setStatus("INACTIVE");

        getEntityManager().merge(item);
    }

    public List<Item> createItems(List<Item> newItemsList) {
        for (Item item : newItemsList) {
            if(!item.isEmptyForUse()){
                if(Utils.isBlank(item.getItemCategory())){
                    item.setItemCategory("PRODUCT");
                }
                item = save(item);
            }
        }
        
        return newItemsList;
    }    

    public Item saveItem(Item item) {
//        if(!item.isEmptyForUse()){

            item = save(item);
//        }
        
        return item;
    }    
    
    private Item save(Item item) {
        if(item.getItemId()== null){
            item.setCreateTs(new Date());
            getEntityManager().persist(item);
        }
        else{
            item.setUpdateTs(new Date());
            item = getEntityManager().merge(item);
        }
        
        return item;
    }
}
