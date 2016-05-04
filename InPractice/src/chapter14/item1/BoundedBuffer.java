package chapter14.item1;

import net.jcip.annotations.ThreadSafe;

/**
 * 3，使用条件队列实现的有界缓存
 * <p>
 *     条件队列使得一组线程能够通过某种方式来等待特定的条件变成真。
 *     传统队列的元素是一个个数据，而条件队列的元素是一个个正在等待相关条件的线程。
 * <p>
 *     正如每个 Java 对象都可以作为一个锁，每个对象同样可以作为一个条件队列，
 *     并且 Object 中的 wait、notify 和 notifyAll 方法就构成了内部条件队列的 API。
 *     对象的内置锁与内部条件队列是相互关联的，要调用对象 X 中条件队列的任何一个方法，必须持有 X 的锁。
 *     这是因为“等待由状态构成的条件”与“维护状态一致性”这两种机制必须被紧密地绑定在一起：
 *     只有能对状态进行检查时，才能在某个条件上等待，并且只有能修改状态时，才能从条件等待中释放另一个线程。
 * <p>
 *     条件队列使得在表达和管理状态依赖性时更加简单和高效，比使用“休眠”的有界缓存更简单，
 *     更高效（当缓存状态没有发生变化时，线程醒来的次数更少）响应性也更高（当发生特定状态变化时将立即醒来）。
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
            // Object.wait 会自动释放锁，并请求操作系统挂起当前线程，
            // 从而使其他线程能够获得这个锁并修改对象的状态。
            // 当被挂起的线程醒来时，它将在返回之前重新获取锁。
            // 从直观上来理解，调用 wait 意味着“我要去休息了，但当发生特定的事情时唤醒我”
            // 而调用 notify/notifyAll 方法就意味着“特定的事情发生了”。
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
