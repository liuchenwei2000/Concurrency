## Java 内存模型 ##

如果缺少同步，那么将会有许多因素使得线程无法立即看到另一个线程的操作结果（甚至永远无法看到）。在编译期生成的指令顺序，可以与源代码中的顺序不同，此外编译期还会把变量保存在寄存器中而不是内存中；处理器可以采用乱序或并行等方式来执行指令；缓存可能会改变将写入变量提交到主内存的次序；而且保存在处理器本地缓存中的值，对于其他处理器是不可见的。这些因素都会使得一个线程无法看到变量的最新值，并且会导致其他线程的内存操作似乎在乱序执行——如果没有使用正确的同步。

Java 语言规范要求 JVM 在线程中维护一组类似串行的语义：只要程序的最终结果与严格串行环境中执行的结果相同，那么上述所有操作都是允许的。这是一件好事，因为在最近几年中，计算性能的提升在很大程度上要归功于这些重新排序措施。随着处理器越来越强大，编译器也在不断地改进：通过对指令重新排序来实现优化执行，以及使用成熟的全局寄存器分配算法。由于时钟频率越来越难以提高，因此许多处理器厂商开始转而生产多核处理器，因为能够提高的只有硬件并行性。

Java 内存模型（JMM）必须遵循一组最小保证，这组保证规定了对变量的写入操作在何时将对于其他线程可见。JMM 在设计时就在可预测性和程序的易于开发性之间进行了权衡，从而在各种主流的处理器体系架构上能实现高性能的 JVM。

### 平台的内存模型

在共享内存的多处理器体系架构中，每个处理器都拥有自己的缓存，并且定期地与主内存进行协调。在不同的处理器架构中提供了不同级别的缓存一致性，其中一部分只提供最小的保证，即允许不同的处理器在任意时刻从同一个存储位置上看到不同的值。操作系统、编译期以及运行时需要弥合这种在硬件能力与线程安全需求之间的差异。

为了使 Java 开发人员无须关心不同架构上内存模型之间的差异，Java 还提供了自己的内存模型，并且 JVM 通过在适当的位置上插入内存栅栏（特殊的指令，当需要共享数据时，这些指令就能实现额外的存储协调保证）来屏蔽在 JMM 与底层平台内存模型之间的差异。

在现代支持共享内存的多处理器（和编译期）中，当跨线程共享数据时，会出现一些奇怪的情况，除非通过使用内存栅栏来防止这些情况的发生。幸运的是，Java 程序不需要指定内存栅栏的位置，而只需通过正确地使用同步找出何时将访问共享状态。

### 重排序

在没有充分同步的程序中，如果调度器采用不恰当的方式来交替执行不同线程的操作，那么将导致不正确的结果。更糟的是，JMM 还使得不同线程看到的操作顺序是不同的，从而导致在缺乏同步的情况下，要推断操作的执行顺序将变得更加复杂。各种使操作延迟或者看似乱序执行的不同原因，都可以归为重排序。

内存级的重排序会使程序的行为变得不可预测。如果没有同步，那么推断出执行顺序将是非常困难的，而要确保在程序中正确地使用同步却是非常容易的。同步将限制编译期、运行时和硬件对内存操作重排序的方式，从而在实施重排序时不会破坏 JMM 提供的可见性保证。

### Java 内存模型简介

Java 内存模型是通过各种操作来定义的，包括对变量的读/写操作，监视器的加锁和释放操作，以及线程的启动和合并操作。JMM 为程序中所有的操作定义了一个偏序关系，重置为 Happens-Before。要想保证执行操作B 的线程看到操作 A 的结果（无论 A 和 B 是否在同一个线程中执行），那么在 A 和 B 之间必须满足 Happens-Before 关系。如果两个操作之间缺乏 Happens-Before 关系，那么 JVM 可以对它们任意地重排序。

当一个变量被多个线程读取并且至少被一个线程写入时，如果在读操作和写操作之间没有依照 Happens-Before 来排序，那么就会产生数据竞争问题。在正确同步（锁的获取与释放以及 volatile 变量的读取与写入）的程序中不存在数据竞争，并会表现出串行一致性，这意味着程序中的所有操作都会按照一种固定的和全局的顺序执行。