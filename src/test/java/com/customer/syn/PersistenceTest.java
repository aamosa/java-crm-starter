package com.customer.syn;

import com.customer.syn.model.Contact;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.BasicRepository;
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
        WebArchive web = ShrinkWrap.create(WebArchive.class)
                .addPackage("com.customer.syn.model")
                .addClasses(ContactService.class, BaseRepositoryImpl.class, BasicRepository.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web.xml");
        System.out.println(web.toString()); // TOOD: remove this
        return web;
    }

    @EJB ContactService contactService;


    @Test
    public void saveEntityAndFetch() {
        Contact contact = new Contact("JOHN", "DOE", "jdoe@gmail.com");
        contactService.save(contact);
        assertTrue(contactService.exists(contact));
    }


}
