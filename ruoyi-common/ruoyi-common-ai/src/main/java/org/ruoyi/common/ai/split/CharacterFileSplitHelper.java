package org.ruoyi.common.ai.split;

import io.micrometer.common.util.StringUtils;
import org.ruoyi.common.ai.standard.SplitStandard;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CharacterFileSplitHelper implements FileSplitHelper {

	@Override
	public List<String> split(String content, SplitStandard splitStandard) {
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
		return chunkList;
	}

}
