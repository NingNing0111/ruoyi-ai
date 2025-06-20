package org.ruoyi.vector.service;

import org.ruoyi.common.core.domain.vo.Label;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.vector.domain.VectorDbInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.ruoyi.vector.domain.bo.VectorDBInfoBo;
import org.ruoyi.vector.domain.vo.VectorDBInfoLabelVO;
import org.ruoyi.vector.domain.vo.VectorDBInfoVo;

import java.util.List;

/**
 * @author admin
 * @description 针对表【vector_db_info(向量数据库信息表)】的数据库操作Service
 * @createDate 2025-06-18 13:56:33
 */
public interface VectorDbInfoService extends IService<VectorDbInfo> {

	/**
	 * 获取支持的向量数据库列表
	 * @return 向量数据库列表
	 */
	List<Label<Integer>> listVectorTypes();

	/**
	 * 新增向量数据库信息
	 * @param vectorDBInfoBo 数据库信息对象
	 * @return 插入的数据库对象的id
	 */
	Long insertVectorDBInfo(VectorDBInfoBo vectorDBInfoBo);

	/**
	 * 根据数据库id 进行删除
	 * @param id 数据库信息ID
	 * @return 是否删除成功
	 */
	Boolean deleteVectorDBInfo(Long id);

	/**
	 * 修改数据库信息
	 * @param vectorDBInfoBo 数据库信息对象
	 * @return 是否修改成功
	 */
	Boolean updateVectorDBInfo(VectorDBInfoBo vectorDBInfoBo);

	/**
	 * 查询向量数据库信息
	 * @return
	 */
	TableDataInfo<VectorDBInfoVo> listVectorDBInfo(VectorDBInfoBo vectorDBInfoBo, PageQuery pageQuery);

	/**
	 * 获取单个数据库信息
	 * @param id 数据库ID
	 * @return 数据库信息
	 */
	VectorDBInfoVo getVectorDBInfo(Long id);

	/**
	 * 获取向量库列表
	 * @return
	 */
	List<Label<Long>> listVectorLabelInfo(String keyword);

}
