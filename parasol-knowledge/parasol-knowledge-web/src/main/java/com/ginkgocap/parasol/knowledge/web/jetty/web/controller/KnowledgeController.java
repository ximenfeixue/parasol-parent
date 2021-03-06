package com.ginkgocap.parasol.knowledge.web.jetty.web.controller;

import com.ginkgocap.parasol.knowledge.model.KnowledgeData;
import com.ginkgocap.parasol.knowledge.model.LoginInfo;
import com.ginkgocap.parasol.knowledge.model.common.CommonResultCode;
import com.ginkgocap.parasol.knowledge.model.common.InterfaceResult;
import com.ginkgocap.parasol.knowledge.service.IKnowledgeServiceV1;
import com.ginkgocap.parasol.knowledge.web.jetty.web.ResponseError;
import net.sf.json.JSONObject;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2016/3/24.
 */
public class KnowledgeController  extends BaseControl {

    private Logger logger = LoggerFactory.getLogger(KnowledgeControllerV2.class);

    @Autowired
    private IKnowledgeServiceV1 knowledgeService;

    /**
     * 插入数据
     * @author 周仕奇
     * @date 2016年1月15日 下午4:51:59
     * @throws java.io.IOException
     */

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public InterfaceResult insert(HttpServletRequest request, HttpServletResponse response) throws IOException {

        LoginInfo user = this.getLoginInfo(request);

        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        JSONObject requestJson = this.getRequestJson(request);

        KnowledgeData KnowledgeData = (KnowledgeData) JSONObject.toBean(requestJson, KnowledgeData.class);

        InterfaceResult<KnowledgeData> affterSaveKnowledgeData = null;

        try {
            affterSaveKnowledgeData = this.knowledgeService.insert(KnowledgeData, user.getUserId());
        } catch (Exception e) {
            logger.error("知识插入失败！失败原因："+affterSaveKnowledgeData.getNotification().getNotifInfo());
            return affterSaveKnowledgeData;
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(null);
    }

    /**
     * 更新数据
     * @author 周仕奇
     * @date 2016年1月15日 下午4:52:17
     * @throws IOException
     */
     @RequestMapping(value = "",method = RequestMethod.PUT)
     @ResponseBody
     public InterfaceResult<KnowledgeData> update(HttpServletRequest request, HttpServletResponse response) throws IOException {

     LoginInfo user = this.getLoginInfo(request);

     if(user == null)
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);

     JSONObject requestJson = this.getRequestJson(request);

     KnowledgeData KnowledgeData = (KnowledgeData) JSONObject.toBean(requestJson, KnowledgeData.class);

     InterfaceResult<KnowledgeData> affterSaveKnowledgeData = null;

     try {
     affterSaveKnowledgeData = this.knowledgeService.update(KnowledgeData, user.getUserId());
     } catch (Exception e) {
     logger.error("知识更新失败！失败原因："+affterSaveKnowledgeData.getNotification().getNotifInfo());
     return affterSaveKnowledgeData;
     }

     return InterfaceResult.getSuccessInterfaceResultInstance(null);
     }

     /**
      * 删除数据
      * @author 周仕奇
     * @date 2016年1月15日 下午4:52:33
     * @param id 知识主键
     * @param columnId 栏目主键
     * @throws IOException
     */
     @RequestMapping(value = "/{id}/{columnId}", method = RequestMethod.DELETE)
     @ResponseBody
     public InterfaceResult<KnowledgeData> delete(HttpServletRequest request, HttpServletResponse response,
     @PathVariable Long id,@PathVariable short columnId) throws IOException {

     LoginInfo user = this.getLoginInfo(request);

     if(user == null)
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
     if(id <= 0 || columnId <= 0){
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
     }

     InterfaceResult<KnowledgeData> affterDeleteKnowledgeData = null;
     try {
        affterDeleteKnowledgeData = this.knowledgeService.deleteByKnowledgeId(id, columnId, user.getUserId());
     } catch (Exception e) {
     logger.error("知识删除失败！失败原因："+affterDeleteKnowledgeData.getNotification().getNotifInfo());
     return affterDeleteKnowledgeData;
     }

     return InterfaceResult.getSuccessInterfaceResultInstance(null);
     }

     /**
      * 提取知识详细信息，一般用在详细查看界面、编辑界面
      * @author 周仕奇
     * @date 2016年1月15日 下午4:53:25
     * @param id 知识主键
     * @param columnId 栏目主键
     * @throws IOException
     */
     @RequestMapping(value = "/{id}/{columnId}", method = RequestMethod.GET)
     @ResponseBody
     public InterfaceResult<KnowledgeData> getByIdAndColumnId(HttpServletRequest request, HttpServletResponse response,
     @PathVariable Long id,@PathVariable short columnId) throws IOException {

     //判断参数合法性
     if(id <= 0 || columnId <= 0) {
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
     }

     LoginInfo user = this.getLoginInfo(request);

     //如果是游客登录，则给予金桐脑权限
     if (null == user) {
     user = new LoginInfo(0L,0L);
     //user.setUserId(0L);// 金桐脑
     }

     KnowledgeData affterDeleteKnowledgeData = null;
     try {
        affterDeleteKnowledgeData = this.knowledgeService.getDetailById(id, columnId, user.getUserId());
     } catch (Exception e) {
        logger.error("知识提取失败！失败原因："+ e);
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
     }

     //数据为空则直接返回异常给前端
     if(affterDeleteKnowledgeData == null) {
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
     }

        return InterfaceResult.getSuccessInterfaceResultInstance(affterDeleteKnowledgeData);
     }

     /**
      * 提取所有知识数据
      * @author 周仕奇
     * @date 2016年1月15日 下午4:54:27
     * @param start 分页起始
     * @param size 分页大小
     * @throws IOException
     */
     @RequestMapping(value = "/all/{start}/{size}", method = RequestMethod.GET)
     @ResponseBody
     public InterfaceResult<List<KnowledgeData>> getAll(HttpServletRequest request, HttpServletResponse response,
     @PathVariable int start,@PathVariable int size) throws IOException {

     LoginInfo user = this.getLoginInfo(request);

     if(user == null)
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);

     List<KnowledgeData> getKnowledgeData = null;
     try {
        getKnowledgeData = this.knowledgeService.getBaseAll(start, size);
     } catch (Exception e) {
         logger.error("知识提取失败！失败原因："+ getKnowledgeData);
         return InterfaceResult.getInterfaceResultInstance(null);
     }

     return InterfaceResult.getSuccessInterfaceResultInstance(getKnowledgeData);
     }

     /**
      * 根据栏目提取知识数据
      * @author 周仕奇
     * @date 2016年1月15日 下午4:54:27
     * @param start 分页起始
     * @param size 分页大小
     * @throws IOException
     */
     @RequestMapping(value = "/all/{columnId}/{start}/{size}", method = RequestMethod.GET)
     @ResponseBody
     public InterfaceResult<List<KnowledgeData>> getAllByColumnId(HttpServletRequest request, HttpServletResponse response,
     @PathVariable long columnId,@PathVariable int start,@PathVariable int size) throws IOException {

     LoginInfo user = this.getLoginInfo(request);

     if(user == null)
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);

     List<KnowledgeData> getKnowledgeData = null;
     try {
        getKnowledgeData = this.knowledgeService.getBaseByCreateUserId(user.getUserId(), start, size);
     } catch (Exception e) {
     logger.error("知识提取失败！失败原因：{}",e);
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
     }

     return InterfaceResult.getSuccessInterfaceResultInstance(getKnowledgeData);
     }

     /**
      * 提取当前用户的所有知识数据
      * @author 周仕奇
     * @date 2016年1月15日 下午4:54:27
     * @param start 分页起始
     * @param size 分页大小
     * @throws IOException
     */
     @RequestMapping(value = "/user/{start}/{size}", method = RequestMethod.GET)
     @ResponseBody
     public InterfaceResult<List<KnowledgeData>> getByCreateUserId(HttpServletRequest request, HttpServletResponse response,
     @PathVariable int start,@PathVariable int size) throws IOException {

     LoginInfo user = this.getLoginInfo(request);

     if(user == null)
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);

     List<KnowledgeData> getKnowledgeData = null;
     try {
     getKnowledgeData = this.knowledgeService.getBaseByCreateUserId(user.getUserId(), start, size);
     } catch (Exception e) {
     logger.error("知识提取失败！失败原因："+e);
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
     }

     return InterfaceResult.getSuccessInterfaceResultInstance(getKnowledgeData);
     }

     /**
      * 根据栏目提取当前用户的知识数据
      * @author 周仕奇
     * @date 2016年1月15日 下午4:54:27
     * @param start 分页起始
     * @param size 分页大小
     * @throws IOException
     */
     @RequestMapping(value = "/user/{columnId}/{start}/{size}", method = RequestMethod.GET)
     @ResponseBody
     public InterfaceResult<List<KnowledgeData>> getByCreateUserIdAndColumnId(HttpServletRequest request, HttpServletResponse response,
     @PathVariable short columnId,@PathVariable int start,@PathVariable int size) throws IOException {

     LoginInfo user = this.getLoginInfo(request);

     if(user == null)
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);

     List<KnowledgeData> getKnowledgeData = null;
     try {
     getKnowledgeData = this.knowledgeService.getBaseByCreateUserIdAndColumnId(user.getUserId(), columnId, start, size);
     } catch (Exception e) {
     logger.error("知识提取失败！失败原因：" + e);
     return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
     }

     return InterfaceResult.getSuccessInterfaceResultInstance(getKnowledgeData);
     }

     /**
      * 首页获取大数据知识热门推荐和发现热门知识
      * @author 周仕奇
     * @date 2016年1月18日 上午10:46:43
     * @param type 1,推荐 2,发现
     * @param start
     * @param size
     * @throws Exception
     */
     @RequestMapping(value = "/all/{type}/{start}/{size}", method = RequestMethod.GET)
     @ResponseBody
     public InterfaceResult<Map<String, Object>> getRecommendedKnowledge(HttpServletRequest request, HttpServletResponse response,
     @PathVariable String type,@PathVariable int start, @PathVariable int size) throws Exception {
     String url = (String) request.getSession().getServletContext().getAttribute("newQueryHost");
     List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
     pairs.add(new BasicNameValuePair("page", String.valueOf(start)));
     pairs.add(new BasicNameValuePair("rows", String.valueOf(size)));
     pairs.add(new BasicNameValuePair("type", type));// 1,推荐 2,发现
     Map<String, Object> model = new HashMap<String, Object>();
     try {
     //String responseJson = HttpClientHelper.post(url + "/API/hotKno.do", pairs);
     //model.put("list", PackingDataUtil.getRecommendResult(responseJson));
     } catch (Exception e) {
     logger.error("连接大数据出错！");
     e.printStackTrace();
     }
     return InterfaceResult.getSuccessInterfaceResultInstance(model);
     }

    @Override
    protected <T> void processBusinessException(ResponseError error,
                                                Exception ex) {
        // TODO Auto-generated method stub

    }

}