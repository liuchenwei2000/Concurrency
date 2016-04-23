## 原子变量（Atomic Variables） ##

### 产生背景

原子变量（Atomic Variables）在 Java5 版本被引入，用来提供对单个变量的原子操作支持。这是因为在 Java 语言里，对正常变量的每个操作都会在编译时被转换成多条机器指令。比如对某个变量进行赋值操作，这在 Java 语言里只需要一条语句，但编译源代码后，这条语句将会被转换成多条 JVM 指令。这样一来，当在多线程环境中共享某个变量时就会产生数据不一致问题。为此，Java 引入了原子变量。

### 原理

当某个线程正在操作一个原子变量时，如果其他线程也想操作这个变量，原子变量类会提供检查操作是否在一步内完成的机制。也就是说，操作会先获取原子变量的值，修改本地变量值，然后尝试以新值替换原子变量的旧值，如果此时旧值仍然是最初获取的那个值，那就直接替换它；
否则再次进行上述操作步骤。这个操作步骤称为 Compare And Set（简称 CAS，比较并交换）。

### CAS

在多线程环境编程时，为了避免数据不一致问题，通常会使用锁机制（synchronized 或 Lock）。但锁机制会产生如下问题：

* 死锁（Deadlock）
* 即使只有一个线程访问共享对象，它也必须要先获得锁然后再释放锁，造成性能损失。

为了避免上述问题并提供更优的性能，引入了 compare-and-swap（CAS）方式。CAS 操作通过以下3个步骤来实现对变量值的修改：

* 获取当前内存中变量的值。
* 用一个新的临时变量（temporal variable）保存改变后的新值。
* 如果当前内存中的值等于变量的旧值，则将新值赋值到变量；否则不进行任何操作。

使用 CAS 的方式可以不使用锁机制，从而避免了死锁。这种机制能保证多个并发线程对一个共享变量操作做到最终一致。

Java 在原子变量类（如 AtomicInteger）中实现了 CAS 机制，这些类提供了 compareAndSet() 方法——这个方法是 CAS 操作的实现和其他方法的基础。

### 与锁机制的区别

原子变量没有使用锁或其他同步机制来保护对变量值的访问，所有的操作都是基于 CAS 的。这能保证多个线程可以在同一时间操作同一个原子变量而不会产生数据不一致问题，并且性能也会比使用加锁机制保护的变量要好。

示例模拟银行转账的操作，一个公司朝账户打钱，银行从账户扣钱（即两个线程同时修改某个变量的操作）。