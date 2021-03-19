package net.sf.persism.dao;

import net.sf.persism.annotations.Column;

public final class DumbTableStringAutoInc {
    private String id;

    public String getId() {
        return id;
    }

    @Column(autoIncrement = true, primary = true)
    public void setId(String id) {
        this.id = id;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
