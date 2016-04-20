package chapter2.item2;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 安全的计数器实现
 * <p>
 *     当在无状态的类中添加一个状态时，如果该状态完全由线程安全的对象来管理，那么这个类仍然是线程安全的。
 * <p>
 * Created by liuchenwei on 2016/4/20.
 */
@ThreadSafe
public class SafeCounter {

    private AtomicInteger counter = new AtomicInteger(0);

    public void service() {
        // do something
        counter.incrementAndGet();
    }
}
