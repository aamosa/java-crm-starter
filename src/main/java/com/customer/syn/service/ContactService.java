package com.customer.syn.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customer.syn.resource.model.Contact;

/**
 * EJB class for managing entities.
 */
@Stateless
public class ContactService {
    
    @PersistenceContext(unitName = "syn")
    private EntityManager em;

    /**
     * Find entity by the specified primary key.
     * 
     * @param id the <code>Id</code> primary key.
     * @return the entity associated with the <code>Id</code>; otherwise
     *         <code>null</code>.
     */
    public Contact findByID(Long id) {
        return em.find(Contact.class, id);
    }

    /**
     * Fetch all entities.
     * 
     * @return a <code>List&lt;Contact&gt;</code> of all the managed entities.
     */
    @SuppressWarnings("unchecked")
    public List<Contact> findAll() {
        return em.createNamedQuery("Contact.getAll").getResultList();
    }

    /**
     * Save the specified entity.
     * 
     * @param ce the <code>Contact</code> entity to save.
     */
    public void save(Contact ce) {
        em.persist(ce);
    }

    /**
     * Delete the specified entity.
     * 
     * @param id the primary key associated with the entity.
     */
    public void deleteEntity(Long id) {
        em.remove(findByID(id));
    }

    /**
     * Update the specified entity in the current persistence context.
     * 
     * @param ce the <code>Contact</code> entity to update.
     */
    public void mergeEntity(Contact ce) {
        em.merge(ce);
    }

    /**
     * @param lastName string representing the last name to lookup.
     * @return a <code>List&lt;Contact&gt;</code> of entities.
     */
    @SuppressWarnings("unchecked")
    public List<Contact> findByLastName(String lastName) {
        return em.createNamedQuery("Contact.getByLastName").setParameter("lastName", lastName)
                .getResultList();
    }

    /**
     * Fetch entities created in the specified date range.
     * 
     * @param from <code>LocalDate</code> object representing the <em>from</em> date
     *             starting at midnight.
     * @param to   <code>LocalDate</code> object representing the <em>to</em> date
     *             ending at midnight.
     * @return a <code>List&lt;Contact&gt;</code> of entities.
     */
    @SuppressWarnings("unchecked")
    public List<Contact> findByDateRange(LocalDate from, LocalDate to) {
        return em.createNamedQuery("Contact.getByDateRange").setParameter("from", from.atStartOfDay().toInstant(ZoneOffset.UTC))
                .setParameter("to", to.atStartOfDay().toInstant(ZoneOffset.UTC))
                .getResultList();
    }

    /**
     * @param firstName string representing the first name.
     * @param lastName  string representing the last name.
     * @return a <code>list&lt;Contact&gt;</code> of entities.
     */
    @SuppressWarnings("unchecked")
    public List<Contact> findByFullName(String firstName, String lastName) {
        return em.createNamedQuery("Contact.getByFullName").setParameter("firstName", firstName)
                .setParameter("lastName", lastName).getResultList();
    }

}
