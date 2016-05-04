## 构建自定义的同步工具 ##

创建状态依赖类的最简单方法通常是在类库中现有状态依赖类的基础上进行构造。
如果类库没有提供需要的功能，那么还可以使用 Java 语言和类库提供的底层机制来构造自己的同步机制，包括内置的条件队列、显式的 Condition 对象以及 AbstractQueuedSynchronizer 框架。