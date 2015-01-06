/**
 * 
 */
package thread.interrupt;

import java.io.File;

/**
 * InterruptedException��ʾ
 * <p>
 * ����߳������е������㷨�����ҷֽ⵽�˶�������У����߷������еݹ����ʱ������ʹ��һ�ָ��õĻ����������߳��жϡ�
 * InterruptedException �������ﵽ���Ŀ�ģ�������̽�⵽�߳��ж�ʱ�׳�����쳣����run()������catch����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-6
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
	 * ��Ŀ���ļ����в���ָ���ļ������㷨�еݹ����
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
					 * һ��processDirectory()�����У��������ڵݹ���õ��ĸ��㼶�ķ������׳�InterruptedException��
					 * run()�����ͻ�catch�����������У������ù����ж��ٲ�ݹ���á�
					 */
					processDirectory(home);
				} catch (InterruptedException e) {// ���̱߳��ж�ʱ�Ĵ���
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
			// ����߳��Ƿ��жϣ���������׳�InterruptedException
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
			// ����߳��Ƿ��жϣ���������׳�InterruptedException
			if(Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
		}
	}
}