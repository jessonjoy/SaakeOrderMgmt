/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.entity.Vehicle;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author jn
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {
    

    public CustomerFacade() {
        super(Customer.class);
    }

    public Vehicle saveCustomerVehicle(Vehicle custVehicle) {
        if(custVehicle.getCustVehicleId() == null){
            getEntityManager().persist(custVehicle);
            getEntityManager().flush();
        }
        else{
            if(!custVehicle.equals(getEntityManager().find(custVehicle.getClass(),custVehicle.getCustVehicleId()))){
                custVehicle = getEntityManager().merge(custVehicle);
                getEntityManager().flush();
            }
        }                
        
        return custVehicle;
    }
           
    @Override
    public List<Customer> findAll() {
//
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//
//        Root<Item> root = cq.from(ItgetEntityManager().class);
//        cq.select(root);
//
//        ParameterExpression<String> status = cb.parameter(String.class);
////        cq.where(cb.notEqual(invRoot.get("status"), status));
//        cq.orderBy(cb.desc(root.get("itemId")));
//        Query query = getEntityManager().createQuery(cq);
////        query.setParameter(status, InvoiceStatusEnum.DELETE.getValue());

        return (List<Customer>) getEntityManager().createQuery("select t from Customer t where t.status is null or t.status = 'INACTIVE' order by t.customerId desc", Customer.class).getResultList();
    }

    public void softDelete(Customer current) {
        current.setStatus("DELETED");

        getEntityManager().merge(current);
    }

    public Vehicle getVehicle(Integer id) {
        return (Vehicle) getEntityManager().createNamedQuery("Vehicle.findByCustVehicleId").setParameter("vehicleId", id).getResultList().get(0);
    }
    
}
