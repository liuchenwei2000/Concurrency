package chapter10.item1;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 3，在协作对象之间的锁顺序死锁
 * <p>
 *     如果在持有锁时调用某个外部方法，那么将出现活跃性问题。
 *     在这个外部方法中可能会获取其他锁（这可能会产生死锁），
 *     或者阻塞时间过长，导致其他线程无法及时获得当前被持有的锁。
 * <p>
 * Created by liuchenwei on 2016/5/1
 */
public class CollaborationDeadLock {

    /**
     * 出租车类，包含位置和目的地。
     */
    public static class Taxi {

        private String location;
        private String destination;

        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher, String destination) {
            this.dispatcher = dispatcher;
            this.destination = destination;
        }

        public synchronized String getLocation() {
            return location;
        }

        public synchronized void setLocation(String location) {
            this.location = location;
            if (location.equals(destination)) {
                dispatcher.notifyAvailable(this);// 这里隐含的会需要获取 dispatcher 锁
            }
        }
    }

    /**
     * 出租车调度室类
     */
    public static class Dispatcher {

        private final Set<Taxi> taxis;
        private final Set<Taxi> availableTaxis;


        public Dispatcher() {
            this.taxis = new HashSet<>();
            this.availableTaxis = new HashSet<>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized void logInfo() {
            for (Taxi taxi : taxis) {
                System.out.println(taxi.getLocation());// 这里隐含的会需要获取 taxi 锁
            }
        }
    }

    public static void main(String[] args) {
        /*
         * 尽管没有任何方法会显式地获取两个锁，但 taxi.setLocation 和
         * dispatcher.logInfo 等方法的调用者都会获得两个锁，
         * 如果像下面示例这样，两个线程分别执行这两个操作，
         * 就相当于按照不同的顺序来获取两个锁，因此就可能产生死锁。
         */
        final Dispatcher dispatcher = new Dispatcher();
        final Taxi taxi = new Taxi(dispatcher, "Beijing");

        new Thread(new Runnable() {

            @Override
            public void run() {
                taxi.setLocation("Beijing");
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                dispatcher.logInfo();
            }
        }).start();
    }
}
