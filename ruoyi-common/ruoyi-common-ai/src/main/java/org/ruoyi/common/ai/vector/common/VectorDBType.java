package org.ruoyi.common.ai.vector.common;

import org.ruoyi.common.ai.vector.VectorDB;

import java.util.Objects;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 12:43
 * @description
 */
public enum VectorDBType {

	MILVUS(1, "milvus"), PGVECTOR(2, "pgvector");

	private Integer value;

	private String name;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	VectorDBType(Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	public static VectorDBType fromValue(Integer value) {
		VectorDBType[] values = values();
		for (VectorDBType dbType : values) {
			if (Objects.equals(dbType.getValue(), value)) {
				return dbType;
			}
		}
		throw new IllegalArgumentException("No VectorDBType with value " + value);
	}

	public static VectorDBType from(VectorDB vectorDB) {
		Integer type = vectorDB.getType();
		return VectorDBType.fromValue(type);
	}

}
