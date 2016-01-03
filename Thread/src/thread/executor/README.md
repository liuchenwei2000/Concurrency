## Executor 框架 ##


Java5 开始提供了 Executor 框架，它以 Executor 接口、其子接口 ExecutorService 及实现了这两个接口的类 ThreadPoolExecutor 为核心。它将多线程任务的创建和执行分离开来，有了 Executor ，你只需要实现 Runnable 接口，然后将任务对象发送到 Executor 即可，Executor 负责任务的执行。

Executor 框架具有如下优点：

* 通过使用线程池，可以避免因持续产生大量连续线程而拖垮系统。
* 可以将 Callable 对象通过 Executor 执行，它比 Runnable 更灵活并且还可以返回一个结果。
