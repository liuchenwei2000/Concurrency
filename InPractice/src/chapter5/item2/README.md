## 并发容器 ##

Java5 提供了多种并发容器来改进同步容器类的性能。同步容器（如 Vector）将所有对容器状态的访问都串行化（每个方法都使用同一个锁）以实现它们的线程安全性。代价是严重降低并发性，当多个线程竞争容器的锁时，吞吐量将严重降低。另一方面，并发容器是针对多个线程并发访问设计的。

Java5 新增的 ConcurrentHashMap 用来替代同步且基于散列的 Map（如 Collections.synchronizedMap(new HashMap())）；CopyOnWriteArrayList 用于在遍历操作为主要操作的情况下代替同步的 List；在新的 ConcurrentMap 接口中增加了对一些常见复合操作的支持，如“若没有则添加”、条件删除等。Java6 引入了 ConcurrentSkipListMap 和 ConcurrentSkipListSet 分别作为同步的 SortedMap 和 SortedSet 的并发替代品。

另外，Java5 新增了两种容器类型：Queue 和 BlockingQueue。Queue 的实现包括 ConcurrentLinkedQueue（传统的先进先出队列）和 PriorityQueue（非并发的优先队列）。Queue 上的操作不会阻塞，如果队列为空，那么获取元素的操作将返回空值。

BlockingQueue 扩展了 Queue，增加了可阻塞的插入和获取等操作。如果队列为空，则获取元素的操作将一直阻塞，直到队列中出现了一个可用的元素。如果队列已满（对有界队列来说），那么插入元素的操作将一直阻塞，直到队列中出现可用的空间。

通过并发容器来代替同步容器，可以极大地提高伸缩性并降低风险。