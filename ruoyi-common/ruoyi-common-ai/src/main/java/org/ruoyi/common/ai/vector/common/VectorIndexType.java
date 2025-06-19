package org.ruoyi.common.ai.vector.common;

import io.milvus.param.IndexType;
import org.ruoyi.common.ai.vector.exception.VectorIndexTypeUnSupportException;

import java.util.Objects;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 14:32
 * @description
 */
public class VectorIndexType {


    public enum MilvusIndexType {
        None(0, IndexType.None),

        // Only supported for float vectors
        FLAT(1, IndexType.FLAT),
        IVF_FLAT(2, IndexType.IVF_FLAT),
        IVF_SQ8(3, IndexType.IVF_SQ8),
        IVF_PQ(4, IndexType.IVF_PQ);
//
//        HNSW(5, ),
//        HNSW_SQ(6, ),
//        HNSW_PQ(7, ),
//        HNSW_PRQ(8, ),
//        DISKANN(10, ),
//        AUTOINDEX(11, ),
//        SCANN(12, ),
//
//        // GPU indexes only for float vectors
//        GPU_IVF_FLAT(50, ),
//        GPU_IVF_PQ(51, ),
//        GPU_BRUTE_FORCE(52, ),
//        GPU_CAGRA(53, ),
//
//        // Only supported for binary vectors
//        BIN_FLAT(80, ),
//        BIN_IVF_FLAT(81, ),
//
//        // Only for varchar type field
//        TRIE(100, ),
//
//        // Only for scalar type field
//        STL_SORT(200, ),
//        INVERTED(201, ),
//        BITMAP(202, ),
//
//        // Only for sparse vectors
//        SPARSE_INVERTED_INDEX(300, ),
//        SPARSE_WAND(301, );

        private final Integer value;
        private final IndexType indexType;

        MilvusIndexType(Integer value, IndexType indexType) {
            this.value = value;
            this.indexType = indexType;
        }

        public Integer getValue() {
            return value;
        }

        public IndexType getIndexType() {
            return indexType;
        }

        public static MilvusIndexType fromValue(Integer value) {
            MilvusIndexType[] values = values();
            for (MilvusIndexType indexType : values) {
                if (Objects.equals(indexType.getValue(), value)) {
                    return indexType;
                }
            }
            throw new VectorIndexTypeUnSupportException(String.format("No VectorIndexType found for value=%d and dbType=milvus", value));
        }


    }

}
