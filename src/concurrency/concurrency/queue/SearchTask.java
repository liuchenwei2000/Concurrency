/**
 * 
 */
package concurrency.queue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * �����߳�
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-12
 */
public class SearchTask implements Runnable {

	private String keyword;
	private BlockingQueue<File> queue;
	
	public SearchTask(String keyword, BlockingQueue<File> queue) {
		this.keyword = keyword;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			boolean complete = false;// �Ƿ������ļ����������
			// ���ϵĴ������������ó��ļ�����������ֱ��ȫ���ļ��������ꡣ
			while (!complete) {
				File file = queue.take();
				if (FileListTask.END == file) {// ͨ���ض���ʶ�ж��Ƿ�ȫ���������
					queue.put(file);// ����Ҫ�ѱ�ʶ�ٷŽ����У��Ա������߳�֪��
					complete = true;
				} else {
					search(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void search(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp = null;
		int line = 0;
		while((temp = br.readLine())!=null){
			line++;
			if(temp.contains(keyword)){
				System.out.print("file path=" + file.getPath());
				System.out.print("  line number=" + line);
				System.out.println("  content=" + temp);
			}
		};
		br.close();
	}
}