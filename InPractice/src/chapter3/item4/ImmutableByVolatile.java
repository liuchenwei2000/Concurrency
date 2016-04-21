package chapter3.item4;

import net.jcip.annotations.ThreadSafe;

/**
 * 使用 volatile 类型来发布不可变对象
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/21.
 */
public class ImmutableByVolatile {

    /**
     * 使用加锁的方式达到线程安全性
     */
    @ThreadSafe
    public static class CachedCalculatorByLock {

        private int lastNumberA;// 缓存最后一次操作的参数 a
        private int lastNumberB;// 缓存最后一次操作的参数 b

        private int lastResult;// 缓存最后一次操作的结果

        public int add(int a, int b) {
            synchronized (this) {
                if ((a == lastNumberA && b == lastNumberB) || (a == lastNumberB && b == lastNumberA)) {
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
    }

    /**
     * 使用 volatile 不可变对象的方式达到线程安全性
     * <p>
     *     对于在访问和更新多个相关变量时出现的竞态条件问题，
     *     可以通过将这些变量全部保存在一个不可变对象中来消除。
     *     如果是一个可变的对象，那就必须使用锁来确保原子性。
     *     如果是一个不可变对象，那么当线程获得了该对象的引用后，就不必担心另一个线程会修改对象的状态。
     *     如果要更新这些变量，则可以创建一个新的容器对象，但其他使用原有对象的线程仍然会看到对象处于一致的状态。
     * <p>
     */
    @ThreadSafe
    public static class CachedCalculatorByVolatile {

        // 使用指向不可变容器对象的 volatile 类型引用以缓存最新的结果
        private volatile OneValueCache cache = new OneValueCache(0, 0, null);

        public int add(int a, int b) {
            // 与 cache 相关的操作不会相互干扰，因为 OneValueCache 是不可变的
            // 并且在每条相应的代码路径中只会访问它一次。通过使用包含多个状态变量的容器对象来维持不变性条件，
            // 并使用 volatile 类型的引用来确保可见性，使得在没有显式使用锁的情况下仍然是线程安全的。
            Integer result = cache.getResult(a, b);
            if (result == null) {
                result = a + b;
                cache = new OneValueCache(a, b, result);
            }
            return result;
        }
    }

    /**
     * 对操作数和操作结果进行缓存的不可变容器类
     */
    public static class OneValueCache {

        private int numberA;// 缓存参数 a
        private int numberB;// 缓存参数 b

        private Integer result;// 缓存操作的结果

        public OneValueCache(int numberA, int numberB, Integer result) {
            this.numberA = numberA;
            this.numberB = numberB;
            this.result = result;
        }

        public Integer getResult(int a, int b) {
            if ((a == numberA && b == numberB) || (a == numberB && b == numberA)) {
                return result;
            }
            return null;
        }
    }
}
