package chapter15.item2;

import net.jcip.annotations.ThreadSafe;

/**
 * 2，基于 CAS 实现的非阻塞计数器
 * <p>
 *     CasCounter 不会阻塞，如果其他线程同时更新计数器，那么会多次执行重试操作。
 * <p>
 *     当竞争程度不高时，基于 CAS 的计数器在性能上远远超过了基于锁的计数器。
 *     CAS 的主要缺点是，它将使调用者处理竞争问题（重试、回退或放弃），
 *     而在锁中能自动处理竞争问题，线程在获得锁之前将一直阻塞。
 * <p>
 * Created by liuchenwei on 2016/5/6.
 */
@ThreadSafe
public class CasCounter {

    private SimulatedCAS value;

    public int getValue(){
        return value.get();
    }

    /**
     * 递增操作采用了标准形式：
     * 读取旧的值，根据它计算出新值（+1），并使用 CAS 来设置这个新值。
     * 如果 CAS 失败，那么该操作将立即重试。
     *
     * 通常反复地重试是一种合理的策略，但在一些竞争很激烈的情况下，
     * 更好的方式是在重试之前首先等待一段时间或者回退，从而避免造成活锁。
     */
    public int increment() {
        int v;
        do {
            v = value.get();
        } while (v != value.compareAndSwap(v, v + 1));
        return v + 1;
    }
}
