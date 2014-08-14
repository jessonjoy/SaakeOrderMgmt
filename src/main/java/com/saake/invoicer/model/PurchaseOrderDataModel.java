/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.model;

import com.saake.invoicer.entity.PurchaseOrder;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author jn
 */
public class PurchaseOrderDataModel extends ListDataModel<PurchaseOrder> implements Serializable,SelectableDataModel<PurchaseOrder> { 
    
    public PurchaseOrderDataModel() {
    }
    
    
    public PurchaseOrderDataModel(List<PurchaseOrder> data) {
        super(data);
    }

    @Override
    public Object getRowKey(PurchaseOrder t) {
        return t.getPurchaseOrderId();
    }

    @Override
    public PurchaseOrder getRowData(String rowKey) {
        List<PurchaseOrder> list = (List<PurchaseOrder>) getWrappedData();  
          
        for(PurchaseOrder inv : list) {  
            if(inv.getPurchaseOrderId().equals(rowKey))  
                return inv;  
        }  
          
        return null; 
    }        
}
