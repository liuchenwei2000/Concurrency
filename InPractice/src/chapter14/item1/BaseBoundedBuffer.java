package chapter14.item1;

import net.jcip.annotations.ThreadSafe;

/**
 * 0，有界缓存基类
 * <p>
 *     有界缓存提供的 put 和 take 操作中都包含有一个前提条件：
 *     不能从空缓存中获取元素，也不能将元素放入已满的缓存中。
 *     当前提条件未满足时，依赖状态的操作可以抛出一个异常或返回一个错误状态
 *     （使其成为调用者的一个问题），也可以保持阻塞直到对象进入正确的状态。
 * <p>
 * Created by liuchenwei on 2016/5/4.
 */
@ThreadSafe
public abstract class BaseBoundedBuffer<V> {

    private final V[] buffer;// 底层数组
    private int head;// 队列头索引，take 操作始终取队列头的元素
    private int tail;// 队列尾索引，put 操作始终将元素放在队列尾
    private int count;// 元素计数器

    protected BaseBoundedBuffer(int capacity) {
        this.buffer = (V[]) new Object[capacity];
    }

    protected synchronized final void doPut(V v) {
        buffer[tail] = v;
        if (++tail == buffer.length) {
            tail = 0;
        }
        ++count;
    }

    protected synchronized final V doTake() {
        V value = buffer[head];
        buffer[head] = null;
        if (++head == buffer.length) {
            head = 0;
        }
        --count;
        return value;
    }

    public synchronized boolean isFull() {
        return count == buffer.length;
    }

    public synchronized boolean isEmpty() {
        return count == 0;
    }
}
