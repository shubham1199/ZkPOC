package com.sm.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

@Slf4j
public class ZkClientManagerImpl implements ZkManager {

    private static ZooKeeper zkeeper;

    private static ZkConnection zkConnection;


    public ZkClientManagerImpl() {
        initialize();
    }

    /**
     * Initialize connection
     */
    private void initialize() {
        try {
            zkConnection = new ZkConnection();
            zkeeper = zkConnection.connect("127.0.0.1:2180");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Close the zookeeper connection
     */
    public void closeConnection() {
        try {
            zkConnection.close();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void create(String path, byte[] data) throws KeeperException,
            InterruptedException {
        zkeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

    }

    @Override
    public Stat getZNodeStats(String path) throws KeeperException,
            InterruptedException {
        Stat stat = zkeeper.exists(path, true);
        if (stat != null) {
            log.info("Node exists and the node version is "
                    + stat.getVersion());
        } else {
            log.error("Node does not exists");
        }
        return stat;
    }

    @Override
    public Object getZNodeData(final String path, boolean watchFlag) throws KeeperException,
            InterruptedException {


        final CountDownLatch connectedSignal = new CountDownLatch(1);
        try {

            Stat stat = getZNodeStats(path);
            byte[] nodeData = null;
            if (stat != null) {
				nodeData = zkeeper.getData(path, new Watcher() {

					public void process(WatchedEvent we) {

						if (we.getType() == Event.EventType.None) {
							switch (we.getState()) {
							case Expired:
								connectedSignal.countDown();
								break;
							}

						} else {

							try {
							    //watcher check
								connectedSignal.countDown();

							} catch (Exception ex) {
								log.warn(ex.getMessage());
							}
						}
					}
				}, null);

                String data = new String(nodeData, "UTF-8");
                return data;
            } else {
                System.out.println("Node does not exists");
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public void update(String path, byte[] data) throws KeeperException,
            InterruptedException {
        int version = zkeeper.exists(path, true).getVersion();
        zkeeper.setData(path, data, version);

    }

    @Override
    public List<String> getZNodeChildren(String path) throws KeeperException,
            InterruptedException {
        Stat stat = getZNodeStats(path);
        List<String> children  = null;

        if (stat != null) {
            children = zkeeper.getChildren(path, false);
            for (int i = 0; i < children.size(); i++)
                log.warn(children.get(i));

        } else {
            log.warn("Node does not exists");
        }
        return children;
    }

    @Override
    public void delete(String path) throws KeeperException,
            InterruptedException {
        int version = zkeeper.exists(path, true).getVersion();
        zkeeper.delete(path, version);

    }

}
