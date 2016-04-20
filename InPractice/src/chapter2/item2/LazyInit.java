package chapter2.item2;

import net.jcip.annotations.NotThreadSafe;

/**
 * 不安全的延迟初始化实现
 * <p>
 *     在并发编程中，由于不恰当的执行时序而出现不正确的结果，这种情况被称为竞态条件（Race Condition）。
 *     当某个计算的正确定取决于多个线程的交替执行时序时，就会发生竞态条件。
 *     最常见的竞态条件类型是“先检查后执行”操作，即通过一个可能失效的观测结果来决定下一步的动作。
 * <p>
 *     将“先检查后执行”以及“读取-修改-写入”等操作统称为复合操作：
 *     包含了一组必须以原子方式（不可再分割，要么全都执行，要么全都不执行）执行的操作以确保线程安全性。
 *     要避免竞态条件，就必须在某个线程修改状态变量时，通过某种方式防止其他线程使用这个变量，
 *     从而确保其他线程只能在修改操作完成之后读取或修改状态，而不是在修改状态的过程中。
 * <p>
 * Created by liuchenwei on 2016/4/20.
 */
@NotThreadSafe
public class LazyInit {

    private Object instance;

    public Object getInstance() {
        if (instance == null) {
            instance = new Object();
        }
        return instance;
    }
}
