## 使用条件队列 ##

条件队列使构建高效以及高可响应性的状态依赖类变得更容易，但同时也很容易被不正确地使用。所以要尽量基于 LinkedBlockingQueue、Latch、Semaphore 和 FutureTask 等类来构造程序，如果能避免使用条件队列，那么实现起来将容易许多。

### 条件谓词

要想正确地使用条件队列，关键是找出对象在哪个条件谓词上等待。条件谓词将在等待与通知等过程中导致很多困惑，因为在 API 中没有对条件谓词进行实例化的方法。

条件谓词是使某个操作成为状态依赖操作的前提条件。在有界缓存中，只有当缓存不为空时，take 方法才能执行，否则必须等待。对 take 方法来说，它的条件谓词就是“缓存不为空”，take 方法在执行之前必须首先测试该条件谓词。条件谓词是由类中各个状态变量构成的表达式，如测试“缓存不为空”时将 count 与 0进行比较。

在条件等待中存在一种重要的三元关系，包括加锁、wait 方法和一个条件谓词。在条件谓词中包含多个状态变量，而状态变量由一个锁来保护，因此在测试条件谓词之前必须先持有这个锁。锁对象与条件队列对象（即调用 wait 和 notify 等方法所在的对象）必须是同一个对象。

在 BoundedBuffer 中，缓存的状态是由缓存锁保护的，并且缓存对象被用做条件队列。take 方法将先请求缓存锁，然后对条件谓词（缓存非空）进行测试，如果缓存非空，则会删除第一个元素。之所以能这样做，是因为 take 此时仍然持有保护缓存状态的锁。

如果条件谓词不为真（缓存为空），那么 take 必须等待并直到另一个线程在缓存中放入一个对象。take 将在缓存的内置队列上调用 wait 方法，这需要持有条件队列对象上的锁。这是一种谨慎的设计，因为 take 方法已经持有在测试条件谓词时需要的锁（并且如果谓词条件为真，那么将在同一个原子操作中修改缓存的状态）。wait 方法将释放锁，阻塞当前线程，并等待直到超时，然后线程被中断或者通过一个通知被唤醒。在唤醒线程后，wait 在返回前还要重新获取锁。当线程从 wait 方法中被唤醒时，它在重新请求锁时不具有任何特殊的优先级，而要与其他尝试进入同步代码块的线程一起正常地在锁上进行竞争。

### 过早唤醒

内置条件队列可以与多个条件谓词一起使用。当一个线程由于调用 notifyAll 而醒来时，并不意味着该线程正在等待的条件谓词已经变成真。当执行控制重新进入调用 wait 的代码时，它已经重新获取了与条件队列相关联的锁。在发出通知的线程调用 notifyAll 时，条件谓词可能已经变成真，但在重新获取锁时将再次变为假（在线程被唤醒到 wait 重新获得锁的这段时间里，可能有其他线程已经获取了这个锁，并修改了对象的状态）。或者条件谓词从调用 wait 起根本就没有变成真。并不知道另一个线程为什么调用 notify/notifyAll，也许是因为与同一条件队列相关的另一个条件谓词变成了真。一个条件队列与多个条件谓词相关是一种很常见的情形——在 BoundedBuffer 中使用的条件队列与“非满”和“非空”两个条件谓词相关。

基于上述这些原因，每当线程从 wait 中唤醒时，都必须再次测试条件谓词，如果不为真，那么就继续等待（或失败）。由于线程在条件谓词不为真的情况下也可以反复地醒来，因此必须在一个循环中调用 wait，并在每次迭代中都测试条件谓词。条件的标准形式如下：

	// 必须通过一个锁来保护条件谓词
	synchronized(lock){
		while(!conditionPredicate()){
			lock.wait();
		}
		// 现在对象处于合适的状态
	}

### 通知

每当在等待一个条件时，一定要确保在条件谓词变为真时通过某种方式发出通知。在条件队列 API 中有两个发出通知的方法，即 notify 和 notifyAll。无论调用哪一个，都必须持有与条件队列对象相关联的锁。在调用 notify 时，JVM 会从这个条件队列上等待的多个线程中随机选择一个来唤醒，而调用 notifyAll 则会唤醒在这个条件队列上等待的所有线程。由于在调用 notify 和 notifyAll 时必须持有条件队列对象的锁，而如果这些等待中线程此时不能重新获得锁，那么无法从 wait 返回，因此发出通知的线程应该尽快地释放锁，从而确保正在等待的线程尽可能地解除阻塞。

由于多个线程可以基于不同的条件谓词在同一个条件队列上等待，因此如果使用 notify 而不是 notifyAll，那么将是一种危险的操作，因为单一的通知很容易导致类似于信号丢失的问题。

只有同时满足以下两个条件时，才能用 notify 而不是 notifyAll：
* 所有等待线程的类型都相同 只有一个条件谓词与条件队列相关，并且每个线程在从 wait 返回后将执行相同的操作。
* 单进单出 在条件变量上的每次通知，最多只能唤醒一个线程来执行。

由于大多数类并不满足这些需求，因此普遍的做法是优先使用 notifyAll 而不是 notify。虽然 notifyAll 可能比notify 更低效，但却更容易确保类的行为是正确的。条件通知详见 ConditionalNotification.java。

### 子类的安全问题

在使用条件通知或单次通知时，一些约束条件使得子类化过程变得更加复杂。要想支持子类化，那么在设计类时需要保证：如果在实施子类化时违背了条件通知或单词通知的某个需求，那么在子类中可以增加合适的通知机制来代表基类。

对于状态依赖的类，要么将其等待和通知等协议完全向子类公开（并且写入正式文档），要么完全阻止子类参与到等到和通知等过程中。当设计一个可被继承的状态依赖类时，至少需要公开条件队列和锁，并且将条件谓词和同步策略写入文档，此外还需要公开一些底层的状态变量。另外一种选择就是完全禁止子类化，例如将类声明为 final 类型，或者将条件队列、锁和状态变量等隐藏起来，使子类看不见它们。