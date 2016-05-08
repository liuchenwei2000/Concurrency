package chapter16.item2;

import net.jcip.annotations.NotThreadSafe;

/**
 * 1，不安全的延迟初始化
 * <p>
 *     错误的延迟初始化将导致不正确地发布，本例除了存在竞态条件问题外（可能存在多个实例），
 *     仍然是不安全的，因为另一个线程可能看到对部分构造的 Singleton 实例的引用。
 * <p>
 * Created by liuchenwei on 2016/5/8
 */
@NotThreadSafe
public class UnsafeLazyInitialization {

    private static Singleton instance;

    /**
     * 假设线程 A 是第一个调用该方法的线程，它将看到 instance 为 null，
     * 并且初始化一个新的 Singleton 实例，然后将 instance 设置为这个新实例。
     * 当线程 B 随后调用该方法时，它可能看到 instance 的值为非空，因此使用这个已经构造好的 Singleton。
     * 但线程 A 写入 instance 的操作与线程 B 读取 instance 的操作之间不存在 Happens-Before 关系。
     * 在发布对象是存在数据竞争问题，因此线程 B 并不一定能看到 Singleton 的正确状态。
     * <p>
     * 当新分配一个 Singleton 时，Singleton 的构造函数将把新实例中的各个域由默认值修改为它们的初始值。
     * 由于在两个线程中都没有使用同步，因此线程 B 看到的线程 A 中的操作顺序，可能与线程 A 执行这些操作时的顺序并不相同。
     * 即使线程 A 初始化 Singleton 实例之后再将 instance 设置为指向它，
     * 线程 B 仍可能看到对 instance 写入操作将在对 Singleton 各个域的写入操作之前发生。
     * 因此，线程 B 就可能看到一个被部分构造的 Singleton 实例，该实例可能处于无效状态，
     * 随后使用该实例时其状态可能会出现无法预料的变化。
     */
    public static Singleton getInstance() {
        if (instance == null) {// 这里存在竞态条件
            instance = new Singleton();// 这里存在不安全的发布
        }
        return instance;
    }
}
