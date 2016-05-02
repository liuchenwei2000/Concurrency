## 其他活跃性危险 ##

### 饥饿

当线程由于无法访问它所需要的资源而不能继续执行时，就发生了饥饿（Starvation）。引发饥饿的最常见资源就是 CPU 时钟周期。如果在程序中对线程的优先级使用不当或者在持有锁时执行一些无法结束的结构（例如无限循环或者无限制地等待某个资源），那么也可能导致饥饿，因为其他需要这个锁的线程将无法得到它。

通常要避免使用线程优先级，因为这会增加平台依赖性，并可能导致活跃性问题。在大多数并发应用程序中，都可以使用默认的线程优先级。

### 活锁

活锁（Livelock）是另一种形式的活跃性问题，该问题尽管不会阻塞线程，但也不能继续执行，因为线程将不断重复执行相同的操作，而且总会失败。这种形式的活锁通常是由过度的错误恢复代码造成的，因为它错误地将不可修复的错误作为可修复的错误。

当多个相互协作的线程都对彼此进行响应从而修改各自的状态，并使得任何一个线程都无法继续执行时，就发生了活锁。这就像两个过于礼貌的人在半路上面对面地相遇了，他们彼此都让出对方的路，然而又在另一条路上相遇了。因此他们就这样反复地避让下去。

要解决活锁问题，需要在重试机制中引入随机性，在并发应用程序中，通过等待随机长度的时间和回退可以有效地避免活锁的发生。