package org.ruoyi.common.ai.split;

import org.ruoyi.common.ai.standard.SplitStandard;

import java.util.List;

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
	List<String> split(String content, SplitStandard splitStandard);

	default List<String> split(String content) {
		return this.split(content,null);
	}

}
