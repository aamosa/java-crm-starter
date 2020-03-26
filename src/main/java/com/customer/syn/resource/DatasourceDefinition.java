package com.customer.syn.resource;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;


@Singleton
@Startup
@DataSourceDefinition(
    name = "jdbc/h2_datasource",
    className = "org.h2.jdbcx.JdbcDataSource",
    url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    user = "sa",
    password = "")
public class DatasourceDefinition {}
