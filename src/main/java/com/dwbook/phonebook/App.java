package com.dwbook.phonebook;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwbook.phonebook.resources.AdminResource;
import com.dwbook.phonebook.resources.ContactResource;
import com.dwbook.phonebook.resources.FacebookResource;
import com.dwbook.phonebook.resources.FriendResource;
import com.dwbook.phonebook.resources.GroupResource;
import com.dwbook.phonebook.resources.ItemResource;
import com.dwbook.phonebook.resources.MerchantResource;
import com.dwbook.phonebook.resources.MessageResource;
import com.dwbook.phonebook.resources.OrderDetailResource;
import com.dwbook.phonebook.resources.OrderMerchantResource;
import com.dwbook.phonebook.resources.OrderResource;
import com.dwbook.phonebook.resources.OrderUserResource;
import com.dwbook.phonebook.resources.UserResource;

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
        final DBI jdbi = factory.build(e, c.getDataSourceFactory(), "mysql");

        e.jersey().register(new ContactResource(jdbi));
        e.jersey().register(new GroupResource(jdbi));
        e.jersey().register(new UserResource(jdbi));
        e.jersey().register(new FacebookResource(jdbi));
        e.jersey().register(new AdminResource(jdbi));
        e.jersey().register(new FriendResource(jdbi));
        e.jersey().register(new MessageResource(jdbi));
        e.jersey().register(new MerchantResource(jdbi));
        e.jersey().register(new ItemResource(jdbi));
        e.jersey().register(new OrderResource(jdbi));
        e.jersey().register(new OrderUserResource(jdbi));
        e.jersey().register(new OrderMerchantResource(jdbi));
        e.jersey().register(new OrderDetailResource(jdbi));

        e.jersey().register(new BasicAuthProvider<Boolean>(
                new PhonebookAuthenticator(), "Web Service Realm"));
    }

    public static void main( String[] args ) throws Exception{
        new App().run(args);
    }
}
