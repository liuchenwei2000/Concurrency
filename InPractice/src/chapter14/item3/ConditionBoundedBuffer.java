package chapter14.item3;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用显示条件变量的有界缓存
 * <p>
 *     正如 Lock 是一种广义的内置锁，Condition 也是一种广义的内置条件队列。
 *     内置条件队列存在一些缺陷，比如每个内置锁只能有一个相关联的条件队列。
 *     如果想编写一个带有多个他条件谓词的并发对象，或者想获得除了条件队列可见性之外的更多控制权，
 *     就可以使用显式的 Lock 和 Condition 而不是内置锁和条件队列。
 *     在 Condition 对象中，与 wait、notify 和 notifyAll 方法对应的分别是 await、signal 和 signalAll。
 * <p>
 *     本例通过将两个条件谓词分开并放到两个等待线程集中，Condition 使其更容易满足单词通知的需求。
 *     signal 比 signalAll 更高效，它能极大地减少在每次缓存操作中发生的上下文切换与锁请求的次数。
 * <p>
 *     如果需要一些高级功能，例如使用公平的队列操作或者在每个锁上对应多个等待线程集，
 *     那么应该优先使用 Condition 而不是内置条件队列。（如果需要 ReentrantLock
 *     的高级功能并已经使用了它，那就自然会使用 Condition 对象了。）
 * <p>
 * Created by liuchenwei on 2016/5/4.
 */
@ThreadSafe
public class ConditionBoundedBuffer<V> {

    private final Lock lock = new ReentrantLock();

    // 创建一个 Condition，对于每个 Lock，可以由任意数量的 Condition 对象。
    // Condition 对象继承了相关 Lock 对象的公平策略，对于公平的锁，
    // 线程会依照 FIFO 顺序从 Condition.await 中释放。
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    private final V[] buffer;// 底层数组
    private int head;// 队列头索引，take 操作始终取队列头的元素
    private int tail;// 队列尾索引，put 操作始终将元素放在队列尾
    private int count;// 元素计数器

    protected ConditionBoundedBuffer(int capacity) {
        this.buffer = (V[]) new Object[capacity];
    }

    /**
     * 与内置锁和条件队列一样，当使用显式的 Lock 和 Condition 时，
     * 也必须满足锁、条件谓词和条件变量之间的三元关系。
     * 在条件谓词中包含的变量必须由 Lock 来保护，并且在检查条件谓词
     * 以及调用 await 和 signal 时，必须持有 Lock 对象。
     */
    public void put(V v) throws InterruptedException {
        lock.lock();

        try {
            while (isFull()) {// 阻塞并直到 not full
                notFull.await();
            }
            buffer[tail] = v;
            if (++tail == buffer.length) {
                tail = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public V take() throws InterruptedException {
        lock.lock();

        try {
            while (isEmpty()) {// 阻塞并直到 not empty
                notEmpty.await();
            }

            V value = buffer[head];
            buffer[head] = null;
            if (++head == buffer.length) {
                head = 0;
            }
            --count;
            notFull.signal();
            return value;
        } finally {
            lock.unlock();
        }
    }

    public boolean isFull() {
        lock.lock();
        try {
            return count == buffer.length;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return count == 0;
        } finally {
            lock.unlock();
        }
    }
}
