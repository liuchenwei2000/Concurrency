package chapter11.item4;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1，缩小锁的范围（“快进快出”）
 * <p>
 *     降低发生竞争可能性的一种有效方式就是尽可能缩短锁的持有时间。
 *     例如，可以将一些与锁无关的代码移出同步代码块，尤其是那些开销较大的操作，
 *     以及可能被阻塞的操作，例如 I/O 操作。
 * <p>
 *     尽量缩小同步代码块能提高可伸缩性，但同步代码块也不能过小——一些需要采用原子方式执行的操作
 *     （例如对某个不变性条件中的多个变量进行更新）必须包含在一个同步块中。
 *     此外，同步需要一定的开销，当把一个同步代码块分解为多个同步代码块时，反而会对性能提升产生负面影响。
 *     在分解同步代码块时，仅当可以将一些“大量”的计算或阻塞操作从同步代码块中移出时，才应该考虑同步代码块的大小。
 * <p>
 * Created by liuchenwei on 2016/5/2
 */
public class NarrowingLockScope {

    /**
     * 初始版本，将整个 getValue 方法都加锁，实际上只有 attributes.get 这个方法才需要锁。
     */
    @ThreadSafe
    public static class AttributeStore {

        private final Map<String, String> attributes = new HashMap<>();

        public synchronized String getValue(String attrName) {
            String key = "key_" + attrName;
            String value = attributes.get(key);
            return value;
        }
    }

    /**
     * 更好的版本，只将对 attributes.get 方法的调用加锁，从而缩小了锁的作用范围。
     */
    @ThreadSafe
    public static class BetterAttributeStore {

        private final Map<String, String> attributes = new HashMap<>();

        public String getValue(String attrName) {
            String key = "key_" + attrName;
            String value;
            synchronized (this) {
                value = attributes.get(key);
            }
            return value;
        }
    }

    /**
     * 最优版本，将线程安全性委托给 ConcurrentHashMap 来进一步提升性能。
     */
    @ThreadSafe
    public static class BestAttributeStore {

        private final Map<String, String> attributes = new ConcurrentHashMap<>();

        public String getValue(String attrName) {
            String key = "key_" + attrName;
            String value = attributes.get(key);
            return value;
        }
    }
}
