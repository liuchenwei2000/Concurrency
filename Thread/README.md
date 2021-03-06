## 线程 ##

### 线程与进程 ###

* 本质区别在于每个进程拥有自己的一整套变量，而线程则共享数据。共享数据使线程之间的通信比进程之间更有效、更容易。
* 与进程相比较，线程更轻量级，创建/销毁一个线程比创建/销毁一个进程的开销要小得多。

多线程的应用场景有：浏览器同时下载多个图片，一个 Web 服务器需要同时响应几个并发的请求。

当一个 Java 程序启动时会有一个主线程(main thread)立即启动，它的重要性体现在两方面：
1，它是产生其他子线程的线程。
2，通常它必须最后完成，因为它执行各种关闭动作。


### 线程状态 ###

线程可以有以下六种状态：新生（new）、可运行（runnable）、被阻塞（blocked）、等待（waiting）、计时等待（time waiting）、被终止的（terminated）。

* 新生

	当用new操作符创建一个线程时，线程还没有开始运行，此时线程处于新生状态。

* 可运行

	一旦调用了start方法，该线程就成为可运行的了。一个可运行线程可能实际上正在运行，也可能没有，这取决于操作系统为该线程提供的运行时间。

* 被阻塞、等待、计时等待

	当线程处于被阻塞或等待状态时，它暂时不活动。它不运行任何代码且消耗最少的资源，直到线程调度器重新激活它。

	* 当一个线程试图获取一个内部的对象锁，而该锁被其他线程持有，则该线程进入阻塞状态。
	* 当线程等待另一个线程通知调度器一个条件时，它自己进入等待状态。
	* 有几个方法有一个超时参数，调用它们导致线程进入计时等待状态。如Thread.sleep和Object.wait。
	
* 被终止的

	有两个原因会导致线程被终止：
	* run方法正常退出而自然终止。
	* 因为一个未捕获的异常终止了run方法而意外终止。


### 废弃 stop 方法和 suspend 方法的原因 ###

* stop方法会破坏原子性操作，导致对象被破坏。
	
	比如某个转账操作刚从源账户扣掉了一笔钱，还没有给目标账户加上这笔钱之前线程被stop了，操作的原子性就被破坏了。

* suspend方法会导致死锁
