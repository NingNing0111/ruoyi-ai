package org.ruoyi.common.ai.vector.common;

import io.milvus.param.MetricType;
import org.ruoyi.common.ai.vector.exception.VectorMetricTypeUnSupportException;

import java.util.Objects;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 14:33
 * @description
 */
public class VectorMetricType {

	public enum MilvusMetricType {

		None(0, MetricType.None),

		// Only for float vectors
		L2(1, MetricType.L2), IP(2, MetricType.IP), COSINE(3, MetricType.COSINE),

		// Only for binary vectors
		HAMMING(4, MetricType.HAMMING), JACCARD(5, MetricType.JACCARD);

		private final Integer value;

		private final MetricType metricType;

		MilvusMetricType(Integer value, MetricType metricType) {
			this.value = value;
			this.metricType = metricType;
		}

		public Integer getValue() {
			return value;
		}

		public MetricType getMetricType() {
			return metricType;
		}

		public static MilvusMetricType fromValue(Integer value) {
			MilvusMetricType[] values = values();
			for (MilvusMetricType metricType : values) {
				if (Objects.equals(metricType.getValue(), value)) {
					return metricType;
				}
			}
			throw new VectorMetricTypeUnSupportException(
					String.format("No VectorMetricType found for value=%d and dbType=milvus", value));
		}

	}

}
