package org.ruoyi.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ageer
 */
@Data
public class KnowledgeInfoUploadBo {

    private String kid;

    private MultipartFile file;
}
