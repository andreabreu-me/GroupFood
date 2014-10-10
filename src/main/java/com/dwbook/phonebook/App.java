package com.dwbook.phonebook;

import com.dwbook.phonebook.resources.ContactResource;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.jdbi.DBIFactory;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Hello world!
 */
public class App extends Application<PhonebookConfiguration> {

    private static final Logger LOGGER =LoggerFactory.getLogger(App.class);

    @Override
    public void initialize(Bootstrap<PhonebookConfiguration> configurationBootstrap) {

    }

    @Override
    public void run(PhonebookConfiguration c, Environment e) throws Exception {
        LOGGER.info("Method App#run() called");
        for (int i=0; i < c.getMessageRepetitions(); i++) {
            System.out.println(c.getMessage());
        }

        // Create a DBI factory and build a JDBI instance
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory
                .build(e, c.getDataSourceFactory(), "mysql");
        // Add the resource to the environment
        e.jersey().register(new ContactResource(jdbi));

        e.jersey().register(new BasicAuthProvider<Boolean>(
                new PhonebookAuthenticator(), "Web Service Realm"));
    }

    public static void main( String[] args ) throws Exception{
        new App().run(args);
    }
}
