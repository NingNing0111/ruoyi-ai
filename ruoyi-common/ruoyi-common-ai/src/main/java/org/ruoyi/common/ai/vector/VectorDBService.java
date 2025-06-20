package org.ruoyi.common.ai.vector;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import org.apache.commons.lang3.ObjectUtils;
import org.ruoyi.common.ai.vector.common.VectorDBType;
import org.ruoyi.common.ai.vector.common.VectorIndexType;
import org.ruoyi.common.ai.vector.common.VectorMetricType;
import org.ruoyi.common.ai.vector.exception.VectorDBUnSupportException;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.util.StringUtils;

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
				// TODO
				return null;
			}
			case MILVUS -> {
				return buildMilvusVectorStore(vectorDB, embeddingModel);
			}
			default -> {
				throw new VectorDBUnSupportException(vectorDB, "Can't create milvus vector store.");
			}
		}
	}

	default MilvusVectorStore buildMilvusVectorStore(T vectorDB, EmbeddingModel embeddingModel) {
		MilvusVectorStore build = MilvusVectorStore.builder(buildMilvusClient(vectorDB), embeddingModel)
				.collectionName(vectorDB.getCollection())
				.databaseName(vectorDB.getDbName())
				.indexType(VectorIndexType.MilvusIndexType.fromValue(vectorDB.getIndexType()).getIndexType())
				.metricType(VectorMetricType.MilvusMetricType.fromValue(vectorDB.getMetricType()).getMetricType())
				.initializeSchema(vectorDB.initializeSchema())
				.embeddingDimension(vectorDB.getDimension())
				.iDFieldName(vectorDB.getIdFieldName())
				.metadataFieldName(vectorDB.getMetadataFieldName())
				.embeddingFieldName(vectorDB.getEmbeddingFieldName())
				.autoId(true)
				.build();

		return build;
//		MilvusVectorStore.Builder vectorBuilder = MilvusVectorStore.builder(milvusServiceClient, embeddingModel);
//		if (StringUtils.hasText(vectorDB.getCollection())) {
//			vectorBuilder.collectionName(vectorDB.getCollection());
//		}
//		if (StringUtils.hasText(vectorDB.getDbName())) {
//			vectorBuilder.databaseName(vectorDB.getDbName());
//		}
//
//		if (vectorDB.getIndexType() != null) {
//			vectorBuilder.indexType(VectorIndexType.MilvusIndexType.fromValue(vectorDB.getIndexType()).getIndexType());
//		}
//
//		if (vectorDB.getMetricType() != null) {
//			vectorBuilder.metricType(VectorMetricType.MilvusMetricType.fromValue(vectorDB.getMetricType()).getMetricType());
//		}
//
//		if (vectorDB.getDimension() != null) {
//			vectorBuilder.embeddingDimension(vectorDB.getDimension());
//		}
//
//		if (StringUtils.hasText(vectorDB.getIdFieldName())) {
//			vectorBuilder.iDFieldName(vectorDB.getIdFieldName());
//		}
//
//		if (StringUtils.hasText(vectorDB.getMetadataFieldName())) {
//			vectorBuilder.metadataFieldName(vectorDB.getMetadataFieldName());
//		}
//
//		if (StringUtils.hasText(vectorDB.getEmbeddingFieldName())) {
//			vectorBuilder.embeddingFieldName(vectorDB.getEmbeddingFieldName());
//		}
//		if(vectorDB.initializeSchema()) {
//			vectorBuilder.initializeSchema(vectorDB.initializeSchema());
//		}
//		vectorBuilder.autoId(true);
//
//		return vectorBuilder.build();
	}

	default MilvusServiceClient buildMilvusClient (T vectorDB) {
		ConnectParam.Builder connectParamBuilder = ConnectParam.newBuilder();
		String username = vectorDB.getUsername();
		String password = vectorDB.getPassword();
		String hostname = vectorDB.getHostname();
		Integer port = vectorDB.getPort();
		List<String> missingFields = new ArrayList<>();
		if (hostname == null) {
			missingFields.add("hostname");
		}
		if (port == null) {
			missingFields.add("port");
		}
		if (!missingFields.isEmpty()) {
			String name = VectorDBType.from(vectorDB).getName();
			throw new IllegalArgumentException(
					String.format("%s config missing required field(s): %s", name, String.join(", ", missingFields)));
		}
		if(ObjectUtils.isNotEmpty(username) && ObjectUtils.isNotEmpty(password)) {
			connectParamBuilder.withAuthorization(username,password);
		}
		return new MilvusServiceClient(connectParamBuilder.build());

	}

}
