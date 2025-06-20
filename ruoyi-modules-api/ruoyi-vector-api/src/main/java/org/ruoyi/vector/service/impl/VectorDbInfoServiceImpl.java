package org.ruoyi.vector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.ai.vector.VectorDBService;
import org.ruoyi.common.ai.vector.common.VectorDBType;
import org.ruoyi.common.core.domain.vo.Label;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.ObjectUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.vector.domain.VectorDbInfo;
import org.ruoyi.vector.domain.bo.VectorDBInfoBo;
import org.ruoyi.vector.domain.vo.VectorDBInfoLabelVO;
import org.ruoyi.vector.domain.vo.VectorDBInfoVo;
import org.ruoyi.vector.service.VectorDbInfoService;
import org.ruoyi.vector.mapper.VectorDbInfoMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @description 针对表【vector_db_info(向量数据库信息表)】的数据库操作Service实现
 * @createDate 2025-06-18 13:56:33
 */
@Service
@RequiredArgsConstructor
public class VectorDbInfoServiceImpl extends ServiceImpl<VectorDbInfoMapper, VectorDbInfo>
		implements VectorDbInfoService, VectorDBService<VectorDbInfo> {

	private final VectorDbInfoMapper vectorDbInfoMapper;

	@Override
	public List<Label<Integer>> listVectorTypes() {
		List<Label<Integer>> vectorTypes = new ArrayList<>();
		for (VectorDBType dbType : VectorDBType.values()) {
			vectorTypes.add(new Label<>(dbType.getValue(), dbType.getName()));
		}
		return vectorTypes;
	}

	@Override
	public Long insertVectorDBInfo(VectorDBInfoBo vectorDBInfoBo) {
		VectorDbInfo vectorDbInfo = MapstructUtils.convert(vectorDBInfoBo, VectorDbInfo.class);
		validEntityBeforeSave(vectorDbInfo);
		save(vectorDbInfo);
		return vectorDbInfo.getId();
	}

	@Override
	public Boolean updateVectorDBInfo(VectorDBInfoBo vectorDBInfoBo) {
		VectorDbInfo vectorDbInfo = MapstructUtils.convert(vectorDBInfoBo, VectorDbInfo.class);
		validEntityBeforeSave(vectorDbInfo);
		return updateById(vectorDbInfo);
	}

	@Override
	public Boolean deleteVectorDBInfo(Long id) {
		return removeById(id);
	}

	private void validEntityBeforeSave(VectorDbInfo vectorDBInfo) {
		// TODO 参数校验
	}

	@Override
	public TableDataInfo<VectorDBInfoVo> listVectorDBInfo(VectorDBInfoBo vectorDBInfoBo, PageQuery pageQuery) {
		if (!LoginHelper.isLogin()) {
			return TableDataInfo.build();
		}
		LambdaQueryWrapper<VectorDbInfo> lqw = buildQueryWrapper(vectorDBInfoBo);
		Page<VectorDBInfoVo> result = vectorDbInfoMapper.selectVoPage(pageQuery.build(), lqw);
		return TableDataInfo.build(result);
	}

	@Override
	public VectorDBInfoVo getVectorDBInfo(Long id) {
		return vectorDbInfoMapper.selectVoById(id);
	}

	@Override
	public List<Label<Long>> listVectorLabelInfo(String keyword) {
		LambdaQueryWrapper<VectorDbInfo> qw = new LambdaQueryWrapper<>();
		qw.eq(ObjectUtils.isNotNull(keyword), VectorDbInfo::getLabel, keyword);
		List<VectorDBInfoVo> vectorDBInfoVos = vectorDbInfoMapper.selectVoList(qw);
		return vectorDBInfoVos.stream().map(item -> new Label<>(item.getId(), item.getLabel())).toList();
	}

	private LambdaQueryWrapper<VectorDbInfo> buildQueryWrapper(VectorDBInfoBo vectorDBInfoBo) {
		LambdaQueryWrapper<VectorDbInfo> qw = new LambdaQueryWrapper<>();
		qw.eq(ObjectUtils.isNotNull(vectorDBInfoBo.getType()), VectorDbInfo::getType, vectorDBInfoBo.getType());
		// TODO 其它条件
		return qw;
	}

}
