/**
 * 
 */
package concurrency.collections.bank;

import java.util.concurrent.BlockingQueue;

import concurrency.bank.Account;
import concurrency.bank.Bank;
import concurrency.bank.UnsynBank;

/**
 * ������ת������
 * <p>
 * ��ʵ������һ�������߽�ɫ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��8��3��
 */
public class TransferTask implements Runnable {

	private BlockingQueue<TransferCommand> commands;
	
	private Bank bank;
	
	public TransferTask(int accNumber, BlockingQueue<TransferCommand> commands) {
		this.bank = new UnsynBank(accNumber, 100);
		this.commands = commands;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				// ����ط���Ȼ�����˲�������ȡ�� TransferCommand ֮���Bank�Ĳ�����û�в���������
				// ����˵Ӧ�û������⣬����ʵû�С�
				TransferCommand command = commands.take();
				Account[] accounts = bank.getAccounts();
				bank.transfer(accounts[command.getFrom()], accounts[command.getTo()], command.getAmount());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
