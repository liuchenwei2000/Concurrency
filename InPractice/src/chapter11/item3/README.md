## 线程引入的开销 ##

### 上下文切换

如果可运行的线程数大于 CPU 的数量，那么操作系统最终会将某个正在运行的线程调度出来，从而使其他线程都能够使用 CPU。这将导致一次上下文切换，在这个过程中将保存当前运行线程的执行上下文，并将新调度进来的线程的上下文设置为当前上下文。

当线程由于等待某个发生竞争的锁而阻塞时，JVM 通常会将这个线程挂起，并允许它被交换出去。如果线程频繁地发生阻塞，那么它们将无法使用完整的调度时间片。在程序中发生越多的阻塞（包括阻塞 I/O、等待获取发生竞争的锁、或者在条件变量上等待），CPU 密集型的程序就会发生越多的上下文切换，从而增加调度开销，并因此而降低吞吐量。

在大多数通用的处理器中，上下文切换的开销相当于 5000-10000 个时钟周期，也就是几微妙。

### 内存同步

同步操作的性能开销包括多个方面，在 sychronized 和 volatile 提供的可见性保证中可能会使用一些特殊指令，这将抑制一些编译器优化操作。现代的 JVM 能通过优化来去掉一些不会发生竞争的锁，从而减少不必要的同步开销。

某个线程中的同步可能会影响其他线程的性能。同步会增加共享内存总线上的通信量，总线的带宽是有限的，并且所有的处理器都将共享这条总线。如果有多个线程竞争同步带宽，那么所有使用了同步的线程都会受到影响。

### 阻塞

非竞争的同步完全可以在 JVM 中进行处理，而竞争的同步可能需要操作系统的介入，从而增加开销。当线程无法获取某个锁或者由于在某个条件等待或在 I/O 操作上阻塞时，需要被挂起，在这个过程中将包含两次额外的上下文切换，以及所有必要的操作系统操作和缓存操作：被阻塞的线程在其执行时间片还未用完之前就被交换出去，而在随后当要获取的锁或者其他资源可用时，又再次被切换回来。由于锁竞争而导致阻塞时，线程在持有锁时将存在一定的开销：当它释放锁时，必须告诉操作系统恢复运行阻塞的线程。