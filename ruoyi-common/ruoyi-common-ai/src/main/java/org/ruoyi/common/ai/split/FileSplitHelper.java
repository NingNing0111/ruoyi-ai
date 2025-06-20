package org.ruoyi.common.ai.split;

import org.ruoyi.common.ai.standard.SplitStandard;
import org.springframework.ai.document.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Peng WenDeng
 * @email pengwendeng@huice.com
 * @time 2025-06-20 10-15
 * @description 文本切分
 */
public interface FileSplitHelper {

	/**
	 * 文本切分
	 * @param content 文本内容
	 * @param splitStandard 分词标准
	 * @return 切分后的文本列表
	 */
	List<Document> split(String content, SplitStandard splitStandard);

	default List<Document> split(String content) {
		return this.split(content,null);
	}

	default Map<String, Object> getMetaData(SplitStandard splitStandard) {
		if(splitStandard == null) {
			return new HashMap<>();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("kId", splitStandard.getKId().toString());
		map.put("docId", splitStandard.getDocId().toString());
		map.put("score", splitStandard.getScore());
		map.put("creator", splitStandard.getCreateBy().toString());
		return map;
	}
}
