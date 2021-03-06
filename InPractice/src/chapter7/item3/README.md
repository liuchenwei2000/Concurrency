## 处理非正常的线程终止 ##

如果并发程序中的某个线程发生故障，在控制台中可能会输出栈追踪信息，但没有人会观察控制台。
导致线程提前死亡的最主要原因是 RuntimeException，由于这些异常表示出现了某种编程错误或者其他不可修复的错误，因此它们通常不会被捕获。
它们不会在调用栈中逐层传递，而是默认地在控制台中输出栈追踪信息，并终止线程。

对于线程池内部的工作线程，一般的实现原理是这样的：
如果任务抛出了一个未检查异常，那么它将使线程终结，但会首先通知框架该线程已经终结。
然后，框架可能会用新的线程来代替这个工作线程，也可能不会，因为线程池正在关闭，或者当前已有足够多的线程能满足需要。
ThreadPoolExecutor 是通过这项技术来确保行为糟糕的任务不会影响到后续任务的执行。