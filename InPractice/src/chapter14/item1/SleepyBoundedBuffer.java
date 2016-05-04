package chapter14.item1;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.TimeUnit;

/**
 * 2，通过轮询与休眠来实现简单的阻塞
 * <p>
 *     本类使用简单阻塞实现的有界缓存：
 *     put 和 take 方法来实现一种简单的“轮询与休眠”重试机制，
 *     从而使调用者无须每次调用时都实现重试逻辑。
 *     如果缓存为空，则 take 将休眠并直到另一个线程在缓存中放入一些数据；
 *     如果缓存是满，则 put 将休眠并直到另一个线程从缓存中取出一些数据。
 *     这种方式将前提条件的管理操作封装起来，并简化了对缓存的使用。
 * <p>
 * Created by liuchenwei on 2016/5/4.
 */
@ThreadSafe
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

    public SleepyBoundedBuffer(int capacity) {
        super(capacity);
    }

    public void put(V v) throws InterruptedException {
        while (true) {// 轮询
            synchronized (this) {// 重新加锁
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            // 释放锁并休眠等待从而使其他线程能够访问缓存
            TimeUnit.NANOSECONDS.sleep(10);
        }
    }

    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty()) {
                    return doTake();
                }
            }
            TimeUnit.NANOSECONDS.sleep(10);
        }
    }

    public static void main(String[] args) {
        /*
        * 从调用者的角度看，这种方式能很好地运行，
        * 如果某个操作可以执行，那么就立即执行，否则就阻塞，调用者无须处理失败和重试。
        * 要选择合适的休眠时间间隔，就需要在响应性与 CPU 使用率之间进行权衡。
        */
        SleepyBoundedBuffer<String> buffer = new SleepyBoundedBuffer(10);
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
