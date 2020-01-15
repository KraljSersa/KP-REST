package fri.kp.rest.generic.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import fri.kp.rest.generic.entities.GenericItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

public class GenericItemBean {
    private static Logger LOGGER = Logger.getLogger(GenericItemBean.class.getName());

    @PersistenceContext(unitName = "kp-rest-service-jpa")
    private EntityManager em;

    public List<GenericItem> getAll(QueryParameters query){
        List<GenericItem> items = JPAUtils.queryEntities(em, GenericItem.class, query);
        return items;
    }

    public Long getItemListCount(QueryParameters query) {
        Long count = JPAUtils.queryEntitiesCount(em, GenericItem.class, query);
        return count;
    }

    public GenericItem getItemById(int id){
        return (GenericItem) em.createNamedQuery("GenericItem.getById").setParameter("id",id).getSingleResult();
    }

    @Transactional
    public GenericItem createItem( GenericItem item1){
        GenericItem item = new GenericItem();
        item.setAmount(item1.getAmount());
        item.setCost(item1.getCost());
        item.setDescription(item1.getDescription());
        item.setName(item1.getName());

        em.persist(item);

        return item;
    }

    @Transactional
    public boolean deleteItem(int id){
        GenericItem remove = em.find(GenericItem.class, id);

        if (remove != null) {
            try{
                em.remove(remove);
                return true;
            }catch (Exception e){
                LOGGER.severe(e.toString());
            }
        }
        return false;
    }

    @Transactional
    public GenericItem updateItem(GenericItem item, int id) {
        GenericItem update = em.find(GenericItem.class, id);
        if (item != null && update != null) {
            update.setName(item.getName());
            update.setDescription(item.getDescription());
            update.setAmount(item.getAmount());
            update.setCost(item.getCost());
            update.setItemList(item.getItemList());
            update = em.merge(update);
            return update;
        }
        return null;
    }
}
