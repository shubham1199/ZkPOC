package com.sm.dropwizard;

import com.sm.dropwizard.dao.ConfigDao;
import com.sm.dropwizard.model.Config;
import com.sm.dropwizard.resource.ConfigRes;
import com.sm.dropwizard.service.ConfigService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class DropwizardApplication extends Application<DropwizardConfiguration> {
    public static void main(String[] args) throws Exception {
        new DropwizardApplication().run(args);
    }

    private final HibernateBundle<DropwizardConfiguration> hibernateBundle =
            new HibernateBundle<DropwizardConfiguration>(Config.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(DropwizardConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "dropwizard";
    }

    @Override
    public void initialize(Bootstrap<DropwizardConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(DropwizardConfiguration configuration, Environment environment) {
        ConfigDao configDao = new ConfigDao(hibernateBundle.getSessionFactory());
        ConfigService configService = new ConfigService(configDao);

        Client client = ClientBuilder.newClient();
        //final HttpClient client = HttpClientBuilder.create().build();
        environment.jersey().register(new ConfigRes(configService,client));
    }
}
