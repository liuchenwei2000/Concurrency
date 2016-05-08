package chapter16.item2;

import net.jcip.annotations.NotThreadSafe;

/**
 * 3，双重检查加锁
 * <p>
 *     双重检查加锁（DCL）声称能实现在常见代码上的延迟初始化中不存在同步开销。
 *     它的工作原理是：首先检查是否在没有同步的情况下需要初始化，如果 instance 不为空，则直接使用它。
 *     否则，就进行同步并再次检查 Singleton 是否被初始化，从而保证只有一个线程对共享的 Singleton 执行初始化。
 *     这种模式存在的问题是：线程可能看到一个仅被部分构造的 Singleton。
 * <p>
 *     在 Java5 及更高的版本中，如果把 instance 声明为 volatile 类型，就能启用 DCL，
 *     并且这种方式对性能的影响很小，因为 volatile 变量读取操作的性能通常只是略高于非 volatile 变量。
 *     然而，DCL 的这种使用方法已经被广泛地废弃——促使该模式出现的原因（无竞争同步的执行速度很慢）已经不复存在，
 *     因而它不是一种高效的优化措施，延迟初始化占位类模式能带来同样的优势，而且更容易理解。
 * <p>
 * Created by liuchenwei on 2016/5/8
 */
@NotThreadSafe
public class DoubleCheckedLocking {

    private static Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedLocking.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
