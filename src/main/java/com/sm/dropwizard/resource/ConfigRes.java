package com.sm.dropwizard.resource;


import com.sm.dropwizard.model.Config;
import com.sm.dropwizard.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;


@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ConfigRes {
    private final ConfigService configService;
    private Client client;

    public ConfigRes(ConfigService configService, Client client) {
        this.configService = configService;
        this.client = client;
    }

    @GET
    @Path("/{path}")
    public Config getConfig(@PathParam("path") String path) throws InterruptedException,KeeperException {
        return configService.getConfig(path);
    }

    @PUT
    @Path("/{path}")
    public Config updateConfig(@PathParam("path") String path,Config config) throws InterruptedException,KeeperException {
        return configService.updateConfig(path,config.getMessage());
    }
}