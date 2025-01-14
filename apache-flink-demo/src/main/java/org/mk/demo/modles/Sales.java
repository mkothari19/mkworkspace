package org.mk.demo.modles;

import java.io.Serializable;
import java.util.Objects;

public class Sales implements Serializable {

    private String date;
    private String month;
    private String category;
    private String product;
    private Integer sales;
    private Integer count;

    public Sales() {
    }

    public Sales(String date, String month, String category, String product, Integer sales, Integer count) {
        this.date = date;
        this.month = month;
        this.category = category;
        this.product = product;
        this.sales = sales;
        this.count=count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sales sales = (Sales) o;
        return getMonth().equals(sales.getMonth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMonth());
    }

    @Override
    public String toString() {
        return "Sales{" +
                "date='" + date + '\'' +
                ", month='" + month + '\'' +
                ", category='" + category + '\'' +
                ", product='" + product + '\'' +
                ", sales=" + sales +
                ", count=" + count +
                '}';
    }
}
