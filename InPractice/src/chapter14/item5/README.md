## AbstractQueuedSynchronizer ##

大多数开发者都不会直接使用 AbstractQueuedSynchronizer（AQS），标准同步器类能满足绝大多数情况的需要。在基于 AQS 构建的同步器类中，最基本的操作包括各种形式的获取操作和释放操作。**获取操作**是一种依赖状态的操作，并且通常会阻塞。当使用锁或信号量时，获取操作的含义就很直观，即获取的是锁或者许可，并且调用者可能会一直等待直到同步器类处于可被获取的状态。**释放操作**并不是一个可阻塞的操作，当执行“释放”操作时，所有在请求时被阻塞的线程都会开始执行。

如果一个类想成为状态依赖的类，那么它必须拥有一些状态。AQS 负责管理同步器类中的状态，它管理了一个整数状态信息。可以通过 getState、setState 以及 compareAndSetState 等 protected 方法来进行操作。这个整数可以用于表示任意状态。例如，ReentrantLock 用它来表示所有者线程已经重复获取该锁的次数，Semaphore 用它来表示剩余的许可数量。在同步器类中还可以自行管理一些额外的状态变量，例如，ReentrantLock 保存了锁的当前所有者的信息，这样就能区分某个获取操作是重入的还是竞争的。

下面的伪代码给出了 AQS 中的获取操作和释放操作的形式。

	boolean acquire() throws InterruptedException {
		while (state does not permit acquire) {
			if (blocking acquisition requested) {
				enqueue current thread if not already queued
				block current thread
			}
			else
			return failure
		}
		possibly update synchronization state
		dequeue thread if it was queued
		return success
	}

	void release() {
		update synchronization state
		if (new state may permit a blocked thread to acquire)
			unblock one or more queued threads
	}

根据同步器的不同，获取操作可以是一种独占操作（ReentrantLock），也可以是一种非独占操作（Semaphore、CountDownLatch）。一个获取操作包括两部分：首先，同步器判断当前状态是否允许获得操作，如果是则允许线程执行，否则获取操作将阻塞或失败。这种判断是由同步器的语义决定的。例如，对于锁来说，如果它没有被某个线程持有，那么就能被成功地获取，而对于闭锁来说，如果它处于结束状态，那么也能被成功地获取。其次，就是更新同步器的状态，获取同步器的某个线程可能会对其它线程能否也获取该同步器造成影响。例如，当获取一个锁后，锁的状态将从“未被持有”变成“已被持有”，而从 Semaphore 中获取一个许可后，将把剩余许可的数量减一。

如果某个同步器支持独占的获取操作，那么需要实现一些保护方法，包括 tryAcquire、tryRelease 和 isHeldExclusively 等，而对于支持共享获取的同步器，则应该实现 tryAcquireShared、tryReleaseShared 等方法。AQS 中的 acquire、acquireShared、release、releaseShared 等方法都将调用这些方法在子类中带有前缀 try 的版本来判断某个操作是否能执行。在同步器的子类中，可以根据其获取操作和释放操作的语义，使用 getState、setState 以及 compareAndSetState 来检查和更新状态，并通过返回的状态值来告知基类“获取”或“释放”同步器的操作是否成功。例如，如果 tryAcquireShared 返回一个负值，那么表示获取操作失败，返回零值表示同步器通过独占方式被获取，返回正值表示同步器通过非独占方式被获取；对于 tryRelease 和 tryReleaseShared 方法来说，如果释放操作使得所有在获取同步器时被阻塞的线程恢复执行，那么这两个方法应该返回 true。