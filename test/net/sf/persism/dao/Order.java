package net.sf.persism.dao;

import net.sf.persism.PersistableObject;
import net.sf.persism.annotations.Column;

import java.util.Date;

/**
 * $RCSfile: $
 * $Revision: $
 * $Date: $
 * Created by IntelliJ IDEA.
 * User: DHoward
 * Date: 9/21/11
 * Time: 2:30 PM
 */
public class Order extends PersistableObject {

    private long id;
    private String name;
    private Date created;
    private String customerId;
    private boolean paid;


    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", customerId='" + customerId + '\'' +
                ", paid=" + paid +
                '}';
    }
}
