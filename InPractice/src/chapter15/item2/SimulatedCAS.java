package chapter15.item2;

import net.jcip.annotations.ThreadSafe;

/**
 * 1，模拟 CAS 操作，说明了 CAS 语义（不是实现）。
 * <p>
 *     CAS 包含了3个操作数——需要读写的内存位置 V、进行比较的值 A 和拟写入的新值 B。
 *     当且仅当 V 的值等于 A 时，才会通过原子方式用新值 B 来更新 V 的值，否则不会执行任何操作。
 *     无论位置 V 的值是否等于 A，都将返回 V 原有的值。
 *     CAS 的含义是：
 *     我认为 V 的值应该为 A，如果是的话，那么将 V 的值更新为 B，否则不修改并告诉 V 的值实际为多少。
 * <p>
 *     CAS 是一项乐观的技术，它希望能成功地执行更新操作，
 *     并且如果有另一个线程在最近一次检查后更新了该变量，那么 CAS 能检测到这个错误。
 *     由于 CAS 能检测到来自其他线程的干扰，因此即使不使用锁也能够实现原子的读-改-写操作序列。
 * <p>
 * Created by liuchenwei on 2016/5/6.
 */
@ThreadSafe
public class SimulatedCAS {

    private int value;

    public SimulatedCAS(int value) {
        this.value = value;
    }

    public int get() {
        return this.value;
    }

    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = this.value;
        if (expectedValue == oldValue) {
            value = newValue;
        }
        return oldValue;
    }

    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return (expectedValue == compareAndSwap(expectedValue, newValue));
    }
}
