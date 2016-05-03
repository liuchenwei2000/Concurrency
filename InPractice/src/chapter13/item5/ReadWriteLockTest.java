package chapter13.item5;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * <p>
 *     ReentrantLock 实现了一种标准的互斥锁，即每次最多只有一个线程能持有锁。
 *     互斥是一种保守的加锁策略，虽然可以避免“写/写”冲突和“读/写”冲突，但同样也避免了“读/读”冲突。
 *     在多数情况下，数据结构上的操作都是读操作。如果能够放宽加锁需求，
 *     允许多个执行读操作的线程同时访问数据结构，那么将提升程序的性能。
 *     只要每个线程都能确保读取到最新的数据，并且在读取数据时不会有其他的线程修改数据，那就不会有问题。
 *     在这种情况下可以使用读/写锁：一个资源可以被多个读操作访问，或者被一个写操作访问，但两者不能同时进行。
 * <p>
 *     ReadWriteLock 中暴露了两个 Lock 对象：一个用于读操作，一个用于写操作。
 *     要读取由 ReadWriteLock 保护的数据，必须首先获得读取锁；
 *     当需要修改 ReadWriteLock 保护的数据时，必须首先获得写入锁。
 *     尽管这两个锁看上去是彼此独立的，但读取锁和写入锁只是读写锁对象的不同视图。
 * <p>
 *     读写锁是一种性能优化策略，在一些特定的情况下能实现更高的并发性。
 *     在实际情况中，对于多处理器系统上被频繁读取的数据结构，读写锁能够提高性能。
 * <p>
 * Created by liuchenwei on 2016/5/3.
 */
public class ReadWriteLockTest {

    /**
     * 用读写锁来包装 Map
     * <p>
     *     ReentrantReadWriteLock 为读锁和写锁都提供了可重入的加锁语义。
     *     构造时也可以选择一个非公平的锁（默认）还是公平的锁。
     *     它的写入锁只能有唯一的所有者，并且只能由获得该锁的线程来释放。
     *     当锁的持有时间较长并且大部分操作都不会修改被保护的资源时，读写锁能提高并发性。
     */
    public static class ReadWriteMap<K, V> {

        private final Map<K, V> map;
        private final ReadWriteLock lock;
        private final Lock readLock;
        private final Lock writeLock;

        public ReadWriteMap(Map<K, V> map) {
            this.map = map;
            this.lock = new ReentrantReadWriteLock();
            this.readLock = lock.readLock();
            this.writeLock = lock.writeLock();
        }

        /**
         * 写操作实现
         */
        public void put(K key, V value) {
            writeLock.lock();
            try {
                map.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        /**
         * 读操作实现
         */
        public V get(K key) {
            readLock.lock();
            try {
                return map.get(key);
            } finally {
                readLock.unlock();
            }
        }
    }
}
