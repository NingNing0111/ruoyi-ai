package org.ruoyi.vector.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.vector.domain.VectorDbInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.ruoyi.vector.domain.vo.VectorDBInfoVo;

/**
* @author admin
* @description 针对表【vector_db_info(向量数据库信息表)】的数据库操作Mapper
* @createDate 2025-06-18 13:56:33
* @Entity org.ruoyi.vector.domain.VectorDbInfo
*/
@Mapper
public interface VectorDbInfoMapper extends BaseMapperPlus<VectorDbInfo, VectorDBInfoVo> {

}




