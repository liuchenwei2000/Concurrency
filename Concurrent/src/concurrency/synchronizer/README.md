## 同步器 ##


`java.util.concurrent` 包提供了可以帮助管理相互合作线程集的类：
CyclicBarrier、CountDownLatch、Exchanger、Semaphore、SynchronousQueue，这些类为线程之间的集结点模式提供了预置功能。

如果有一个相互合作的线程集满足这些行为模式之一，那就应该直接重用合适的类而不要提供手工的锁和条件的集合。
