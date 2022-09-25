// https://gist.github.com/shun91/2c64d0c81355b920ca56 から拝借しました。

import java.util.HashMap;
import java.util.Map;

public class NgramCreator {
	public static final Map<String, Integer> createNgram(final String text, final int n) {
		final String[] words = text.split(" ", 0);
		final int numberOfNgram = words.length - n + 1;
		Map<String, Integer> ngramMap = new HashMap<String, Integer>();
		StringBuilder ngramSb = new StringBuilder();

		for (int i = 0; i < numberOfNgram; i++) {
			for (int j = i; j < i + n; j++) {
				ngramSb.append(words[j]).append(" ");
			}
			System.out.println(ngramSb);
			ngramSb.deleteCharAt(ngramSb.length() - 1);
			String ngramStr = ngramSb.toString();
			ngramSb.delete(0, ngramSb.length());
			//
			if (ngramMap.containsKey(ngramStr)) {
				ngramMap.put(ngramStr, ngramMap.get(ngramStr) + 1);
			} else {
				ngramMap.put(ngramStr, 1);
			}
		}
		return ngramMap;
	}

	//
	public static void main(String[] args) {
		String text = "In the fields of computational linguistics and probability, an n-gram is a contiguous sequence of n items from a given sequence of text or speech. The items can be phonemes, syllables, letters, words or base pairs according to the application. The n-grams typically are collected from a text or speech corpus.";
		int n = 3;
		Map<String, Integer> map = createNgram(text, n);
		for (Map.Entry<String, Integer> entry: map.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}
}