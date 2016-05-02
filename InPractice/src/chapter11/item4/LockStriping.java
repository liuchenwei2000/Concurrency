package chapter11.item4;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 3，锁分段
 * <p>
 *     在某些情况下，可以将锁分解技术进一步扩展为对一组独立对象上的锁进行分解，这种情况被称为锁分段。
 *     例如在 ConcurrentHashMap 的实现中使用了一个包含 16 个锁的数组，每个锁保护所有散列桶的 1/16，
 *     其中第 N 个散列桶由第（N mod 16）个锁来保护。假设散列函数具有合理的分布性，并且能够实现均匀分布，
 *     那么大约能把对于锁的请求减少到原来的 1/16。
 * <p>
 *     锁分段的劣势在于，与采用单个锁来实现独占访问相比，要获取多个锁来实现独占访问将更加困难并且开销更高。
 *     通常，在执行一个操作时最多只需获取一个锁，但在某些情况下需要加锁整个容器。
 * <p>
 * Created by liuchenwei on 2016/5/2
 */
public class LockStriping {

    /**
     * 在基于散列的 Map 中使用锁分段技术：它拥有 N_LOCKS 个锁，并且每个锁保护散列桶的一个子集。
     * 大多数方法，例如 get 只需要获得一个锁，而有些方法则需要获得所有的锁，但不要求同时获得，例如 clear。
     */
    @ThreadSafe
    public static class StripedMap {

        private static final int N_LOCKS = 16;

        // 同步策略：buckets[n] 由 locks[n%N_LOCKS] 来保护
        private Bucket[] buckets;
        private Object[] locks;

        public StripedMap(int numberOfBuckets) {
            this.buckets = new Bucket[numberOfBuckets];
            this.locks = new Object[N_LOCKS];
            initLocks();
        }

        private void initLocks() {
            for (int i = 0; i < N_LOCKS; i++) {
                locks[i] = new Object();
            }
        }

        private final int index(Object key){
            return Math.abs(key.hashCode() % buckets.length);
        }

        public Object get(Object key) {
            int index = index(key);// 对应 Bucket 的索引
            synchronized (locks[index % N_LOCKS]) {
                Bucket bucket = buckets[index];
                return bucket.find(key);
            }
        }

        public void clear() {
            for (int i = 0; i < buckets.length; i++) {
                synchronized (locks[i % N_LOCKS]) {
                    buckets[i].clear();
                }
            }
        }
    }

    private static class Bucket {

        public Object find(Object key) {
            // find value by key
            return null;
        }

        public void clear() {
            // clear the bucket
        }
    }
}
