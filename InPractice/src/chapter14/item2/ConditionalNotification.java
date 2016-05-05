package chapter14.item2;

import chapter14.item1.BaseBoundedBuffer;
import net.jcip.annotations.ThreadSafe;

/**
 * 1，条件通知实现有界缓存
 * <p>
 *     在 chapter14.item1.BoundedBuffer 的 put 和 take 方法中采用的通知机制是保守的：
 *     每当将一个对象放入缓存或者从缓存中取走一个对象时，就执行一次通知。
 *     可以对其进行优化：首先，仅当缓存从空变为非空，或者从满转为非满时，才需要释放一个线程。
 *     并且，仅当 put 和 take 影响到这些状态转换时，才发出通知。
 *     这类通知被称为“条件通知”，虽然条件通知可以提升性能，但却很难正确地实现，因此使用时要谨慎。
 * <p>
 * Created by liuchenwei on 2016/5/4.
 */
public class ConditionalNotification {

    @ThreadSafe
    public static class BoundedBuffer<V> extends BaseBoundedBuffer<V> {

        public BoundedBuffer(int capacity) {
            super(capacity);
        }

        public synchronized void put(V v) throws InterruptedException {
            while (isFull()) {
                wait();
            }
            boolean wasEmpty = isEmpty();
            doPut(v);
            if (wasEmpty) {
                notifyAll();
            }
        }

        public synchronized V take() throws InterruptedException {
            while (isEmpty()) {
                wait();
            }
            boolean wasFull = isFull();
            V v = doTake();
            if (wasFull) {
                notifyAll();
            }
            return v;
        }
    }
}
