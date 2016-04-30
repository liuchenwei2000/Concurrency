## 配置 ThreadPoolExecutor ##

ThreadPoolExecutor 为一些 Executor 提供了基本的实现，这些 Executor 是由 Executors 中的 newCachedThreadPool、newFixedThreadPool 和 newScheduledThreadExecutor 等工厂方法返回的。
ThreadPoolExecutor 是一个灵活的、稳定的线程池，允许进行各种定制。如果默认的执行策略不能满足需求，那么可以通过 ThreadPoolExecutor 的构造函数来实例化一个对象，并根据自己的需求来定制，并且可以参考 Executors 的源代码来了解默认配置下的执行策略，然后再以这些执行策略为基础进行修改。

`public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                           TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory)`

### 线程的创建与销毁

线程池的基本大小（Core Pool Size）、最大大小（Maximum Pool Size）以及存活时间等因素共同负责线程的创建与销毁。基本大小也就是线程池的目标大小，即在没有任务执行时线程池的大小，并且只有在工作队列满了的情况下才会创建超出这个数量的线程。线程池的最大大小表示可同时活动的线程数量的上限。如果某个线程的空闲时间超过了存活时间，那么将被标记为可回收的，并且当线程池的当前大小超过了基本大小时，这个线程将被终止。

Executors.newFixedThreadPool 方法将线程池的基本大小和最大大小设置为参数中指定的值，而且创建的线程不会超时。Executors.newCachedThreadPool 方法将线程池的最大大小设置为 Integer.MAX_VALUE，而将基本大小设为零，并将超时设为一分钟，这种方法创建出来的线程池可以被无限扩展，并且当需求降低时会自动收缩。

### 管理队列任务

ThreadPoolExecutor 允许提供一个 BlockingQueue 来保存等待执行的任务。基本的任务排列方法有三种：无界队列、有界队列和同步移交。队列的选择与其他的配置参数有关，例如线程池的大小等。

Executors.newFixedThreadPool 方法和 Executors.newSingleThreadExecutor 方法在默认情况下将使用一个无界的 LinkedBlockingQueue。一种更稳妥的资源管理策略是使用有界队列，例如 ArrayBlockingQueue、有界的 LinkedBlockingQueue、PriorityBlockingQueue。有界队列有助于避免资源耗尽的情况发生。

对于非常大的或者无界的线程池，可以通过使用 SynchronousQueue 来避免任务排队，以及直接将任务从生产者移交到工作者线程（同步移交）。SynchronousQueue 不是一个真正的队列，而是一种在线程之间进行移交的机制。要将一个元素放入 SynchronousQueue 中，必须有另一个线程正在等待接受这个元素。如果没有线程正在等待，并且线程池的当前大小小于最大值，那么 ThreadPoolExecutor 将创建一个新的线程，否则根据饱和策略，这个任务将被拒绝。使用直接移交将更高效，因为任务会直接移交给执行它的线程，而不是被首先放在队列中，然后由工作者线程从队列中提取该任务。只有当线程池是无界的或者可以拒绝任务时，SynchronousQueue 才有实际价值。在 Executors.newCachedThreadPool 方法中就是用了 SynchronousQueue。

当使用像 LinkedBlockingQueue 或 ArrayBlockingQueue 这样的 FIFO 队列时，任务的执行顺序与它们的到达顺序相同。如果想进一步控制任务执行顺序，还可以使用 PriorityBlockingQueue，这个队列将根据优先级来安排任务。

### 饱和策略

当有界队列被填满后，饱和策略开始发挥作用。ThreadPoolExecutor 的饱和策略可以通过调用 setRejectedExecutionHandler 来设置。
JDK 提供了几种不同的 RejectedExecutionHandler 实现对应不同的饱和策略：AbortPolicy、CallerRunsPolicy、DiscardPolicy、DiscardOldestPolicy。详见 SaturationPolicyTest.java。

当工作队列被填满后，没有预定义的饱和策略来阻塞 execute 方法（以阻止继续提交新任务）。然而通过使用 Semaphore 来限制任务的到达率就可以实现这个功能。详见 BoundedExecutor.java。

### 线程工厂

每当线程池需要创建一个线程时，都是通过线程工厂（ThreadFactory）的 newThread 方法来完成的。默认的线程工厂将创建一个新的、非守护的线程，并且不包含特殊的配置信息。通过指定一个线程工厂，可以定制线程池的配置信息。详见 ThreadFactoryTest.java。