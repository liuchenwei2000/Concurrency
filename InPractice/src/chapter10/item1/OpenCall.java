package chapter10.item1;

import java.util.HashSet;
import java.util.Set;

/**
 * 4，通过开放调用避免死锁
 * <p>
 *     如果在调用某个方法时不需要持有锁，那么这种调用被称为开放调用。
 *     依赖于开放调用的类通常能表现出更好的行为，并且与那些在调用方法时
 *     需要持有锁的类想必，也更易于编写，是一种可以用来避免死锁的方法。
 * <p>
 *     本例通过使用开放调用，解决了 CollaborationDeadLock 中的死锁问题。
 *     这需要使用同步代码块仅被用于保护那些涉及共享状态的操作。
 * <p>
 * Created by liuchenwei on 2016/5/1
 */
public class OpenCall {

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

        /**
         * 在重新编写同步代码块以使用开放调用时会产生意想不到的结果，因为这会使得某个原子操作变为非原子操作。
         * 然而在许多情况下，使某个操作失去原子性是可以接受的。
         * 例如本方法中出租车更新位置和通知调度室并不需要实现为一个原子操作。
         */
        public void setLocation(String location) {
            boolean reached;

            synchronized (this) {
                this.location = location;
                reached = location.equals(destination);
            }

            if (reached) {
                // 此时再需要获取 dispatcher 锁时已经释放了 taxi 锁
                dispatcher.notifyAvailable(this);
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

        /**
         * 在有些情况下，虽然去掉原子性可能会出现一些问题，但这种语义变化仍是可以接受的。
         * 例如本例中输出的不是某个时刻下整个车队位置的完整快照，而是每辆出租车在不同时刻的位置。
         */
        public synchronized void logInfo() {
            Set<Taxi> copy;

            synchronized (this) {
                copy = new HashSet<>(taxis);
            }

            for (Taxi taxi : copy) {
                // 此时再需要获取 taxi 锁时已经释放了 dispatcher 锁
                System.out.println(taxi.getLocation());
            }
        }
    }
}
