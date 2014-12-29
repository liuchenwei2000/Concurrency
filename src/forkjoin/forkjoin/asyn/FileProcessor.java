/**
 * 
 */
package forkjoin.asyn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * 文件处理器
 * <p>
 * 可以查找指定路径下所有符合扩展名的文件。
 * <p>
 * 当在ForkJoinPool中运行一个ForkJoinTask时，可以选择同步模式或者异步模式。
 * <li>
 * 选择同步模式时（如调用ForkJoinTask.invokeAll()方法），方法直到任务运行结束才会返回，
 * 并且这种模式允许ForkJoinPool使用工作窃取算法（为某个正在等待任务完成的工作线程分配新的任务）；
 * <li>
 * 选择异步模式时（如调用ForkJoinTask.fork()方法），方法直接返回，任务会继续向下执行，
 * 此时ForkJoinPool不能使用工作窃取算法提高性能，只有在调用ForkJoinTask.join()或ForkJoinTask.get()方法
 * 等待任务完成的时候，ForkJoinPool才会使用工作窃取算法。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月29日
 */
public class FileProcessor extends RecursiveTask<List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String path;// 目录
	private String extension;// 扩展名
	
	public FileProcessor(String path, String extension) {
		this.path = path;
		this.extension = extension;
	}

	/**
	 * 实现compute()方法，返回List<String>表示所有符合扩展名的文件路径。
	 * 
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected List<String> compute() {
		// 每一个子目录会使用一个子任务进行查找
		List<FileProcessor> tasks = new ArrayList<>();
		// 符合扩展名的文件路径列表
		List<String> result = new ArrayList<>();
		
		File file = new File(path);
		File[] children = file.listFiles();
		
		if(children != null) {
			for (File child : children) {
				/*
				 * 如果子文件是目录，本任务就会创建另一个子任务来处理这个子目录，并且通过调用其fork()方法将
				 * 其加入到ForkJoinPool中，pool如果有空闲的工作线程或者可以创建一个新工作线程则会执行该子任务。
				 * fork()方法会立即返回，所以本任务可以继续进行下面的处理。
				 */
				if (child.isDirectory()) {
					// 对每个子目录，都使用异步调用(fork())执行子任务
					FileProcessor subTask = new FileProcessor(child.getAbsolutePath(), extension);
					subTask.fork();
					tasks.add(subTask);
				} else {
					if (checkExtension(child)) {
						result.add(file.getAbsolutePath());
					}
				}
			}
		}
		/*
		 * 一旦本任务处理完所有的目录（及其子目录），通过调用join()方法等待ForkJoinPool中所有任务的完成，该方法会返回其对应任务的compute()方法的返回值。
		 * 本任务会将其自身计算结果和其创建的所有子任务的计算结果合并，作为它的compute()方法的返回值。
		 */
		addResultsFromTasks(result, tasks);
		
		return result;
	}

	private boolean checkExtension(File file) {
		return file.getPath().toLowerCase().endsWith(extension);
	}

	private void addResultsFromTasks(List<String> result, List<FileProcessor> tasks) {
		for (FileProcessor task : tasks) {
			/*
			 * 除了使用join()方法等待任务结束获得结果之外，还可以使用get()方法达成目的：
			 * get()方法会在任务结束时返回compute()方法的返回值，否则一直等待任务结束。
			 * get(long timeout, TimeUnit unit)方法可以设置超时时间，一旦超时则返回null。
			 * 
			 * join()和get()方法的区别在于：
			 * 1，join()是不可被中断(interrupted)的，如果中断执行join()方法的线程则会抛出InterruptedException。
			 * 2，对于任务中抛出的非受查异常，get()方法会返回ExecutionException，而join()会返回RuntimeException。
			 */
			result.addAll(task.join());
		}
	}
}
