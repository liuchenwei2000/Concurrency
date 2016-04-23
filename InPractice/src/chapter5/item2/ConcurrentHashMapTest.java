package chapter5.item2;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap 示例
 * <p>
 *     ConcurrentHashMap 使用了一种安全不同的加锁策略来提供更高的并发性和伸缩性。
 *     它不是将每个方法都在同一个锁同步并使得每次只能有一个线程访问容器，
 *     而是使用一种粒度更细的加锁机制来实现更大程度的共享，这种机制被称为分段锁。
 *
 *     在这种机制中，任意数量的读取线程可以并发地访问 Map，执行读取操作的线程和执行写入操作的
 *     线程可以并发地访问 Map，并且一定数量的写入线程可以并发地修改 Map。
 * <p>
 *     用 ConcurrentHashMap 来代替同步 Map（Hashtable 和 synchronizedMap）能进一步
 *     提高代码的可伸缩性。只有当应用程序需要加锁 Map 以进行独占访问时，才应该放弃使用它。
 * <p>
 * Created by liuchenwei on 2016/4/23
 */
public class ConcurrentHashMapTest {

    private ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public void somemore() {
        /*
        * 由于 ConcurrentHashMap 不能被加锁来执行独占访问，因此无法使用客户端加锁来创建新的原子操作。
        * 但是，一些常见的复合操作，例如“若没有则添加”、“若相等则移除”等，
        * 都已经实现为原子操作且在 ConcurrentMap 接口中声明。
        */
        map.putIfAbsent("key", "value");
        map.remove("key", "value");
    }
}
