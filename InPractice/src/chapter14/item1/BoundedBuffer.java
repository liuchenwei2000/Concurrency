package chapter14.item1;

import net.jcip.annotations.ThreadSafe;

/**
 * 3，使用条件队列实现的有界缓存
 * <p>
 *
 * <p>
 * Created by liuchenwei on 2016/5/4.
 */
@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {

    public BoundedBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(V v) throws InterruptedException {
        // 阻塞并直到 not-full
        while (isFull()) {
            // JVM 会将当前线程挂起（直到超时或被唤醒）并释放它持有的对象锁，
            // 从而允许其他线程执行该对象的同步块代码。
            // 当线程被唤醒时（其他线程调用了notify()或notifyAll()方法时），
            // 它会先争取获得对象锁，然后再检查条件是否满足。
            wait();
        }
        doPut(v);
        notifyAll();
    }

    public synchronized V take() throws InterruptedException {
        // 阻塞并直到 not-empty
        while (isEmpty()) {
            wait();
        }
        V v = doTake();
        notifyAll();
        return v;
    }

    public static void main(String[] args) {
        /*
        * 从调用者的角度看，这种方式能很好地运行，
        * 如果某个操作可以执行，那么就立即执行，否则就阻塞，调用者无须处理失败和重试。
        * 要选择合适的休眠时间间隔，就需要在响应性与 CPU 使用率之间进行权衡。
        */
        BoundedBuffer<String> buffer = new BoundedBuffer(10);
        try {
            System.out.println(buffer.take());
        }
        /*
        * SleepyBoundedBuffer 对调用者提出了新的需求：处理 InterruptedException。
        * 当一个方法由于等待某个条件变成真而阻塞时，需要提供一种取消机制。
        * 与大多数具备良好行为的阻塞库方法一样，SleepyBoundedBuffer 通过中断来支持取消，
        * 如果该方法被中断，那么将提前返回并抛出 InterruptedException。
        */
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
