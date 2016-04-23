package chapter5.item1;

import net.jcip.annotations.NotThreadSafe;

import java.util.Vector;

/**
 * 同步容器类的问题
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/23
 */
public class ClientLock {

    private Vector vector = new Vector();

    /**
     * 同步容器类都是线程安全的，但在某些情况下可能需要额外的客户端加锁来保护复合操作。
     * 比如本方法实现的“若没有则添加”功能。
     * <p>
     * 由于同步容器类要遵守同步策略，即支持客户端加锁，因此可能会创建一些新的操作，
     * 只要知道应该使用哪一个锁，那么这些新的操作就与容器的其他操作一样都是原子操作。
     */
    public void putIfAbsent(Object object) {
        // 同步容器类通过其自身的锁来保护它的每个方法
        synchronized (vector) {
            if (!vector.contains(object)) {
                vector.add(object);
            }
        }
    }

    public void printAll() {
        /**
         * 可能抛出 ConcurrentModificationException
         *
         * 在设计同步容器类的迭代器时并没有考虑到并发修改的问题，而是使用称之为“fail-fast”的机制。
         * 这意味着，当迭代器发现容器在迭代过程中被修改时，就会抛出一个 ConcurrentModificationException。
         */
        for (Object object : vector) {
            System.out.println(object);
        }
    }
}
