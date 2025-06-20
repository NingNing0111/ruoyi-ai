package org.ruoyi.common.ai.split;

import org.ruoyi.common.ai.standard.SplitStandard;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Peng WenDeng
 * @email pengwendeng@huice.com
 * @time 2025-06-20 10-15
 * @description
 */
public class TokenFileSplitHelper implements FileSplitHelper {

	@Override
	public List<Document> split(String content, SplitStandard splitStandard) {
		TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
		CharacterFileSplitHelper characterFileSplitHelper = new CharacterFileSplitHelper();
		List<Document> documents = characterFileSplitHelper.split(content, splitStandard);
		return tokenTextSplitter.apply(documents);
	}

}
