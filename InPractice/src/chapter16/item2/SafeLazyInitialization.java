package chapter16.item2;

import net.jcip.annotations.ThreadSafe;

/**
 * 2，安全初始化模式
 * <p>
 * <p>
 * Created by liuchenwei on 2016/5/8
 */
public class SafeLazyInitialization {

    /**
     * 1，线程安全的延迟初始化
     * <p>
     *     通过将 getInstance 方法声明为 synchronized，可以修复 UnsafeLazyInitialization 中的问题。
     *     由于 getInstance 的代码路径很短，因此如果该方法没有被多个线程频繁调用，
     *     那么在锁上不会存在激烈的竞争，从而能提供令人满意的性能。
     */
    @ThreadSafe
    public static class SafeLazyInitializationBySynch {

        private static Singleton instance;

        public synchronized static Singleton getInstance() {
            if (instance == null) {
                instance = new Singleton();
            }
            return instance;
        }
    }

    /**
     * 静态初始化器是由 JVM 在类的初始化阶段执行，即在类被加载后并且被线程使用之前。
     * 由于 JVM 将在初始化期间获得一个锁，并且每个线程都至少获取一次这个锁以确保这个类已经加载，
     * 因此在静态初始化期间，内存写入操作将自动对所有线程可见。
     * 无论是在被构造期间还是被引用时，静态初始化的对象都不需要显式的同步。
     * 然而，这个规则仅适用于在构造时的状态，如果对象是可变的，那么在读线程和写线程之间
     * 仍然需要通过同步来确保随后的修改操作是可见的，以及避免数据被破坏。
     */

    /**
     * 2，通过使用提前初始化，避免了在每次调用 getInstance 时的同步开销
     * <p>
     *     通过将这项技术和 JVM 的延迟加载机制结合起来，可以形成一种延迟初始化技术，
     *     从而在常见的代码中不需要同步。详见 SingletonFactory.java。
     */
    @ThreadSafe
    public static class EagerInitialization {

        private static Singleton instance = new Singleton();

        public static Singleton getInstance() {
            return instance;
        }
    }

    /**
     * 3，延迟初始化占位类（Holder）模式
     * <p>
     *     通过使用一个专门的类来初始化 Singleton，JVM 将推迟 SingletonHolder 的初始化操作，
     *     直到开始使用这个类时才初始化，并且由于通过静态初始化来初始化 Singleton，因此不需要额外的同步。
     *     当任何线程第一次调用 getInstance 时，都会使 SingletonHolder 被加载和被初始化，
     *     此时静态初始化器将执行 Singleton 的初始化操作。
     */
    @ThreadSafe
    public static class SingletonFactory {

        private static class SingletonHolder {

            private static Singleton instance = new Singleton();
        }

        public static Singleton getInstance() {
            return SingletonHolder.instance;
        }
    }
}
