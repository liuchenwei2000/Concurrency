package chapter2.item5;

import net.jcip.annotations.ThreadSafe;

/**
 * 带缓存功能的计算器
 * <p>
 *     要判断同步代码块的合理大小，需要在各种设计需求之间进行权衡，
 *     包括安全性、简单性和性能，有时候，在简单性与性能之间会发生冲突。
 *     当实现某个同步策略时，一定不要盲目地为了性能而牺牲简单性（也可能是破坏安全性）。
 *     当执行时间较长的计算或者可能无法快速完成的操作时（网络IO等）一定不要持有锁。
 * <p>
 * Created by liuchenwei on 2016/4/20.
 */
@ThreadSafe
public class CachedCalculator {

    private int lastNumberA;// 缓存最后一次操作的参数 a
    private int lastNumberB;// 缓存最后一次操作的参数 b

    private int lastResult;// 缓存最后一次操作的结果

    private int counter;// 计数器

    private int cacheHits;// 缓存命中数

    // 这个版本的 add 方法将 synchronized 应用到整个方法上，虽然能确保线程安全性，但性能很差。
    public synchronized int addSync(int a, int b) {
        ++counter;
        if ((a == lastNumberA && b == lastNumberB) || (a == lastNumberB && b == lastNumberA)) {
            ++cacheHits;
            return lastResult;
        }

        int result = a + b;

        lastNumberA = a;
        lastNumberB = b;
        lastResult = result;
        return result;
    }

    /**
     * 这个版本的 add 方法通过使用两个独立的同步代码块，每个同步代码块都只包含一小段代码。
     * 其中一个同步代码块负责保护判断是否只需返回缓存结果的“先检查后执行”操作序列；
     * 另一个同步代码块负责确保对缓存的数值和求和结果进行同步更新。
     * 由于两个计数器也是共享可变状态的一部分，因此必须在所有访问它们的位置上都是用同步。
     * 位于同步代码块之外的代码将以独占方式来访问（位于栈上的）局部变量，这些变量不会在多个线程间共享，无需同步。
     *
     * 这里两个计数器不再使用 AtomicInteger 类型，因为已经使用了同步代码块来构造原子操作，
     * 而使用两种不同的同步机制不仅会带来混乱，也不会在性能或安全性上带来提升，所以不再使用原子变量
     */
    public int add(int a, int b) {
        synchronized (this) {
            ++counter;
            if ((a == lastNumberA && b == lastNumberB) || (a == lastNumberB && b == lastNumberA)) {
                ++cacheHits;
                return lastResult;
            }
        }

        int result = a + b;

        synchronized (this) {
            lastNumberA = a;
            lastNumberB = b;
            lastResult = result;
        }
        return result;
    }

    public synchronized int getCounter() {
        return counter;
    }

    public synchronized int getCacheHits() {
        return cacheHits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) counter;
    }
}
