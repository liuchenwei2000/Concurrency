## 配置 ThreadPoolExecutor ##

ThreadPoolExecutor 为一些 Executor 提供了基本的实现，这些 Executor 是由 Executors 中的 newCachedThreadPool、newFixedThreadPool 和 newScheduledThreadExecutor 等工厂方法返回的。
ThreadPoolExecutor 是一个灵活的、稳定的线程池，允许进行各种定制。如果默认的执行策略不能满足需求，那么可以通过 ThreadPoolExecutor 的构造函数来实例化一个对象，并根据自己的需求来定制，并且可以参考 Executors 的源代码来了解默认配置下的执行策略，然后再以这些执行策略为基础进行修改。