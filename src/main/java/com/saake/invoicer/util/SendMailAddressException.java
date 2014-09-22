package com.saake.invoicer.util;

import javax.mail.Address;

public class SendMailAddressException extends Exception
{
    private Address[] validUnsent;
    private Address[] invalid;
    private Address[] sendTo;

    public SendMailAddressException(String s, Address[] validUnsent, Address[] invalid, Address[] sendTo) {
        super(s);
        this.validUnsent = validUnsent;
        this.invalid = invalid;
        this.sendTo = sendTo;
    }

    public SendMailAddressException(Address[] validUnsent, Address[] invalid, Address[] sendTo) {
        this.validUnsent = validUnsent;
        this.invalid = invalid;
        this.sendTo = sendTo;
    }

    public Address[] getValidUnsent() {
        return validUnsent;
    }

    public String getValidUnsentString(){
        return generateString(validUnsent);
    }
    public void setValidUnsent(Address[] validUnsent) {
        this.validUnsent = validUnsent;
    }

    public Address[] getInvalid() {
        return invalid;
    }

    public String getInvalidString(){
        return generateString(invalid);
    }

    private String generateString(Address[] add) {
        StringBuffer sb = new StringBuffer();
        if (add != null){
            for (int i=0;i<add.length;i++){
                Address ia = add[i];
                sb.append(ia.toString() + ", ");
            }
        }
        if (sb.length() > 3) {
            sb.substring(0, sb.length() - 3);
        }
        return sb.toString();
    }

    public void setInvalid(Address[] invalid) {
        this.invalid = invalid;
    }

    public Address[] getSendTo() {
        return sendTo;
    }

    public void setSendTo(Address[] sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendToString(){
        return generateString(sendTo);
    }


    public Address[] getAllNotSent() {
        Address[] notSent = null;
        if ((validUnsent != null) && (invalid != null)) {
            notSent = new Address[validUnsent.length + invalid.length];
            for (int i=0;i<validUnsent.length;i++){
                notSent[i] = validUnsent[i];
            }
            for (int i=0;i<invalid.length;i++){
                notSent[validUnsent.length + i] = invalid[i];
            }
        } else if (validUnsent != null){
            notSent = validUnsent;
        } else if (invalid != null){
            notSent = invalid;
        }
        return notSent;
    }
}
