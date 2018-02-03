package com.sm.dropwizard.service;

import com.sm.dropwizard.dao.ConfigDao;
import com.sm.dropwizard.model.Config;
import org.apache.zookeeper.KeeperException;

public class ConfigService {
    private final ConfigDao configDao;

    public ConfigService(ConfigDao configDao){
        this.configDao = configDao;
    }

    public Config getConfig(String path) throws InterruptedException, KeeperException{
        return configDao.getConfig(path);
    }
    //update
    public Config updateConfig(String path,String data) throws InterruptedException, KeeperException{
        return configDao.updateConfig(path,data);
    }
}
