package com.customer.syn.resource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.sql.DataSource;

import com.customer.syn.resource.model.Role;
import com.customer.syn.resource.model.User;
import com.customer.syn.service.UserService;

@Singleton
@Startup
@DataSourceDefinition(name = "java:app/jdbc/h2_datasource",
                      className = "org.h2.jdbcx.JdbcDataSource",
                      url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                      user = "sa",
                      password = "")
public class DatasourceDefinition {

    @Resource(lookup = "java:app/jdbc/h2_datasource")
    private DataSource dataSource;

    @Inject
    UserService userService;

    @Inject
    private Pbkdf2PasswordHash hash;

    @PostConstruct
    public void init() {
        User user = new User("Admin", "Web", "ADMIN", "passw0rd");
        user.addRole(new Role("ALLOW_ACCESS", user));
        userService.save(user);
    }

//    private void initHashParams() {
//        Map<String, String> params = new HashMap<>();
//        params.put("Pbkdf2PasswordHash.Iterations", "3072");
//        params.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
//        params.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
//        hash.initialize(params);
//    }

}