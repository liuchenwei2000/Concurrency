## 阻塞队列和生产者-消费者模式 ##

阻塞队列提供了可阻塞的 put 和 take 方法，以及支持定时的 offer 和 poll 方法。如果队列已经满了，那么 put 方法将阻塞直到空间可用；如果队列为空，那么 take 方法将会阻塞直到有元素可用。

阻塞队列支持生产者-消费者模式。该模式将“找出需要完成的工作”与“执行工作”这两个过程分离开来，并把工作项放入一个“待完成”列表中以便在随后处理，而不是找出后立即处理。一种常见的设计是线程池与工作队列的组合。虽然生产者-消费者模式能够将生产者和消费者的代码彼此解耦，但它们的行为仍然会通过共享队列间接地耦合在一起。

BlockingQueue 的实现类主要包括：LinkedBlockingQueue、ArrayBlockingQueue、PriorityBlockingQueue 和 SynchromousQueue。

### 串行线程封闭

阻塞队列包含了足够的内部同步机制，从而安全地将对象从生产者线程发布到消费者线程。

对于可变对象，生产者-消费者模式与阻塞队列一起，促进了串行线程封闭，从而将对象所有权从生产者交付给消费者。线程封闭对象只能由单个线程拥有，但可以通过安全地发布该对象来转移所有权。在转移所有权后，只有一个线程能获得该对象的访问权限，并且发布对象的线程不会再访问它。这种安全的发布确保了对象状态对于新的所有者来说是可见的，并且由于最初的所有者不会再访问它，因此它被封闭在新的线程中。新的所有者线程可以对该对象做任意修改，因为它具有独占的访问权。

### 双端队列与工作窃取

Java6 新增了两种容器类型：Deque 和 BlockingDeque，它们分别对 Queue 和 BlockingQueue 进行了扩展。Deque 是一个双端队列，实现了在队列头和队列尾的高效插入和移除。具体实现包括 ArrayDeque 和 LinkedBlockingDeque。

双端队列适用于工作窃取（work stealing）。在生产者-消费者设计中，所有消费者有一个共享的工作队列，而在工作窃取设计中，每个消费者都有各自的双端队列。如果一个消费者完成了自己双端队列中的全部工作，那么它可以从其他消费者双端队列末尾秘密地获取工作。工作窃取比传统的生产者-消费者模式具有更高的可伸缩性，因为工作线程不会在单个共享的任务队列上发生竞争。在大多数时候，它们都只是访问自己的双端队列，从而极大地减少了竞争。当工作线程要访问另一个线程的双端队列时，它会从队列的尾部而不是头部获取工作，因此进一步降低了队列上的竞争程度。

工作窃取非常适用于既是消费者又是生产者问题——当执行某个工作时可能导致出现更多的工作。例如网络爬虫处理一个页面时，通常会发现有更多的页面需要处理。当一个工作线程找到新的任务单元时，它会将其放到自己队列的末尾。当双端队列为空时，它会在另一个线程的队列队尾查找新的任务，从而确保每个线程都是忙碌的。