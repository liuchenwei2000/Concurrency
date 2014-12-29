/**
 * 
 */
package forkjoin.asyn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * �ļ�������
 * <p>
 * ���Բ���ָ��·�������з�����չ�����ļ���
 * <p>
 * ����ForkJoinPool������һ��ForkJoinTaskʱ������ѡ��ͬ��ģʽ�����첽ģʽ��
 * <li>
 * ѡ��ͬ��ģʽʱ�������ForkJoinTask.invokeAll()������������ֱ���������н����Ż᷵�أ�
 * ��������ģʽ����ForkJoinPoolʹ�ù�����ȡ�㷨��Ϊĳ�����ڵȴ�������ɵĹ����̷߳����µ����񣩣�
 * <li>
 * ѡ���첽ģʽʱ�������ForkJoinTask.fork()������������ֱ�ӷ��أ�������������ִ�У�
 * ��ʱForkJoinPool����ʹ�ù�����ȡ�㷨������ܣ�ֻ���ڵ���ForkJoinTask.join()��ForkJoinTask.get()����
 * �ȴ�������ɵ�ʱ��ForkJoinPool�Ż�ʹ�ù�����ȡ�㷨��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��29��
 */
public class FileProcessor extends RecursiveTask<List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String path;// Ŀ¼
	private String extension;// ��չ��
	
	public FileProcessor(String path, String extension) {
		this.path = path;
		this.extension = extension;
	}

	/**
	 * ʵ��compute()����������List<String>��ʾ���з�����չ�����ļ�·����
	 * 
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected List<String> compute() {
		// ÿһ����Ŀ¼��ʹ��һ����������в���
		List<FileProcessor> tasks = new ArrayList<>();
		// ������չ�����ļ�·���б�
		List<String> result = new ArrayList<>();
		
		File file = new File(path);
		File[] children = file.listFiles();
		
		if(children != null) {
			for (File child : children) {
				/*
				 * ������ļ���Ŀ¼��������ͻᴴ����һ�������������������Ŀ¼������ͨ��������fork()������
				 * ����뵽ForkJoinPool�У�pool����п��еĹ����̻߳��߿��Դ���һ���¹����߳����ִ�и�������
				 * fork()�������������أ����Ա�������Լ�����������Ĵ���
				 */
				if (child.isDirectory()) {
					// ��ÿ����Ŀ¼����ʹ���첽����(fork())ִ��������
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
		 * һ���������������е�Ŀ¼��������Ŀ¼����ͨ������join()�����ȴ�ForkJoinPool�������������ɣ��÷����᷵�����Ӧ�����compute()�����ķ���ֵ��
		 * ������Ὣ��������������䴴��������������ļ������ϲ�����Ϊ����compute()�����ķ���ֵ��
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
			 * ����ʹ��join()�����ȴ����������ý��֮�⣬������ʹ��get()�������Ŀ�ģ�
			 * get()���������������ʱ����compute()�����ķ���ֵ������һֱ�ȴ����������
			 * get(long timeout, TimeUnit unit)�����������ó�ʱʱ�䣬һ����ʱ�򷵻�null��
			 * 
			 * join()��get()�������������ڣ�
			 * 1��join()�ǲ��ɱ��ж�(interrupted)�ģ�����ж�ִ��join()�������߳�����׳�InterruptedException��
			 * 2�������������׳��ķ��ܲ��쳣��get()�����᷵��ExecutionException����join()�᷵��RuntimeException��
			 */
			result.addAll(task.join());
		}
	}
}
