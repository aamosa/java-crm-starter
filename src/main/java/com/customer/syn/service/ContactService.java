package com.customer.syn.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customer.syn.resource.model.Contact;

/**
 * EJB class for managing Contact JPA entities.
 */
@Stateless
public class ContactService extends BaseRepositoryImpl<Contact, Long> {
    
    @PersistenceContext(unitName = "syn")
    private EntityManager em;
    
    public ContactService() {
        super();
    }

    /**
     * @param lastName string representing the last name to lookup.
     * @return a <code>List&lt;Contact&gt;</code> of entities.
     */
    public List<Contact> findByLastName(String lastName) {
        return em.createNamedQuery("Contact.getByLastName", Contact.class)
                .setParameter("lastName", lastName)
                .getResultList();
    }
    

    /**
     * @param firstName string representing the first name.
     * @param lastName  string representing the last name.
     * @return a <code>list&lt;Contact&gt;</code> of entities.
     */
    public List<Contact> findByFullName(String firstName, String lastName) {
        return em.createNamedQuery("Contact.getByFullName", Contact.class)
                .setParameter("fistName", firstName)
                .setParameter("lastName", lastName)
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
        return em.createNamedQuery("Contact.getByDateRange")
                .setParameter("from", from.atStartOfDay().toInstant(ZoneOffset.UTC))
                .setParameter("to", to.atStartOfDay().toInstant(ZoneOffset.UTC))
                .getResultList();
    }


}
