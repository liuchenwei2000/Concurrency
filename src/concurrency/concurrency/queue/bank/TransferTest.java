/**
 * 
 */
package concurrency.queue.bank;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 银行转账测试器
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public class TransferTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int accNumber = 10;// 银行总账号数
		
		// 使用 阻塞队列 控制同步
		BlockingQueue<TransferCommand> commands = new LinkedBlockingQueue<TransferCommand>(20);

		for (int i = 0; i < accNumber; i++) {
			new Thread(new TransferCommandTask(i, accNumber, commands)).start();
			new Thread(new TransferTask(accNumber, commands)).start();
		}
	}
}