package org.ruoyi.common.ai.split;

import io.micrometer.common.util.StringUtils;
import org.ruoyi.common.ai.standard.SplitStandard;
import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Lazy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Peng WenDeng
 * @email pengwendeng@huice.com
 * @time 2025-06-20 10-15
 * @description
 */

public class CharacterFileSplitHelper implements FileSplitHelper {

	@Override
	public List<Document> split(String content, SplitStandard splitStandard) {
		content = content.replaceAll("\\r?\\n", "");
		String knowledgeSeparator = splitStandard.getSeparator();
		int textBlockSize = splitStandard.getTextBlockSize();
		int overlapChar = splitStandard.getOverlapChar();
		List<String> chunkList = new ArrayList<>();
		if (content.contains(knowledgeSeparator) && StringUtils.isNotBlank(knowledgeSeparator)) {
			// 按自定义分隔符切分
			String[] chunks = content.split(knowledgeSeparator);
			chunkList.addAll(Arrays.asList(chunks));
		}
		else {
			int indexMin = 0;
			int len = content.length();
			int i = 0;
			int right = 0;
			while (true) {
				if (len > right) {
					int begin = i * textBlockSize - overlapChar;
					if (begin < indexMin) {
						begin = indexMin;
					}
					int end = textBlockSize * (i + 1) + overlapChar;
					if (end > len) {
						end = len;
					}
					String chunk = content.substring(begin, end);
					chunkList.add(chunk);
					i++;
					right = right + textBlockSize;
				}
				else {
					break;
				}
			}
		}
		Map<String, Object> metaData = getMetaData(splitStandard);
		List<Document> documentList = chunkList.stream()
				.map(chunk -> new Document(chunk, metaData))
				.collect(Collectors.toList());
		return documentList;
	}

}
