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
}
