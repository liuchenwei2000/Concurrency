## 同步容器类 ##

同步容器类包括 Vector、Hashtable 以及通过 Collections.synchronizedXxx 方法创建的容器类。
这些类实现线程安全的方式是：将它们的状态封装起来，并对每个公有方法都进行同步，使得每次只有一个线程能访问容器的状态。