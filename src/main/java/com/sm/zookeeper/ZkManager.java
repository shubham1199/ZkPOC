package com.sm.zookeeper;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

public interface ZkManager {
    public void create(String path, byte[] data) throws KeeperException,
            InterruptedException;

    public Stat getZNodeStats(String path) throws KeeperException,
            InterruptedException;

    public Object getZNodeData(String path,boolean watchFlag) throws KeeperException,
            InterruptedException;

    public void update(String path, byte[] data) throws KeeperException,
            InterruptedException;

    public List<String> getZNodeChildren(String path) throws KeeperException,
            InterruptedException;

    public void delete(String path) throws KeeperException,
            InterruptedException;
}