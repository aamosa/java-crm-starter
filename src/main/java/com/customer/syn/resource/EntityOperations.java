package com.customer.syn.resource;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * EJB class for managing entities.
 * 
 */
@Stateless
public class EntityOperations {

    @PersistenceContext(unitName = "syn")
    private EntityManager em;

    /**
     * Find entity by the specified primary key.
     * 
     * @param id the <code>Id</code> primary key.
     * @return the entity associated with the <code>Id</code>; otherwise
     *         <code>null</code>.
     */
    public Customer findByID(int id) {
        return em.find(Customer.class, id);
    }

    /**
     * @return a <code>List&lt;Customer&gt;</code> of all the managed entities.
     */
    @SuppressWarnings("unchecked")
    public List<Customer> findAll() {
        return em.createNamedQuery("Customer.getAll").getResultList();
    }

    /**
     * Save the specified entity.
     * 
     * @param ce the <code>Customer</code> entity to save.
     */
    public void save(Customer ce) {
        em.persist(ce);
    }

    /**
     * Delete the specified entity.
     * 
     * @param id the primary key associated with the entity.
     */
    public void deleteEntity(Integer id) {
        em.remove(findByID(id));
    }

    /**
     * @param lastName string representing the last name to lookup.
     * @return a <code>List&lt;Customer&gt;</code> of entities.
     */
    @SuppressWarnings("unchecked")
    public List<Customer> findByLastName(String lastName) {
        return em.createNamedQuery("Customer.getByLastName").setParameter("lastName", lastName)
                .getResultList();
    }

    /**
     * 
     * @param from <code>LocalDate</code> object representing the <em>from</em> date
     *             starting at midnight.
     * @param to   <code>LocalDate</code> object representing the <em>to</em> date
     *             ending at midnight.
     * @return a <code>List&lt;Customer&gt;</code> of entities.
     */
    @SuppressWarnings("unchecked")
    public List<Customer> findByDateRange(LocalDate from, LocalDate to) {
        return em.createNamedQuery("Customer.getByDateRange").setParameter("from", from.atStartOfDay())
                .setParameter("to", to.atStartOfDay()).getResultList();
    }

    /**
     * @param firstName string representing the first name.
     * @param lastName  string representing the last name.
     * @return a <code>list&lt;Customer&gt;</code> of entities.
     */
    @SuppressWarnings("unchecked")
    public List<Customer> findByFullName(String firstName, String lastName) {
        return em.createNamedQuery("Customer.getByFullName").setParameter("firstName", firstName)
                .setParameter("lastName", lastName).getResultList();
    }

    /**
     * Updates the managed entity into the current persistence context.
     * 
     * @param ce the <code>Customer</code> entity to update.
     */
    public void mergeEntity(Customer ce) {
        em.merge(ce);
    }
    
}
