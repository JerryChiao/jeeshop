package org.rembx.jeeshop.catalog.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.Set;

/**
 * Stock keeping unit
 */
@Entity
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SKU extends CatalogItem{

    private Double price;

    @Size(min=3, max = 3)
    @Column(length = 3)
    private String currency;

    @Size(max = 50)
    @Column(length = 50)
    private String reference;

    private Integer threshold;

    private Integer quantity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "skuId"),
            inverseJoinColumns = @JoinColumn(name = "presentationId"))
    private Set<Presentation> presentations;

    /**
     * Calculated field true if quantity > threshold
     */
    @Transient
    private Boolean available;

    public SKU() {
    }

    public SKU(String name, String description, Double price, Integer quantity, String reference,
               Date endDate, Date startDate, Boolean disabled, Integer threshold) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.reference = reference;
        this.disabled = disabled;
        this.startDate = startDate;
        this.endDate = endDate;
        this.threshold = threshold;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void computeAvailable() {
        available = quantity >threshold;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<Presentation> getPresentations() {
        return presentations;
    }

    public void setPresentations(Set<Presentation> presentations) {
        this.presentations = presentations;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SKU sku = (SKU) o;

        if (available != null ? !available.equals(sku.available) : sku.available != null) return false;
        if (currency != null ? !currency.equals(sku.currency) : sku.currency != null) return false;
        if (price != null ? !price.equals(sku.price) : sku.price != null) return false;
        if (quantity != null ? !quantity.equals(sku.quantity) : sku.quantity != null) return false;
        if (reference != null ? !reference.equals(sku.reference) : sku.reference != null) return false;
        if (threshold != null ? !threshold.equals(sku.threshold) : sku.threshold != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (threshold != null ? threshold.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (available != null ? available.hashCode() : 0);
        return result;
    }
}