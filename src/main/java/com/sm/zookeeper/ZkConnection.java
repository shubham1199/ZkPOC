package com.sm.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import lombok.*;

@Slf4j
@Data
public class ZkConnection {

    // Local Zookeeper object to access ZooKeeper ensemble
    private ZooKeeper zoo;
    final CountDownLatch connectionLatch = new CountDownLatch(1);


    public ZkConnection() {
        // TODO Auto-generated constructor stub
    }

    public ZooKeeper connect(String host) throws IOException,
            InterruptedException {
        zoo = new ZooKeeper(host, 2000, new Watcher() {
            public void process(WatchedEvent we) {

                if (we.getState() == KeeperState.SyncConnected) {
                    connectionLatch.countDown();
                }
            }
        });
        connectionLatch.await();
        return zoo;
    }

    // Method to disconnect from zookeeper server
    public void close() throws InterruptedException {
        zoo.close();
    }

}