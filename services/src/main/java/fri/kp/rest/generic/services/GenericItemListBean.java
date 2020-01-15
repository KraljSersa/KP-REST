package fri.kp.rest.generic.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import fri.kp.rest.generic.entities.GenericItem;
import fri.kp.rest.generic.entities.GenericItemList;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

public class GenericItemListBean {
    private static Logger LOGGER = Logger.getLogger(GenericItemListBean.class.getName());

    @Inject
    private GenericItemBean genericItemBean;

    @PersistenceContext(unitName = "kp-rest-service-jpa")
    private EntityManager em;

    public List<GenericItemList> getGenericLists(QueryParameters query) {
        List<GenericItemList> genericLists = JPAUtils.queryEntities(em, GenericItemList.class, query);

        if (genericLists.isEmpty()) {
            LOGGER.severe("genericList table is empty.");
        }

        return genericLists;
    }

    public Long getGenericListCount(QueryParameters query) {
        Long count = JPAUtils.queryEntitiesCount(em, GenericItemList.class, query);
        return count;
    }

    public GenericItemList getGenericListById(int id) {
        GenericItemList genericList = em.find(GenericItemList.class, id);

        if (genericList == null) {
            LOGGER.severe("Did not find generic list with id: " + id);
        }

        return genericList;
    }

    @Transactional
    public GenericItemList createGenericList() {
        GenericItemList genericList = new GenericItemList();
        em.persist(genericList);

        return genericList;
    }

    @Transactional
    public boolean deleteGenericList(int id) {
        GenericItemList genericList = em.find(GenericItemList.class, id);
        try {
            em.remove(genericList);
        } catch (Exception e) {
            LOGGER.severe(e.toString());
            return false;
        }

        return true;
    }

    @Transactional
    public boolean addItemToGenericList(GenericItem item, int id) {
        GenericItemList genericList = em.find(GenericItemList.class, id);
        if (genericList != null) {
            GenericItem created = genericItemBean.createItem(item);
            genericList.addItem(created);
            em.merge(genericList);
            //genericList.setUpdateTime(Instant.now());
        } else {
            LOGGER.config("Did not find generic list with id: " + id);
            return  false;
        }

        return  true;
    }

    @Transactional
    public boolean removeItemFromGenericList(GenericItem item, int id) {
        GenericItemList genericList = em.find(GenericItemList.class, id);
        if (genericList != null) {
            genericList.removeItem(item);
            //genericList.setUpdateTime(Instant.now());
            return true;
        } else {
            LOGGER.config("Did not find generic list with id: " + id);
            return false;
        }
    }
}
