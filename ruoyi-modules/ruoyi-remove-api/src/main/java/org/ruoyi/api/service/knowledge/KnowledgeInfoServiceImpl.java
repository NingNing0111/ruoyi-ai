package org.ruoyi.api.service.knowledge;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.api.enums.SplitterTypeEnums;
import org.ruoyi.api.enums.VectorStatusEnums;
import org.ruoyi.chain.loader.ResourceLoader;
import org.ruoyi.chain.loader.ResourceLoaderFactory;
import org.ruoyi.common.ai.split.CharacterFileSplitHelper;
import org.ruoyi.common.ai.split.FileSplitHelper;
import org.ruoyi.common.ai.split.MarkdownFileSplitHelper;
import org.ruoyi.common.ai.split.TokenFileSplitHelper;
import org.ruoyi.common.ai.standard.SplitStandard;
import org.ruoyi.common.ai.vector.VectorDBService;
import org.ruoyi.common.core.domain.model.LoginUser;
import org.ruoyi.common.core.domain.vo.Label;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.minio.util.MinIOUtil;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.KnowledgeAttach;
import org.ruoyi.domain.KnowledgeFragment;
import org.ruoyi.domain.KnowledgeInfo;
import org.ruoyi.domain.bo.KnowledgeInfoBo;
import org.ruoyi.domain.bo.KnowledgeInfoUploadBo;
import org.ruoyi.domain.bo.StoreEmbeddingBo;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.mapper.KnowledgeAttachMapper;
import org.ruoyi.mapper.KnowledgeFragmentMapper;
import org.ruoyi.mapper.KnowledgeInfoMapper;
import org.ruoyi.service.IKnowledgeInfoService;
import org.ruoyi.service.VectorStoreService;
import org.ruoyi.system.service.ISysOssService;
import org.ruoyi.vector.domain.VectorDbInfo;
import org.ruoyi.vector.mapper.VectorDbInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 * 知识库Service业务层处理
 *
 * @author ageerle
 * @date 2025-04-08
 */
@RequiredArgsConstructor
@Service
public class KnowledgeInfoServiceImpl implements IKnowledgeInfoService {

  private static final Logger log = LoggerFactory.getLogger(KnowledgeInfoServiceImpl.class);

  private final KnowledgeInfoMapper baseMapper;

  private final VectorStoreService vectorStoreService;

  private final ResourceLoaderFactory resourceLoaderFactory;

  private final KnowledgeFragmentMapper fragmentMapper;

  private final KnowledgeAttachMapper attachMapper;
  @Autowired
  ThreadPoolTaskExecutor threadPoolTaskExecutor;

//  private final IChatModelService chatModelService;

  private final ISysOssService ossService;
  @Autowired
  private MinIOUtil minIOUtil;
  @Autowired
  TransactionTemplate transactionTemplate;
  @Autowired
  VectorDBService vectorDBService;
  @Autowired
  EmbeddingModel embeddingModel;
  @Autowired
  VectorDbInfoMapper vectorDbInfoMapper;
  @Autowired
  KnowledgeInfoMapper knowledgeInfoMapper;
  /**
   * 查询知识库
   */
  @Override
  public KnowledgeInfoVo queryById(Long id) {
    return baseMapper.selectVoById(id);
  }

  /**
   * 查询知识库列表
   */
  @Override
  public TableDataInfo<KnowledgeInfoVo> queryPageList(KnowledgeInfoBo bo, PageQuery pageQuery) {
    LambdaQueryWrapper<KnowledgeInfo> lqw = buildQueryWrapper(bo);
    Page<KnowledgeInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
    List<KnowledgeInfoVo> records = result.getRecords();
    for (KnowledgeInfoVo record : records) {
      record.setLabel(vectorDbInfoMapper.selectById(record.getVId()).getLabel());
    }
    return TableDataInfo.build(result);
  }

  /**
   * 查询知识库列表
   */
  @Override
  public List<KnowledgeInfoVo> queryList(KnowledgeInfoBo bo) {
    LambdaQueryWrapper<KnowledgeInfo> lqw = buildQueryWrapper(bo);
    return baseMapper.selectVoList(lqw);
  }

  private LambdaQueryWrapper<KnowledgeInfo> buildQueryWrapper(KnowledgeInfoBo bo) {
    Map<String, Object> params = bo.getParams();
    LambdaQueryWrapper<KnowledgeInfo> lqw = Wrappers.lambdaQuery();
    lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeInfo::getKid, bo.getKid());
    lqw.eq(bo.getUid() != null, KnowledgeInfo::getUid, bo.getUid());
    lqw.like(StringUtils.isNotBlank(bo.getKname()), KnowledgeInfo::getKname, bo.getKname());
    lqw.eq(bo.getShare() != null, KnowledgeInfo::getShare, bo.getShare());
    lqw.eq(StringUtils.isNotBlank(bo.getDescription()), KnowledgeInfo::getDescription,
        bo.getDescription());
    lqw.eq(StringUtils.isNotBlank(bo.getKnowledgeSeparator()), KnowledgeInfo::getKnowledgeSeparator,
        bo.getKnowledgeSeparator());
    lqw.eq(StringUtils.isNotBlank(bo.getQuestionSeparator()), KnowledgeInfo::getQuestionSeparator,
        bo.getQuestionSeparator());
    lqw.eq(bo.getOverlapChar() != null, KnowledgeInfo::getOverlapChar, bo.getOverlapChar());
    lqw.eq(bo.getRetrieveLimit() != null, KnowledgeInfo::getRetrieveLimit, bo.getRetrieveLimit());
    lqw.eq(bo.getTextBlockSize() != null, KnowledgeInfo::getTextBlockSize, bo.getTextBlockSize());
    return lqw;
  }

  /**
   * 新增知识库
   */
  @Override
  public Boolean insertByBo(KnowledgeInfoBo bo) {
    KnowledgeInfo add = MapstructUtils.convert(bo, KnowledgeInfo.class);
    validEntityBeforeSave(add);
    boolean flag = baseMapper.insert(add) > 0;
    if (flag) {
      bo.setId(add.getId());
    }
    return flag;
  }

  /**
   * 修改知识库
   */
  @Override
  public Boolean updateByBo(KnowledgeInfoBo bo) {
    KnowledgeInfo update = MapstructUtils.convert(bo, KnowledgeInfo.class);
    validEntityBeforeSave(update);
    return baseMapper.updateById(update) > 0;
  }

  /**
   * 保存前的数据校验
   */
  private void validEntityBeforeSave(KnowledgeInfo entity) {
    //TODO 做一些数据校验,如唯一约束
  }

  /**
   * 批量删除知识库
   */
  @Override
  public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
    if (isValid) {
      //TODO 做一些业务上的校验,判断是否需要校验
    }
    return baseMapper.deleteBatchIds(ids) > 0;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void saveOne(KnowledgeInfoBo bo) {
    KnowledgeInfo knowledgeInfo = MapstructUtils.convert(bo, KnowledgeInfo.class);
    if (StringUtils.isBlank(bo.getKid())) {
      String kid = RandomUtil.randomString(10);
      if (knowledgeInfo != null) {
        knowledgeInfo.setKid(kid);
        knowledgeInfo.setUid(LoginHelper.getLoginUser().getUserId());
      }
      baseMapper.insert(knowledgeInfo);
      if (knowledgeInfo != null) {
        vectorStoreService.createSchema(String.valueOf(knowledgeInfo.getId()),
            bo.getVectorModelName());
      }
    } else {
      baseMapper.updateById(knowledgeInfo);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void removeKnowledge(String id) {
    Map<String,Object> map = new HashMap<>();
    KnowledgeInfo knowledgeInfo = baseMapper.selectById(id);
    check(knowledgeInfo);
    map.put("kid",knowledgeInfo.getKid());
    // 删除向量数据
    vectorStoreService.removeById(String.valueOf(knowledgeInfo.getId()),knowledgeInfo.getVectorModelName());
    // 删除附件和知识片段
    fragmentMapper.deleteByMap(map);
    attachMapper.deleteByMap(map);
    // 删除知识库
    baseMapper.deleteByMap(map);
  }

  @Override
  public void upload(KnowledgeInfoUploadBo bo) throws Exception {
    storeContent(bo.getFile(), bo.getKid(), Integer.parseInt(bo.getScore()));
  }

  @Override
  public List<Label<Long>> knowledgeInfoList(String keyword) {
    LambdaQueryWrapper<KnowledgeInfo> qw = new LambdaQueryWrapper<>();
    qw.like(StringUtils.isNotEmpty(keyword), KnowledgeInfo::getKname, keyword);
    List<KnowledgeInfoVo> KnowledgeInfoVos = knowledgeInfoMapper.selectVoList(qw);
    return KnowledgeInfoVos.stream().map(item -> new Label<>(item.getId(), item.getKname())).toList();
  }

  @Transactional(rollbackFor = Exception.class)
  public void storeContent(MultipartFile file, String kid, Integer score) throws Exception {
    String md5 = "";
    try {
      md5 = getFileMd5(file.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException("文章md5值计算错误");
    }
    String fileName = file.getOriginalFilename();
    List<String> chunkList = new ArrayList<>();
    KnowledgeAttach knowledgeAttach = new KnowledgeAttach();
    knowledgeAttach.setKid(kid);
    String docId = RandomUtil.randomString(10);
    Map<String, String> responeMap = minIOUtil.uploadFile(file);
    String url = responeMap.get("url");
    String[] split = url.split("/");
    knowledgeAttach.setUrl(url);
    knowledgeAttach.setBucketName(minIOUtil.getDefaultBucketName());
    knowledgeAttach.setObjectName(split[split.length - 1]);
    knowledgeAttach.setDocId(docId);
    knowledgeAttach.setDocName(fileName);
    knowledgeAttach.setDocType(fileName.substring(fileName.lastIndexOf(".")+1));
    String content = "";
    List<String> fids = new ArrayList<>();
    knowledgeAttach.setContent(content);
    knowledgeAttach.setCreateTime(new Date());
    knowledgeAttach.setScore(score);
    knowledgeAttach.setMd5(md5);
    try {
      knowledgeAttach.setVectorStatus(VectorStatusEnums.NOT_STARTED.getCode());
      attachMapper.insert(knowledgeAttach);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("知识文档上传错误，同一个文件在同一知识库只能上传一次");
    }
    KnowledgeInfoVo knowledgeInfoVo = baseMapper.selectVoOne(Wrappers.<KnowledgeInfo>lambdaQuery()
            .eq(KnowledgeInfo::getId, kid));
    FileSplitHelper fileSplitHelper = getSplitHelper(knowledgeInfoVo.getSplitterType());
    ResourceLoader resourceLoader = resourceLoaderFactory.getLoaderByFileType(knowledgeAttach.getDocType());
    content = resourceLoader.getContent(file.getInputStream());
    SplitStandard splitStandard = new SplitStandard.Builder()
            .textBlockSize(knowledgeInfoVo.getTextBlockSize())
            .separator(knowledgeInfoVo.getKnowledgeSeparator())
            .overlapChar(knowledgeInfoVo.getOverlapChar())
            .kId(Long.parseLong(kid))
            .score(score)
            .createBy(knowledgeAttach.getCreateBy())
            .docId(knowledgeAttach.getId())
            .build();
    List<Document> documentList = fileSplitHelper.split(content, splitStandard);
//    List<Document> list = new ArrayList<>();
//    try {
//      chunkList = resourceLoader.getChunkList(content, kid);
//      List<KnowledgeFragment> knowledgeFragmentList = new ArrayList<>();
//      if (CollUtil.isNotEmpty(chunkList)) {
//        for (int i = 0; i < chunkList.size(); i++) {
//          String fid = RandomUtil.randomString(10);
//          fids.add(fid);
//          KnowledgeFragment knowledgeFragment = new KnowledgeFragment();
//          knowledgeFragment.setKid(kid);
//          knowledgeFragment.setDocId(docId);
//          knowledgeFragment.setFid(fid);
//          knowledgeFragment.setIdx(i);
//          knowledgeFragment.setContent(chunkList.get(i));
//          knowledgeFragment.setCreateTime(new Date());
//          knowledgeFragmentList.add(knowledgeFragment);
//          Map<String, Object> map = new HashMap<>();
//          map.put("kId", kid);
//          map.put("docId", knowledgeAttach.getId().toString());
//          map.put("score", score);
//          map.put("creator", knowledgeAttach.getCreateBy().toString());
//          Document document = new Document(chunkList.get(i), map);
//          list.add(document);
//        }
//      }
//      fragmentMapper.insertBatch(knowledgeFragmentList);
//    } catch (IOException e) {
//      log.error("保存知识库信息失败！{}", e.getMessage());
//      throw new RuntimeException(e);
//    }
//    通过kid查询知识库信息

    CompletableFuture.runAsync(() -> {

      VectorDbInfo vectorDbInfo = vectorDbInfoMapper.selectById(knowledgeInfoVo.getVId());
      if (vectorDbInfo == null) {
        log.warn("未查询到向量数据库 id: {}", knowledgeInfoVo.getVId());
        knowledgeAttach.setVectorStatus(VectorStatusEnums.ERROR.getCode());
        attachMapper.updateById(knowledgeAttach);
        return;
      }

      knowledgeAttach.setVectorStatus(VectorStatusEnums.IN_PROGRESS.getCode());
      attachMapper.updateById(knowledgeAttach);

      VectorStore vectorStore = vectorDBService.getVectorStore(vectorDbInfo, embeddingModel);
      vectorStore.add(documentList);

      knowledgeAttach.setVectorStatus(VectorStatusEnums.COMPLETED.getCode());
      attachMapper.updateById(knowledgeAttach);
    }, threadPoolTaskExecutor);
//    // 通过向量模型查询模型信息
//    ChatModelVo chatModelVo = chatModelService.selectModelByName(knowledgeInfoVo.getEmbeddingModelName());

//    StoreEmbeddingBo storeEmbeddingBo = new StoreEmbeddingBo();
//    storeEmbeddingBo.setKid(kid);
//    storeEmbeddingBo.setDocId(docId);
//    storeEmbeddingBo.setFids(fids);
//    storeEmbeddingBo.setChunkList(chunkList);
//    storeEmbeddingBo.setVectorModelName(knowledgeInfoVo.getVectorModelName());
//    storeEmbeddingBo.setEmbeddingModelName(knowledgeInfoVo.getEmbeddingModelName());
////    storeEmbeddingBo.setApiKey(chatModelVo.getApiKey());
////    storeEmbeddingBo.setBaseUrl(chatModelVo.getApiHost());
//    vectorStoreService.storeEmbeddings(storeEmbeddingBo);
  }

  private FileSplitHelper getSplitHelper(Integer splitterType) {
    if(splitterType.equals(SplitterTypeEnums.CHAR_COUNT.getType())) {
      return new CharacterFileSplitHelper();

    } else if(splitterType.equals(SplitterTypeEnums.MARKDOWN.getType())) {
      return new MarkdownFileSplitHelper();

    } else if(splitterType.equals(SplitterTypeEnums.TOKEN_COUNT.getType())) {
      return new TokenFileSplitHelper();

    }
    throw new RuntimeException("未知的分词策略");
  }

  /**
   * 检查用户是否有删除知识库权限
   *
   * @param knowledgeInfo 知识库
   */
  public void check( KnowledgeInfo knowledgeInfo) {
    LoginUser loginUser = LoginHelper.getLoginUser();
    if (!knowledgeInfo.getUid().equals(loginUser.getUserId())) {
      throw new SecurityException("权限不足");
    }
  }

  public String getFileMd5(byte[] fileBytes) throws IOException {
    return MD5.create().digestHex16(fileBytes);
  }

}
