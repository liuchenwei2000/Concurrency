## 实例封闭 ##

封装简化了线程安全类的实现过程，它提供了一种实例封闭机制（Instance Confinement）。当一个对象被封装到另一个对象中时，能够访问被封装对象的所有代码路径都是一致的。通过将封闭机制与合适的加锁策略结合起来，可以确保以线程安全的方式来使用非线程安全的对象。

对象可以封闭在类的一个实例（例如作为类的一个私有成员）中，或者封闭在某个作用域内（例如作为一个局部变量），再或者封闭在线程内（例如在某个线程中将对象从一个方法传递到另一个方法，而不是在多个线程之间共享该对象）。当然，对象本身不会逸出——出现逸出情况的原因通常是由于开发人员在发布对象时超出了对象既定的作用域。