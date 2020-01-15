package fri.kp.rest.generic.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class GenericItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany
    private List<GenericItem> itemList;
}
