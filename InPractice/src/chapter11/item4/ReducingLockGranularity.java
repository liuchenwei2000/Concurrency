package chapter11.item4;

import net.jcip.annotations.ThreadSafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 2，减小锁的粒度
 * <p>
 *     减小锁的持有时间的方式是降低线程请求锁的频率（从而缩小发生竞争的可能性）。
 *     这可以通过锁分解和锁分段等技术来实现，其中都将采用多个相互独立的锁来保护独立的状态变量，
 *     从而改变这些变量在之前由单个锁来保护的情况，这些技术能减小锁操作的粒度，实现更高的可伸缩性。
 *     然而，使用的锁越多，发生死锁的风险也就越高。
 * <p>
 *     如果在锁上存在适中而不是激烈的竞争时，通过将一个锁分解为两个锁，能最大限度地提升性能。
 *     如果对竞争并不激烈的锁进行分解，那么在性能和吞吐量等方面带来的提升将非常有限。
 *     对竞争适中的锁进行分解时，实际上是把这些锁转变为非竞争的锁，从而有效提高性能和可伸缩性。
 * <p>
 * Created by liuchenwei on 2016/5/2
 */
public class ReducingLockGranularity {

    /**
     * 初始版本，整个类的方法都使用同一个锁，可伸缩性极差。
     */
    @ThreadSafe
    public static class ServerStatus {

        private final Set<String> users = new HashSet<>();
        private final Set<String> queries = new HashSet<>();

        public synchronized void addUser(String user) {
            users.add(user);
        }

        public synchronized void addQuery(String query) {
            queries.add(query);
        }
    }

    /**
     * 更好的版本，如果一个锁需要保护多个相互独立的状态变量，
     * 那么可以将这个锁分解为多个锁，并且每个锁只保护一个变量，
     * 从而提高可伸缩性，并最终降低每个锁被请求的频率。
     */
    @ThreadSafe
    public static class BetterServerStatus {

        private final Set<String> users = new HashSet<>();
        private final Set<String> queries = new HashSet<>();

        public void addUser(String user) {
            synchronized (users) {
                users.add(user);
            }
        }

        public void addQuery(String query) {
            synchronized (queries) {
                queries.add(query);
            }
        }
    }

    /**
     * 最优版本，将线程安全性委托给一个线程安全的 Set，而不是使用显式的同步，
     * 能隐含地对锁进行分解，因为每个 Set 都会使用一个不同的锁来保护其状态。
     */
    @ThreadSafe
    public static class BestServerStatus {

        private final Set<String> users = Collections.synchronizedSet(new HashSet<String>());
        private final Set<String> queries = Collections.synchronizedSet(new HashSet<String>());

        public void addUser(String user) {
            users.add(user);
        }

        public void addQuery(String query) {
            queries.add(query);
        }
    }
}
