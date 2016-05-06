package chapter15.item3;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 1，通过 CAS 来维持包含多个变量的不变性条件
 * <p>
 *     当不变性条件需要限制一个以上的数值时，使用 volatile/AtomicInteger 无法在同时更新时还维持不变性。
 *     不论是使用多个 volatile 类型变量还是多个 AtomicInteger，都会出现不安全的“先检查再执行”操作序列。
 *     本例通过原子引用，通过对指向不可变对象的引用进行原子更新以避免竞态条件。
 * <p>
 * Created by liuchenwei on 2016/5/6.
 */
public class AtomicReferenceTest {

    @ThreadSafe
    public static class CasNumberRange {

        /**
         * 不可变对象，同时保存上界和下界
         */
        @Immutable
        private static class IntRange {

            private final int lower;
            private final int upper;

            public IntRange(int lower, int upper) {
                this.lower = lower;
                this.upper = upper;
            }
        }

        // 原子引用，保存不可变对象
        private final AtomicReference<IntRange> values;

        public CasNumberRange() {
            this.values = new AtomicReference<>(new IntRange(0, 0));
        }

        public void setLower(int lower) {
            while (true) {
                IntRange oldV = values.get();
                if (lower > oldV.upper) {
                    throw new IllegalArgumentException("Can't set lower to " + lower + " > upper");
                }
                IntRange newV = new IntRange(lower, oldV.upper);
                // 使用 compareAndSet 使得更新上界或下界时能避免竞态条件。
                if (values.compareAndSet(oldV, newV)) {
                    return;
                }
            }
        }

        public void setUpper(int upper) {
            while (true) {
                IntRange oldV = values.get();
                if (upper < oldV.lower) {
                    throw new IllegalArgumentException("Can't set upper to " + upper + " < lower");
                }
                IntRange newV = new IntRange(oldV.lower, upper);
                if (values.compareAndSet(oldV, newV)) {
                    return;
                }
            }
        }

        public int getLower() {
            return values.get().lower;
        }

        public int getUpper() {
            return values.get().upper;
        }
    }
}
