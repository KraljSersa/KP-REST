package fri.kp.rest.generic.entities;

import javax.persistence.*;

@Entity
public class GenericItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private int amount;

    private double cost;

    @ManyToOne
    private GenericItemList itemList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public GenericItemList getItemList() {
        return itemList;
    }

    public void setItemList(GenericItemList itemList) {
        this.itemList = itemList;
    }
}
