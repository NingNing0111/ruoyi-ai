package org.ruoyi.vector.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.domain.vo.Label;
import org.ruoyi.common.idempotent.annotation.RepeatSubmit;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.vector.domain.bo.VectorDBInfoBo;
import org.ruoyi.vector.domain.vo.VectorDBInfoVo;
import org.ruoyi.vector.service.VectorDbInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zhang De Ning
 * @email zhangdening@huice.com
 * @time 2025-06-18 16:40
 * @description
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/vector/db")
public class VectorDBInfoController {

    private final VectorDbInfoService vectorDbInfoService;

    @GetMapping("/types")
    @SaCheckPermission("vector:db:types")
    public List<Label<Integer>> types() {
        return vectorDbInfoService.listVectorTypes();
    }

    @GetMapping("/list")
    @SaCheckPermission("vector:db:list")
    public TableDataInfo<VectorDBInfoVo> list(VectorDBInfoBo bo, PageQuery pageQuery) {
        return vectorDbInfoService.listVectorDBInfo(bo, pageQuery);
    }

    @PostMapping("/insert")
    @SaCheckPermission("vector:db:insert")
    @Log(title = "新增或者修改向量库信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    public Long insert(@RequestBody VectorDBInfoBo vectorDBInfoBo) {
        return vectorDbInfoService.insertVectorDBInfo(vectorDBInfoBo);
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("vector:db:delete")
    @Log(title = "删除向量库信息", businessType = BusinessType.DELETE)
    public Boolean delete(@PathVariable Long id) {
        return vectorDbInfoService.deleteVectorDBInfo(id);
    }

    @SaCheckPermission("vector:db:update")
    @Log(title = "修改向量库信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Boolean update(@RequestBody VectorDBInfoBo vectorDBInfoBo) {
        return vectorDbInfoService.updateVectorDBInfo(vectorDBInfoBo);
    }

}
