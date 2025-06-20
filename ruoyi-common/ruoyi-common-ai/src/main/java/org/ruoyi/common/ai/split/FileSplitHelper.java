package org.ruoyi.common.ai.split;

import org.ruoyi.common.ai.standard.SplitStandard;

import java.util.List;

/**
 * 文本切分
 */
public interface FileSplitHelper {

	/**
	 * 文本切分
	 * @param content 文本内容
	 * @param kid 知识库id
	 * @return 切分后的文本列表
	 */
	List<String> split(String content, SplitStandard splitStandard);

	default List<String> split(String content) {
		return this.split(content,null);
	}

}
