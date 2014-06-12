/**
 * 
 */
package thread.timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Swing��ʱ��Timer����ʾ
 * <p>
 * ��ʵ�����ǹ۲���ģʽ��Ӧ�á�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2010-6-10
 */
public class SwingTimerDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Displayer.createAndShowGUI("Swing Timer Demo", new ClockPanel());
	}
}

class ClockPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel label;

	// ��ʱ��Timer�Ǿ��屻�۲��߽�ɫ
	private Timer timer;

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public ClockPanel() {
		super();
		add(getLabel());
		// ������Timer�����start()����ʱ����ʱ�Ϳ�ʼ��
		getTimer().start();
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel(format.format(new Date()));
			label.setFont(new Font("����", Font.BOLD, 22));
			label.setForeground(Color.RED);
		}
		return label;
	}

	private Timer getTimer() {
		if (timer == null) {
			// 1������һ��ʱ������
			timer = new Timer(1000, null);
			// ��Ӽ�����
			timer.addActionListener(new TimeResetAction());
		}
		return timer;
	}

	/**
	 * ʱ�����������
	 * <p>
	 * ActionListener�ǳ���۲��߽�ɫ��TimeResetAction�Ǿ���۲��߽�ɫ��
	 */
	private class TimeResetAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String time = format.format(new Date());
			getLabel().setText(time);
		}
	}
}