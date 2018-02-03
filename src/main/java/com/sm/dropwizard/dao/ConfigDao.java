package com.sm.dropwizard.dao;

import com.sm.dropwizard.model.Config;
import com.sm.zookeeper.ZkClientManagerImpl;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.zookeeper.KeeperException;
import org.hibernate.SessionFactory;

public class ConfigDao extends AbstractDAO<Config>
{

    final private ZkClientManagerImpl manager = new ZkClientManagerImpl();

    public ConfigDao(SessionFactory sessionFactory) { super(sessionFactory); }

    public Config getConfig(String path) throws InterruptedException , KeeperException{
        Config config = new Config();
        String message = manager.getZNodeData("/" + path,false).toString();
        config.setMessage(message);
        return config;
    }

    public Config updateConfig(String path,String data) throws InterruptedException , KeeperException{
        Config config = new Config();
        manager.update("/" + path,data.getBytes());
        String message = manager.getZNodeData("/" + path,false).toString();
        config.setMessage(message);
        return config;
    }



}
