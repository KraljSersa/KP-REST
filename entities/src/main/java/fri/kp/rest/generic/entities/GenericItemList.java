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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<GenericItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<GenericItem> itemList) {
        this.itemList = itemList;
    }

    public void addItem(GenericItem item) {
        itemList.add(item);
    }

    public void removeItem(GenericItem item) {
        itemList.forEach(i -> {
            if (i.getId().equals(item.getId())) {
                itemList.remove(item);
            }
        });
    }
}
