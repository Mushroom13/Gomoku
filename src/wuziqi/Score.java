package wuziqi;

import java.util.ArrayList;
import java.util.HashMap;

public class Score {
	static String[] types = { "11111",// 成五
			"011110",// 活四
			"011112",// 冲四
			"0101110", "0110110", "011100",// 活三
			"010110", "001112",// 眠三
			"010112", "011012", "10011", "10101", "2011102", "000110",// 活二
			"001100", "010100", "010010", "000112",// 眠二
			"001012", "010012", "10001", "2010102", "2011002" };
	static HashMap<String, Integer> typeToScore = new HashMap<String, Integer>();
	static HashMap<String, Integer> scoreCache = new HashMap<String, Integer>();
	static {
		typeToScore.put("11111", 1000000);// 成五
		typeToScore.put("011110", 100000);// 活四
		typeToScore.put("011112", 1000);// 冲四
		typeToScore.put("0101110", 1000);
		typeToScore.put("0110110", 1000);
		typeToScore.put("011100", 1000);// 活三
		typeToScore.put("010110", 1000);
		typeToScore.put("001112", 100);// 眠三
		typeToScore.put("010112", 100);
		typeToScore.put("011012", 100);
		typeToScore.put("10011", 100);
		typeToScore.put("10101", 100);
		typeToScore.put("2011102", 100);
		typeToScore.put("000110", 100);// 活二
		typeToScore.put("001100", 100);
		typeToScore.put("010100", 100);
		typeToScore.put("010010", 100);
		typeToScore.put("000112", 10);// 眠二
		typeToScore.put("001012", 10);
		typeToScore.put("010012", 10);
		typeToScore.put("10001", 10);
		typeToScore.put("2010102", 10);
		typeToScore.put("2011002", 10);
	}

	static int getScore(ArrayList<Integer> row, int player) {
		String rowString = "";
		for (int n : row) {
			if (player == 2) {
				if (n == 1)
					n = 2;
				else if (n == 2)
					n = 1;
			}
			rowString += n;
		}
		if (scoreCache.containsKey(rowString))
			return scoreCache.get(rowString);
		for (String s : types) {
			if (rowString.indexOf(s) != -1) {
				// System.out.println(rowString+"得分："+typeToScore.get(s));
				scoreCache.put(rowString, typeToScore.get(s));
				scoreCache.put(reverse(rowString), typeToScore.get(s));
				return typeToScore.get(s);
			}
			String s2 = reverse(s);
			if (!s2.equals(s)) {
				if (rowString.indexOf(s2) != -1) {
					// System.out.println(rowString+"得分："+typeToScore.get(s));
					scoreCache.put(rowString, typeToScore.get(s));
					scoreCache.put(reverse(rowString), typeToScore.get(s));
					return typeToScore.get(s);
				}
			}
		}
		scoreCache.put(rowString, 0);
		return 0;
	}

	private static String reverse(String str) {
		return new StringBuilder(str).reverse().toString();
	}
}
