package chapter15.item3;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.*;

/**
 * 0，原子变量类示例
 * <p>
 *     原子变量类相当于一种泛化的 volatile 变量，
 *     能够支持原子的和有条件的读-改-写操作。
 * <p>
 * Created by liuchenwei on 2016/5/6.
 */
public class AtomicXxxTest {

    /**
     * 使用 AtomicInteger 实现的计数器
     * <p>
     *     AtomicInteger 表示一个 int 类型的指，并提供了 get 和 set 方法，
     *     这和 volatile 类型的 int 变量在读取和写入上有着相同的内存语义。
     *     它还提供了一个原子的 compareAndSet 方法，如果该方法成功执行，
     *     那么将实现与读取/写入一个 volatile 变量相同的内存效果。
     *     另外还有提供原子的添加、递增和递减等方法。
     * <p>
     *     本类基于 AtomicInteger 实现了计数器，比 chapter15.item2.CasCounter
     *     在发生竞争的情况下能提供更高的可伸缩性，因为它直接利用了硬件对并发的支持。
     * <p>
     */
    @ThreadSafe
    public static class Counter {

        private AtomicInteger count = new AtomicInteger();

        public int getValue() {
            return count.get();
        }

        public int increment() {
            return count.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        // 共有 12 个原子变量类，最常用的是标量类：这些类都支持 CAS

        // 标量类
        // 如果要想模拟其他类型的原子变量，可将 byte 和 short 等类型与 int 类型进行转换，
        // 以及使用 Float.floatToIntBits 或 DOuble.doubleToIntBits 来转换浮点数。
        AtomicInteger atomicInteger = new AtomicInteger();
        AtomicLong atomicLong = new AtomicLong();
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        AtomicReference atomicReference = new AtomicReference();

        // 原子数组类
        // 原子数组类中的元素可以实现原子更新，为数组的元素提供了 volatile 的访问语义。
        // volatile 类型的数组只在数组引用上具有 volatile 语义，在其元素上则没有。
        AtomicIntegerArray integerArray = new AtomicIntegerArray(10);
        AtomicLongArray longArray = new AtomicLongArray(10);
        AtomicReferenceArray referenceArray = new AtomicReferenceArray(10);
    }
}
