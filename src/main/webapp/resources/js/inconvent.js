/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function jq(myid) {
   return '#' + myid.replace(/(:|\.)/g,'\\$1');
}

function showComp(id){
    jQuery(jq(id)).show('slow');
}

function hideComp(id, args){
    jQuery(jq(id)).hide('slow');
}

function nextComp(fromCls,toCls){
    jQuery(fromCls).hide('drop',{ direction:'up' },'slow');    
    jQuery(toCls).show('drop',{ direction:'down' },'slow');    
}

function addCSS(id){

    alert(id);
    jQuery(jq(id)).addClass(ui-widget-overlay);
}