package org.ruoyi.common.ai.vector.exception;

import org.ruoyi.common.ai.vector.VectorDB;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 14:42
 * @description
 */
public class VectorIndexTypeUnSupportException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VectorIndexTypeUnSupportException(String message) {
		super(message);
	}

}
