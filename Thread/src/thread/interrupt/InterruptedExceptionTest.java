/**
 * 
 */
package thread.interrupt;

import java.io.File;

/**
 * InterruptedException演示
 * <p>
 * 如果线程中运行的任务算法复杂且分解到了多个方法中，或者方法中有递归调用时，可以使用一种更好的机制来控制线程中断。
 * InterruptedException 被用来达到这个目的，可以在探测到线程中断时抛出这个异常并在run()方法中catch它。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-6
 */
public class InterruptedExceptionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filePath = "E:/Programs";
		String fileName = "Test.java";
		
		Thread task = new Thread(new FileSearchTask(filePath,fileName));
		
		task.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		task.interrupt();
		
		System.out.println("Main thread ends.");
	}
	
	/**
	 * 在目标文件夹中查找指定文件任务，算法有递归调用
	 */
	private static class FileSearchTask implements Runnable {

		private String filePath;
		private String fileName;

		public FileSearchTask(String filePath, String fileName) {
			this.filePath = filePath;
			this.fileName = fileName;
		}

		@Override
		public void run() {
			File home = new File(filePath);

			if (home.exists() && home.isDirectory()) {
				try {
					/*
					 * 一旦processDirectory()方法中（不论其内递归调用的哪个层级的方法）抛出InterruptedException，
					 * run()方法就会catch它并向下运行，而不用关心有多少层递归调用。
					 */
					processDirectory(home);
				} catch (InterruptedException e) {// 当线程被中断时的处理
					System.out.printf("%s: The search has been interrupted",
							Thread.currentThread().getName());
				}
			}
		}

		private void processDirectory(File home) throws InterruptedException {
			File[] files = home.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						processDirectory(file);
					} else {
						processFile(file);
					}
				}
			}
			// 检查线程是否被中断，如果是则抛出InterruptedException
			if(Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
		}

		private void processFile(File file) throws InterruptedException {
			if (file.getName().equals(fileName)) {
				System.out.printf("%s : %s\n",
						Thread.currentThread().getName(),
						file.getAbsolutePath());
			}
			// 检查线程是否被中断，如果是则抛出InterruptedException
			if(Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
		}
	}
}
