## 取消与关闭 ##

要使任务和线程能安全、快速、可靠地停止下来，并不是一件容易的事。Java 没有提供任何机制来安全地终止线程。
但它提供了**中断**（interuption），这是一种协作机制，能够使一个线程终止另一个线程的当前工作。
这种协作式的方法是必要的，在编写任务和服务时可以使用一种协作的方式：
当需要停止时，它们首先会清除当前正在执行的工作，然后再结束。
这提供了更好的灵活性，因为任务本身的代码比发出取消请求的代码更清楚如何执行清除工作。