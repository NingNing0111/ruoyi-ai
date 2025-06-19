package org.ruoyi.common.ai.vector.exception;

import org.ruoyi.common.ai.vector.VectorDB;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 13:02
 * @description
 */
public class VectorDBUnSupportException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private final VectorDB vectorDB;

    public VectorDBUnSupportException(VectorDB vectorDB, String message) {
        super(message);
        this.vectorDB = vectorDB;
    }

    public VectorDBUnSupportException(VectorDB vectorDB, String message, Throwable cause) {
        super(message, cause);
        this.vectorDB = vectorDB;
    }

    public VectorDB getVectorDB() {
        return vectorDB;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " | VectorDB info: " + vectorDBSummary();
    }

    private String vectorDBSummary() {
        if (vectorDB == null) {
            return "null";
        }
        return String.format("type=%s, host=%s, port=%s",
                vectorDB.getType(), vectorDB.getHostname(), vectorDB.getPort());
    }
}
