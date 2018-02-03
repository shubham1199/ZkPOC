package com.sm.zookeeper;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class ZkClientTest {

    private static ZkClientManagerImpl zkmanager = new ZkClientManagerImpl();
    // ZNode Path
    private String path = "/QN-GBZnode";
    byte[] data = "This is  test data string.".getBytes();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        log.warn("Test Set-up");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        // data in byte array
        log.warn("Test__1");
        zkmanager.create(path, data);
        Stat stat = zkmanager.getZNodeStats(path);
        assertNotNull(stat);
        zkmanager.delete(path);
    }


    @Test
    public void testGetZNodeStats() throws KeeperException,
            InterruptedException {
        zkmanager.create(path, data);
        Stat stat = zkmanager.getZNodeStats(path);
        assertNotNull(stat);
        assertNotNull(stat.getVersion());
        zkmanager.delete(path);

    }


    @Test
    public void testGetZNodeData() throws KeeperException, InterruptedException {
        zkmanager.create(path, data);
        String data = (String)zkmanager.getZNodeData(path,false);
        assertNotNull(data);
        zkmanager.delete(path);
    }


    @Test
    public void testUpdate() throws KeeperException, InterruptedException {
        zkmanager.create(path, data);
        String data = "testing for update:: string for testing";
        byte[] dataBytes = data.getBytes();
        zkmanager.update(path, dataBytes);
        String retrivedData = (String)zkmanager.getZNodeData(path,false);
        assertNotNull(retrivedData);
        zkmanager.delete(path);
    }

    @Test
    public void testGetZNodeChildren() throws KeeperException, InterruptedException {
        zkmanager.create(path, data);
        List<String> children= zkmanager.getZNodeChildren(path);
        assertNotNull(children);
        zkmanager.delete(path);
    }


    @Test
    public void testDelete() throws KeeperException, InterruptedException {
        zkmanager.create(path, data);
        zkmanager.delete(path);
        Stat stat = zkmanager.getZNodeStats(path);
        assertNull(stat);
    }

}
