package wuziqi;

public class Morpion {
	public static int now = 0;
	public static int size = 15;// 棋盘宽度
	public static int gap = 30;// 棋子大小
	public static int IQ = 4;// AI决策树深度
	public static int AI = 2;// 白子
	public static int HUMAN = 1;// 黑子
	public static Chessboard board = new Chessboard(size);// 设置棋盘宽度
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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		ai = new AI(IQ, board);// 初始化AI
		// 如果AI先手，则下在正中间
		if (AI == 1) {
			Coord newCoord = new Coord(size / 2, size / 2, AI);
			board.setCoord(newCoord);
			board.saveAILast(newCoord);
			draw.checker.repaint();
		}

		while (true) {
			now = 1;
			Draw.now = Morpion.HUMAN;
			// 人类输入
			while (now == 1) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			// 判定局面情况
			if (board.check())
				break;
			// AI输入

			ai.input();
			draw.checker.repaint();
			// 判定局面情况
			if (board.check())
				break;
		}
		draw.finish();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Draw.result=-1;
		draw.checker.repaint();
	}
}
