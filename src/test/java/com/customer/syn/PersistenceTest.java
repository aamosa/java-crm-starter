package com.customer.syn;

import com.customer.syn.model.Contact;
import com.customer.syn.service.ContactService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class PersistenceTest {


    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, PersistenceTest.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web.xml");
    }

    @EJB
    ContactService contactService;


    @Test
    public void saveEntityAndFetch() {
        Contact contact = new Contact("JOHN", "DOE", "jdoe@gmail.com");
        contactService.save(contact);
        assertTrue(contactService.exists(contact));
    }


}
