package wuziqi;

public class Morpion {
	public static int now = 0;
	public static int size = 15;// ���̿��
	public static int gap = 30;// ���Ӵ�С
	public static int IQ = 4;// AI���������
	public static int AI = 2;// ����
	public static int HUMAN = 1;// ����
	public static Chessboard board = new Chessboard(size);// �������̿��
	public static Draw draw;
	public static AI ai;
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				draw = new Draw(1);
			}
		});

		while (draw.begin == 0) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		ai = new AI(IQ, board);// ��ʼ��AI
		// ���AI���֣����������м�
		if (AI == 1) {
			Coord newCoord = new Coord(size / 2, size / 2, AI);
			board.setCoord(newCoord);
			board.saveAILast(newCoord);
			draw.checker.repaint();
		}

		while (true) {
			now = 1;
			Draw.now = Morpion.HUMAN;
			// ��������
			while (now == 1) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			// �ж��������
			if (board.check())
				break;
			// AI����

			ai.input();
			draw.checker.repaint();
			// �ж��������
			if (board.check())
				break;
		}
		draw.finish();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		Draw.result=-1;
		draw.checker.repaint();
	}
}
