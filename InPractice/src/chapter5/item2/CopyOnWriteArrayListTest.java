package chapter5.item2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList 示例
 * <p>
 *     CopyOnWriteArrayList 用于替代同步 List，在某些情况下它能提供更好的并发性能，
 *     并且在迭代期间不需要对容器进行加锁或复制。（类似的还有替代同步 Set 的 CopyOnWriteArraySet）
 * <p>
 *     CopyOnWriteArrayList 的线程安全性在于：
 *     只要正确地发布一个事实不可变对象，那么在访问该对象就不再需要进一步的同步。
 *     在每次修改时，都会创建并重新发布一个新的容器副本，从而实现可变性。
 *     容器的迭代器保留一个指向底层基础数组的引用，由于它不会被修改，因此在对其进行同步时
 *     只需确保数组内容的可见性。因此，多个线程可以同时对这个容器进行迭代，
 *     而不会彼此干扰或者与修改容器的线程相互干扰。另外，迭代器不会抛出 ConcurrentModificationException，
 *     并且返回的元素与迭代器创建时的元素完全一致，而不必考虑之后修改操作所带来的影响。
 * <p>
 *     每当修改容器时都会复制底层数组，这会带来一定的开销，特别是当容器的规模较大时。
 *     仅当迭代操作远远多于修改操作时，才应该使用 CopyOnWriteArrayList。
 * <p>
 * Created by liuchenwei on 2016/4/23
 */
public class CopyOnWriteArrayListTest {

    private CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
}
