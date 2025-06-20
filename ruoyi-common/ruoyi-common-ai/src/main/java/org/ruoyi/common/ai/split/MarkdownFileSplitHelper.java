package org.ruoyi.common.ai.split;

import org.ruoyi.common.ai.standard.SplitStandard;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MarkdownFileSplitHelper implements FileSplitHelper {
	private final int MAX_CHUCK_LENGTH = 1000;

	@Override
	public List<String> split(String content, SplitStandard splitStandard) {
		return splitByHeading(content);
	}

	// 最大 chunk 长度（字符数或可换成 token）

	public List<String> splitByHeading(String markdown) {
		List<String> chunks = new ArrayList<>();

		// 用正则匹配标题（支持 # 到 ###）
		Pattern pattern = Pattern.compile("(?=^#{1,6} .*)", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(markdown);

		List<Integer> indices = new ArrayList<>();
		while (matcher.find()) {
			indices.add(matcher.start());
		}
		indices.add(markdown.length());

		// 按标题范围切割
		for (int i = 0; i < indices.size() - 1; i++) {
			String chunk = markdown.substring(indices.get(i), indices.get(i + 1)).trim();

			// 长 chunk 可进一步按段落或 token 拆分
			if (chunk.length() > MAX_CHUCK_LENGTH) {
				chunks.addAll(splitByParagraph(chunk, MAX_CHUCK_LENGTH));
			} else {
				chunks.add(chunk);
			}
		}
		return chunks;
	}

	// 进一步按段落切分长 chunk（按空行）
	private List<String> splitByParagraph(String text, int maxLen) {
		List<String> result = new ArrayList<>();
		String[] paragraphs = text.split("\\n\\s*\\n");
		StringBuilder buffer = new StringBuilder();

		for (String para : paragraphs) {
			if (buffer.length() + para.length() < maxLen) {
				buffer.append(para).append("\n\n");
			} else {
				result.add(buffer.toString().trim());
				buffer = new StringBuilder(para).append("\n\n");
			}
		}
		if (!buffer.isEmpty()) {
			result.add(buffer.toString().trim());
		}
		return result;
	}

}
