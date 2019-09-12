package com.lsroudi.dice;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

public class Dice extends LeaderSelectorListenerAdapter implements Closeable {

    final static String ZOOKEEPER_SERVER = "127.0.0.1:2181";
    final static String ELECTION_PATH = "/dice-elect";
    CuratorFramework curator;
    LeaderSelector leaderSelector;
    Random rand = new Random();

    public Dice() throws InterruptedException {
        curator = CuratorFrameworkFactory.newClient(ZOOKEEPER_SERVER,
                2000, 10000, new ExponentialBackoffRetry(1000, 3));
        curator.start();
        curator.blockUntilConnected();

        leaderSelector = new LeaderSelector(curator, ELECTION_PATH, this);
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }

    public void play() throws InterruptedException {
        while (true){
            Thread.sleep(1000);
            System.out.println("Value = " + (rand.nextInt(6) + 1));
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Dice dice = new Dice();
        dice.play();
    }

    public void takeLeadership(CuratorFramework client) throws Exception {

    }

    public void close() throws IOException {

    }
}
