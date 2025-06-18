package org.ruoyi.common.ai.vector;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import org.ruoyi.common.ai.vector.common.VectorDBType;
import org.ruoyi.common.ai.vector.common.VectorIndexType;
import org.ruoyi.common.ai.vector.common.VectorMetricType;
import org.ruoyi.common.ai.vector.exception.VectorDBUnSupportException;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 14:07
 * @description
 */
public interface VectorDBService<T extends VectorDB> {

    /**
     * 返回vector store操作接口
     * @param vectorDB 数据库
     * @return vectorStore
     */
    default VectorStore getVectorStore(T vectorDB, EmbeddingModel embeddingModel) {
        VectorDBType type = VectorDBType.from(vectorDB);

        switch (type) {
            case PGVECTOR -> {
                return null;
            }
            case MILVUS -> {
                return MilvusVectorStore.builder(buildMilvusClient(vectorDB), embeddingModel)
                        .collectionName(vectorDB.getCollection())
                        .databaseName(vectorDB.getDbName())
                        .indexType(VectorIndexType.MilvusIndexType.fromValue(vectorDB.getIndexType()).getIndexType())
                        .metricType(VectorMetricType.MilvusMetricType.fromValue(vectorDB.getMetricType()).getMetricType())
                        .initializeSchema(vectorDB.initializeSchema())
                        .embeddingDimension(vectorDB.getDimension())
                        .iDFieldName(vectorDB.getIdFieldName())
                        .metadataFieldName(vectorDB.getMetadataFieldName())
                        .embeddingFieldName(vectorDB.getEmbeddingFieldName())
                        .build();
            }
            default -> {
                throw new VectorDBUnSupportException(vectorDB, "Can't create milvus vector store.");
            }
        }
    }


    default MilvusServiceClient buildMilvusClient(T vectorDB) {
        String username = vectorDB.getUsername();
        String password = vectorDB.getPassword();
        String hostname = vectorDB.getHostname();
        Integer port = vectorDB.getPort();
        List<String> missingFields = new ArrayList<>();
        if (username == null) {
            missingFields.add("username");
        }
        if (password == null) {
            missingFields.add("password");
        }
        if (hostname == null) {
            missingFields.add("hostname");
        }
        if (port == null) {
            missingFields.add("port");
        }
        if (!missingFields.isEmpty()) {
            String name = VectorDBType.from(vectorDB).getName();
            throw new IllegalArgumentException(String.format(
                    "%s config missing required field(s): %s",
                    name, String.join(", ", missingFields)
            ));
        }
        return new MilvusServiceClient(ConnectParam.newBuilder()
                .withAuthorization(vectorDB.getUsername(), vectorDB.getPassword())
                .withHost(vectorDB.getHostname())
                .withPort(vectorDB.getPort())
                .build()
        );

    }


}
