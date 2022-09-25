import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class NgramCreator2 {
	// Map
	static Map<String, ArrayList<String>> ngram_map = new HashMap<String, ArrayList<String>>();

	// Map<String, ArrayList<String>>
	public static final void create_ngram(final String text, final int n, final int doc_id) {
		//
		int text_length = text.length();
		int text_loop_length = text_length - n + 1;
		for (int i = 0; i < text_loop_length; i++) {
			String current_text = text.substring(i, i + n);
			String current_doc_pos = doc_id + "::" + i;
			if (ngram_map.containsKey(current_text)) {
				ArrayList<String> doc_array = ngram_map.get(current_text);
				doc_array.add(current_doc_pos);
				ngram_map.put(current_text, doc_array);
			} else {
				ArrayList<String> doc_array = new ArrayList<String>();
				doc_array.add(current_doc_pos);
				ngram_map.put(current_text, doc_array);
			}
		}
	}
	public static final Map<Integer, ArrayList<Integer>> search_ngram(final String query, final int n) {
		// 完全一致の検索結果を返す
		//
		int query_length = query.length();
		int search_query_length = query_length - n + 1;
		if (query_length < n) {
			System.out.println("Query should be longer than " + n);
			return new HashMap<Integer, ArrayList<Integer>>();
		} else {
			//
			Map<Integer, ArrayList<Integer>> search_result = new HashMap<Integer, ArrayList<Integer>>();
			ArrayList<Integer> search_doc_ids = new ArrayList<Integer>();
			for (int i = 0; i < search_query_length; i++) {
				String partial_search_query = query.substring(i, i+n);
				Map<Integer, ArrayList<Integer>> partial_search_result = map_search_ngram(partial_search_query);
				ArrayList<Integer> partial_search_result_doc_ids = map_doc_pos_to_doc_id(partial_search_result);
				if (i == 0) {
					search_doc_ids = partial_search_result_doc_ids;
					search_result = partial_search_result;
				} else {
					for (int j = 0; j < search_doc_ids.size(); j++) {
						Integer current_doc_id = search_doc_ids.get(j);
						if (!partial_search_result_doc_ids.contains(current_doc_id)) {
							search_doc_ids.remove(j);
							search_result.remove(current_doc_id);
						} else {
							ArrayList<Integer> current_pos_id = search_result.get(current_doc_id);
							ArrayList<Integer> current_pos_ids = partial_search_result.get(current_doc_id);
							current_pos_ids.addAll(current_pos_id);
							search_result.put(current_doc_id, current_pos_ids);
						}
					}
				}
			}
			return search_result;
		}
	}
	public static final Map<Integer, ArrayList<Integer>> map_search_ngram(final String partial_query) {
		if (ngram_map.containsKey(partial_query)) {
			ArrayList<String> searched_array = ngram_map.get(partial_query);
			Map<Integer, ArrayList<Integer>> result_map = new HashMap<Integer, ArrayList<Integer>>();
			for (int i = 0; i < searched_array.size(); i++) {
				String searched_array_element = searched_array.get(i);
				String[] element_splitted = searched_array_element.split("::");
				int doc_id = Integer.parseInt(element_splitted[0]);
				int pos_id = Integer.parseInt(element_splitted[1]);
				if (result_map.containsKey(doc_id)) {
					ArrayList<Integer> pos_ids = result_map.get(doc_id);
					pos_ids.add(pos_id);
					result_map.put(doc_id, pos_ids);
				} else {
					ArrayList<Integer> pos_ids = new ArrayList<Integer>();
					pos_ids.add(pos_id);
					result_map.put(doc_id, pos_ids);
				}
			}
			return result_map;
		} else {
			return new HashMap<Integer, ArrayList<Integer>>();
		}
	}
	public static final ArrayList<Integer> map_doc_pos_to_doc_id(final Map<Integer, ArrayList<Integer>> map) {
		//
		ArrayList<Integer> mapped_result = new ArrayList<Integer>();
		for (Map.Entry<Integer, ArrayList<Integer>> entry: map.entrySet()){
			mapped_result.add(entry.getKey());
		}
		return mapped_result;
	}
	public static final Map<Integer, ArrayList<Integer>> sort_result_pos(final Map<Integer, ArrayList<Integer>> result) {
		Map<Integer, ArrayList<Integer>> result_map = new HashMap<Integer, ArrayList<Integer>>();
		for (Map.Entry<Integer, ArrayList<Integer>> entry: result.entrySet()) {
			ArrayList<Integer> original_array = entry.getValue();
			Collections.sort(original_array);
			result_map.put(entry.getKey(), original_array);
		}
		return result_map;
	}
	public static void main(String[] args) {
		// String text = "Oracleプロファイルの登録を完了するために、電子メール・アドレスのご確認をお願いいたします。ありがとうございます。";
		String[] documents = {"テキストからの N-gram 特徴抽出コンポーネントをパイプラインに追加し、処理するテキストが含まれているデータセットを接続します。",
							  "テキスト列を使用して、抽出するテキストを含む string 型の列を選択します。 結果は詳細であるため、一度に処理できるのは 1 列だけです。",
							  "[Vocabulary mode](ボキャブラリ モード) を [Create](作成) に設定して、新しい N-gram の特徴リストを作成していることを示します。",
							  "[N-Grams size](N-gram のサイズ) を設定して、抽出して格納する N-gram の最大サイズを示します。",
							  "[Weighting function](重み付け関数) は、ドキュメントの特徴ベクトルを作成する方法、およびドキュメントからボキャブラリを抽出する方法を指定します。",
							  "[Minimum word length](単語の最小長) を、N-gram 内の任意の 1 つの単語に使用できる最小文字数に設定します。",
							  "[Maximum word length](単語の最大長) を使用して、N-gram 内の任意の 1 つの単語に使用できる最大文字数を設定します。"
							};
		int n = 3;
		int doc_id = 1;
		for (int i = 0; i < documents.length; i++) {
			// System.out.println(documents[i]);
			create_ngram(documents[i], n, i);
		}
		// Map<String, ArrayList<String>> map = create_ngram(text, n, doc_id);
		// for (Map.Entry<String, ArrayList<String>> entry: ngram_map.entrySet() ) {
		// 	System.out.println(entry.getKey() + " " + entry.getValue());
		// }
		Map<Integer, ArrayList<Integer>> search_result = search_ngram("テキスト", n);
		// System.out.println(search_result);
		Map<Integer, ArrayList<Integer>> search_result_sorted = sort_result_pos(search_result);
		System.out.println(search_result_sorted);
	}
}