package wuziqi;

import java.util.ArrayList;
import java.util.Collections;

//棋盘类
public class Chessboard {
	public int map[][];
	public int size;
	public static int[] aiLastStep;
	public static int[] humanLastStep;
	public int blackScore[][];// black==1
	public int whiteScore[][];// white==2
	static {
		aiLastStep = new int[2];
		humanLastStep = new int[2];
		aiLastStep[0] = -1;
		aiLastStep[1] = -1;
		humanLastStep[0] = -1;
		humanLastStep[1] = -1;
	}

	public Chessboard(int s) {
		size = s;
		map = new int[s][s];
		blackScore = new int[4][2 * s - 1];
		whiteScore = new int[4][2 * s - 1];

	}

	public void saveAILast(Coord xy) {
		aiLastStep[0] = xy.x;
		aiLastStep[1] = xy.y;
		Draw.regretFlag = 0;
	}

	public void saveHumanLast(Coord xy) {
		humanLastStep[0] = xy.x;
		humanLastStep[1] = xy.y;
		Draw.regretFlag = 0;
	}

	public void regret() {
		if (humanLastStep[0] != -1) {

			map[aiLastStep[0]][aiLastStep[1]] = 0;
			map[humanLastStep[0]][humanLastStep[1]] = 0;
			aiLastStep[0] = -1;
			aiLastStep[1] = -1;
			humanLastStep[0] = -1;
			humanLastStep[1] = -1;
		}
	}

	public void setCoord(Coord coord) {
		map[coord.x][coord.y] = coord.player;
		updateScore(coord);
	}

	public void removeCoord(Coord coord) {
		map[coord.x][coord.y] = 0;
		updateScore(coord);
	}

	// 检查游戏是否结束
	public boolean check() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (map[i][j] != 0) {
					// 横
					if (j + 4 < size) {
						if (map[i][j] == map[i][j + 1]
								&& map[i][j + 1] == map[i][j + 2]
								&& map[i][j + 2] == map[i][j + 3]
								&& map[i][j + 3] == map[i][j + 4]) {
							Draw.result=map[i][j];
							System.out.println("游戏结束，胜者是" + map[i][j]);
							return true;
						}
					}
					// 竖
					if (i + 4 < size) {
						if (map[i][j] == map[i + 1][j]
								&& map[i + 1][j] == map[i + 2][j]
								&& map[i + 2][j] == map[i + 3][j]
								&& map[i + 3][j] == map[i + 4][j]) {
							Draw.result=map[i][j];
							System.out.println("游戏结束，胜者是" + map[i][j]);
							return true;
						}
					}
					// 斜
					if (j - 4 > -1 && i + 4 < size) {
						if (map[i][j] == map[i + 1][j - 1]
								&& map[i + 1][j - 1] == map[i + 2][j - 2]
								&& map[i + 2][j - 2] == map[i + 3][j - 3]
								&& map[i + 3][j - 3] == map[i + 4][j - 4]) {
							Draw.result=map[i][j];
							System.out.println("游戏结束，胜者是" + map[i][j]);
							return true;
						}
					}
					if (j + 4 < size && i + 4 < size) {
						if (map[i][j] == map[i + 1][j + 1]
								&& map[i + 1][j + 1] == map[i + 2][j + 2]
								&& map[i + 2][j + 2] == map[i + 3][j + 3]
								&& map[i + 3][j + 3] == map[i + 4][j + 4]) {
							Draw.result=map[i][j];
							System.out.println("游戏结束，胜者是" + map[i][j]);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	// 返回所有存在棋子的坐标点
	public ArrayList<Coord> getAllCoords() {
		ArrayList<Coord> coords = new ArrayList<Coord>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (map[i][j] != 0) {
					Coord coord = new Coord(i, j, map[i][j]);
					coords.add(coord);
				}
			}
		}
		return coords;
	}

	// 复制整个棋盘
	public Chessboard copy() {
		Chessboard newChessboard = new Chessboard(size);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				newChessboard.map[i][j] = map[i][j];
			}
		}
		return newChessboard;
	}

	// 打印棋盘
	public void print() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
		// System.out.println();
	}

	public int[][] getMap() {
		int[][] newMap = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				newMap[i][j] = map[i][j];
			}
		}
		return newMap;
	}

	public ArrayList<Coord> getNearbyChess(Coord c, int[][] m) {
		ArrayList<Coord> coords = new ArrayList<Coord>();
		for (int i = c.x - 1; i <= c.x + 1; i++) {
			for (int j = c.y - 1; j <= c.y + 1; j++) {
				if (i < size && j < size && i > -1 && j > -1 && map[i][j] == 0
						&& m[i][j] != -1) {
					coords.add(new Coord(i, j, 0));
				}
			}
		}
		return coords;
	}

	public int getScore(Coord c) // 单点得分
	{
		setCoord(c);
		int blackSumScore = blackScore[0][c.x] + blackScore[1][c.y]
				+ blackScore[2][c.x + c.y]
				+ blackScore[3][c.x - c.y + size - 1];
		int whiteSumScore = whiteScore[0][c.x] + whiteScore[1][c.y]
				+ whiteScore[2][c.x + c.y]
				+ whiteScore[3][c.x - c.y + size - 1];
		removeCoord(c);
		if (Morpion.AI == 1)
			return blackSumScore - whiteSumScore;
		else
			return whiteSumScore - blackSumScore;
	}

	public int getFullScore(Coord c) // 全盘得分
	{
		setCoord(c);
		int blackSum = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < size; j++) {
				blackSum += blackScore[i][j];
			}
		}
		for (int i = 2; i < 4; i++) {
			for (int j = 0; j < 2 * size - 1; j++) {
				blackSum += blackScore[i][j];
			}
		}
		int whiteSum = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < size; j++) {
				whiteSum += whiteScore[i][j];
			}
		}
		for (int i = 2; i < 4; i++) {
			for (int j = 0; j < 2 * size - 1; j++) {
				whiteSum += whiteScore[i][j];
			}
		}
		removeCoord(c);
		if (Morpion.AI == 1)
			return blackSum - whiteSum;
		else
			return whiteSum - blackSum;
	}

	public boolean isPassed(Coord c) {
		if (c.x > -1 && c.x < size && c.y > -1 && c.y < size
				&& map[c.x][c.y] == 0)
			return true;
		else
			return false;
	}

	public void updateScore(Coord c) {
		ArrayList<Integer> row = new ArrayList<Integer>();
		for (int i = 0; i < size; i++)
			row.add(map[c.x][i]);
		blackScore[0][c.x] = Score.getScore(row, 1);
		whiteScore[0][c.x] = Score.getScore(row, 2);
		row.clear();
		for (int i = 0; i < size; i++)
			row.add(map[i][c.y]);
		blackScore[1][c.y] = Score.getScore(row, 1);
		whiteScore[1][c.y] = Score.getScore(row, 2);
		row.clear();
		for (int i = 0;; i++) {
			if (c.x - i < 0 || c.y + i == size)
				break;
			row.add(map[c.x - i][c.y + i]);
		}
		Collections.reverse(row);
		for (int i = 1;; i++) {
			if (c.x + i == size || c.y - i < 0)
				break;
			row.add(map[c.x + i][c.y - i]);
		}
		blackScore[2][c.y + c.x] = Score.getScore(row, 1);
		whiteScore[2][c.y + c.x] = Score.getScore(row, 2);
		row.clear();
		for (int i = 0;; i++) {
			if (c.x - i < 0 || c.y - i < 0)
				break;
			row.add(map[c.x - i][c.y - i]);
		}
		Collections.reverse(row);
		for (int i = 1;; i++) {
			if (c.x + i == size || c.y + i == size)
				break;
			row.add(map[c.x + i][c.y + i]);
		}
		blackScore[3][c.x - c.y + size - 1] = Score.getScore(row, 1);
		whiteScore[3][c.x - c.y + size - 1] = Score.getScore(row, 2);
	}
}
