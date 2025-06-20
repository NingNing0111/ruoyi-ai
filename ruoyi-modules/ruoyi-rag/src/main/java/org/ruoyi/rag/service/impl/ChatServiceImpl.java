package org.ruoyi.rag.service.impl;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.ai.vector.VectorDBService;
import org.ruoyi.common.core.exception.base.BaseException;
import org.ruoyi.domain.KnowledgeInfo;
import org.ruoyi.mapper.KnowledgeInfoMapper;
import org.ruoyi.rag.common.PromptPathCommon;
import org.ruoyi.rag.domain.ao.ChunkItem;
import org.ruoyi.rag.domain.ao.DocumentAo;
import org.ruoyi.rag.domain.request.ChatRequest;
import org.ruoyi.rag.service.IChatService;
import org.ruoyi.rag.service.IPromptResourceService;
import org.ruoyi.vector.domain.VectorDbInfo;
import org.ruoyi.vector.mapper.VectorDbInfoMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-19 11:29
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {

    private final VectorDBService<VectorDbInfo> vectorDBService;
    private final EmbeddingModel embeddingModel;
    private final ChatModel chatModel;
    private final KnowledgeInfoMapper knowledgeInfoMapper;
    private final VectorDbInfoMapper vectorDbInfoMapper;
    private final IPromptResourceService promptResourceService;


    private final static int DEFAULT_SEARCH_COUNT = 10;

    @Override
    public ChatResponse chat(ChatRequest chatRequest) {
        List<Long> kIds = chatRequest.getKIds();
        List<DocumentAo> similarityDocumentAo = new ArrayList<>();
        int chunkSize = DEFAULT_SEARCH_COUNT / kIds.size() + 1;
        for(Long kid: kIds) {
            KnowledgeInfo knowledgeInfo = knowledgeInfoMapper.selectById(kid);
            if(Objects.isNull(knowledgeInfo)) {
                throw new BaseException("知识库不存在");
            }
            Integer vId = knowledgeInfo.getVId();
            VectorDbInfo vectorDbInfo = vectorDbInfoMapper.selectById(vId);
            if(Objects.isNull(vectorDbInfo)) {
                throw new BaseException("向量数据库接入信息不存在");
            }
            VectorStore vectorStore = vectorDBService.getVectorStore(vectorDbInfo, embeddingModel);
            List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                    .query(chatRequest.getPrompt())
//                    .filterExpression(buildFilterExpression(chatRequest))
                    .topK(chunkSize)
                    .build());
            if(documents != null && !documents.isEmpty()) {
                DocumentAo documentAo = buildDocumentAo(knowledgeInfo, documents);
                similarityDocumentAo.add(documentAo);
            }
        }
        Message systemMessage = promptResourceService.createMessageFromClassResource(PromptPathCommon.SALE_LEAN_ASSISTANT, OpenAiApi.ChatCompletionMessage.Role.SYSTEM);
        Message userMessage = promptResourceService.createMessageFromClassResource(PromptPathCommon.SALE_LEAN_PROMPT, OpenAiApi.ChatCompletionMessage.Role.USER, Map.of("question", chatRequest.getPrompt(), "document_chunk_list", JSON.toJSON(similarityDocumentAo)));
        ChatClient chatClient = ChatClient.builder(chatModel).defaultSystem(systemMessage.getText()).build();
        return chatClient.prompt().user(userMessage.getText()).advisors().call().chatResponse();

    }


    private Filter.Expression buildFilterExpression(ChatRequest chatRequest) {
        FilterExpressionBuilder filterExpressionBuilder = new FilterExpressionBuilder();
        return filterExpressionBuilder.in("kId", chatRequest.getKIds()).build();
    }

    private DocumentAo buildDocumentAo(KnowledgeInfo knowledgeInfo,List<Document> documents) {
        List<ChunkItem> chunks = documents.stream().map(item -> {
            ChunkItem chunkItem = new ChunkItem();
            chunkItem.setContent(chunkItem.getContent());
            chunkItem.setScore(chunkItem.getScore());
            return chunkItem;
        }).toList();
        DocumentAo documentAo = new DocumentAo();
        documentAo.setChunks(chunks);
        documentAo.setKnowledgeBaseName(knowledgeInfo.getKname());
        documentAo.setKnowledgeBaseDescription(knowledgeInfo.getDescription());
        return documentAo;
    }


//    @PostConstruct
//    public void init() {
//        ChatRequest chatRequest = new ChatRequest();
//        String prompt = "旺店通如何解决 商家代拆盲盒的业务场景的？";
//        List<Long> kIds = List.of(1935679654813454338L);
//        chatRequest.setKIds(kIds);
//        chatRequest.setPrompt(prompt);
//        List<DocumentAo> similarityDocumentAo = new ArrayList<>();
//        int chunkSize = DEFAULT_SEARCH_COUNT / kIds.size() + 1;
//        for(Long kid: kIds) {
//            KnowledgeInfo knowledgeInfo = knowledgeInfoMapper.selectById(kid);
//            if(Objects.isNull(knowledgeInfo)) {
//                throw new BaseException("知识库不存在");
//            }
//            Integer vId = knowledgeInfo.getVId();
//            VectorDbInfo vectorDbInfo = vectorDbInfoMapper.selectById(vId);
//            if(Objects.isNull(vectorDbInfo)) {
//                throw new BaseException("向量数据库接入信息不存在");
//            }
//            VectorStore vectorStore = vectorDBService.getVectorStore(vectorDbInfo, embeddingModel);
//            List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
//                    .query(chatRequest.getPrompt())
//                    .filterExpression(buildFilterExpression(chatRequest))
//                    .topK(chunkSize)
//                    .build());
//            if(documents != null && !documents.isEmpty()) {
//                DocumentAo documentAo = buildDocumentAo(knowledgeInfo, documents);
//                similarityDocumentAo.add(documentAo);
//            }
//        }
//        Message systemMessage = promptResourceService.createMessageFromClassResource(PromptPathCommon.SALE_LEAN_ASSISTANT, OpenAiApi.ChatCompletionMessage.Role.SYSTEM);
//        Message userMessage = promptResourceService.createMessageFromClassResource(PromptPathCommon.SALE_LEAN_PROMPT, OpenAiApi.ChatCompletionMessage.Role.USER, Map.of("question", chatRequest.getPrompt(), "document_chunk_list", JSON.toJSON(similarityDocumentAo)));
//        String text = chatClient.prompt().system(systemMessage.getText()).user(userMessage.getText()).call().chatResponse().getResult().getOutput().getText();
//        System.out.println(text);
//    }

}
