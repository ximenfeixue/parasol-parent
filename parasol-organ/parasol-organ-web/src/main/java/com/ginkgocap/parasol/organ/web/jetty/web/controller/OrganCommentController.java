package com.ginkgocap.parasol.organ.web.jetty.web.controller;

import com.ginkgocap.parasol.organ.web.jetty.web.utils.CommonUtil;
import com.ginkgocap.ywxt.organ.model.Constants;
import com.ginkgocap.ywxt.organ.model.comment.CommentMain;
import com.ginkgocap.ywxt.organ.model.comment.CommentPraise;
import com.ginkgocap.ywxt.organ.model.comment.CommentReply;
import com.ginkgocap.ywxt.organ.service.comment.CommentMainService;
import com.ginkgocap.ywxt.organ.service.comment.CommentPraiseService;
import com.ginkgocap.ywxt.organ.service.comment.CommentReplyService;
import com.ginkgocap.ywxt.organ.service.template.TemplateService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.model.UserConfig;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserBlackService;
import com.ginkgocap.ywxt.user.service.UserConfigService;
import com.ginkgocap.ywxt.user.service.UserService;
import com.ginkgocap.ywxt.util.PageUtil;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by jbqiu on 2016/6/10.
 * controller 组织点评controller
 */
@Controller
@RequestMapping("/organ/comment")
public class OrganCommentController  extends BaseController{

     @Resource
     private CommentMainService commentMainService;
     @Resource
     private CommentPraiseService commentPraiseService;
     @Resource
     private CommentReplyService ommentReplyService;
 	@Autowired
 	public TemplateService templateService;
 	@Resource
 	public UserConfigService userConfigService;
	@Autowired
	private UserService userSerivce;
	// 用户黑名单
	private UserBlackService userBlackService;
	@Autowired
	// 好友关系服务
	private FriendsRelationService friendsRelationService;
     /**保存发布评论
 	 * @author zbb
 	 */
 	@ResponseBody
 	@RequestMapping(value="/saveComment.json",method=RequestMethod.POST)
 	public Map<String, Object> saveComment(HttpServletRequest request,
 			HttpServletResponse response) throws IOException {
 		String requestJson = getJsonParamStr(request);
 		Map<String, Object> model = new HashMap<String, Object>();
 		Map<String, Object> responseDataMap = new HashMap<String, Object>();
 		Map<String, Object> notificationMap = new HashMap<String, Object>();

        User userBasic=null;
        userBasic=getUser(request);
 		boolean flag = true;
 		if (requestJson != null && !"".equals(requestJson)){
 			JSONObject jo = JSONObject.fromObject(requestJson);
			if (userBasic == null) {
				setSessionAndErr(request, response, "-1", "请登录以后再操作");
			} else {
			       ObjectMapper objectMapper=new ObjectMapper();
			       CommentMain commentMain=objectMapper.readValue(jo.toString(),CommentMain.class);
			       String username=null;
			       int anonymous=commentMain.getAnonymous();
			       if(anonymous==0){
			    	   username=userBasic.getName();
			       }else{
			    	   username="匿名用户";
			       }
			       commentMain.setCommentuserid(userBasic.getId());
			       commentMain.setCommentusername(username);
			       commentMain.setUserurl(userBasic.getPicPath());
			       commentMainService.savecommentMain(commentMain);

			}
 		}else{
 			setSessionAndErr(request, response, "-1", "请完善信息！");
 			 flag = false;
 		} 
 		   responseDataMap.put("success", flag);
 	       notificationMap.put("notifCode", "0001");
 		   notificationMap.put("notifInfo", "hello mobile app!");
 		model.put("responseData", responseDataMap);
 		model.put("notification", notificationMap);
 	
 		return model;
 	}
 	
 	
	/**删除评论
	 * @author zbb
	 */
	@ResponseBody
	@RequestMapping(value="/deleteComment.json",method=RequestMethod.POST)
	public Map<String, Object> deleteComment(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 String requestJson = getJsonParamStr(request);
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> responseDataMap = new HashMap<String, Object>();
		Map<String, Object> notificationMap = new HashMap<String, Object>();
        User userBasic=null;
        userBasic=getUser(request);
		boolean flag = true;
		if (requestJson != null && !"".equals(requestJson)){
			JSONObject jo = JSONObject.fromObject(requestJson);
			if (userBasic == null) {
				setSessionAndErr(request, response, "-1", "请登录以后再操作");
			} else {
				   String id = jo.optString( "id");
				   commentMainService.deleteById(Long.valueOf(id));
				   commentPraiseService.deleteByCommentid(Long.valueOf(id));
				   ommentReplyService.deleteByCommentid(Long.valueOf(id));
			}
		}else{
			setSessionAndErr(request, response, "-1", "请完善信息！");
			 flag = false;
		}
		   responseDataMap.put("success", flag);
	       notificationMap.put("notifCode", "0001");
			notificationMap.put("notifInfo", "hello mobile app!");
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
	
	
	/**查询评论
	 * @author zbb
	 */
	@ResponseBody
	@RequestMapping(value="/selectComment.json",method=RequestMethod.POST)
	public Map<String, Object> selectComment(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 String requestJson = getJsonParamStr(request);
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> responseDataMap = new HashMap<String, Object>();
		Map<String, Object> notificationMap = new HashMap<String, Object>();
        User userBasic=null;
        userBasic=getUser(request);
		boolean flag = true;
		if (requestJson != null && !"".equals(requestJson)){
			JSONObject jo = JSONObject.fromObject(requestJson);
				 Map<String,Object>  canshu=new HashMap<String,Object>();
				  long orgid = CommonUtil.getLongFromJSONObject(jo, "orgid");
				   int currentPage = jo.getInt( "currentPage");
				   int pageSize = jo.getInt( "pageSize");
				   int ordertype=jo.getInt("ordertype");
				   if("".equals(ordertype)){
					   ordertype=2;
				   }
				   System.out.println("参数========="+orgid+"=="+currentPage+"=="+pageSize+"=="+ordertype);
				   canshu.put("orgid", orgid);
				   canshu.put("currentPage", (currentPage-1)*pageSize);
				   canshu.put("pageSize", pageSize);
				   canshu.put("ordertype", ordertype);
				   List<CommentMain> commentMainlist = commentMainService.findByOrganId(canshu);
				   int count = commentMainService.selectcount(canshu);
				   pageSize=pageSize>Constants.max_select_count?Constants.max_select_count:pageSize;
				   PageUtil page = new PageUtil(count, currentPage, pageSize);
				   responseDataMap.put("page", page);
			       for(CommentMain commentMain:commentMainlist){
			    	  Long id = commentMain.getId();
			    	  Long praisecount = commentPraiseService.selectPraiseCount(id);
			    	  boolean praiseresult=false;
			    	  if(userBasic!=null){
			    		   praiseresult = commentPraiseService.selectUserPraiseCount(userBasic.getId(), id);
			    	  }
			    	  commentMain.setPraisecount(praisecount);
			    	  commentMain.setPraiseresult(praiseresult);
			    	  List<CommentReply> replyList = ommentReplyService.findByCommentid(id);
			    	  commentMain.setReplyMap(replyList);
			    	  commentMain.setReplyCount(ommentReplyService.findByCommentIdCount(id));
			       }
						UserConfig uc = userConfigService.getByUserId(orgid);
						int Jurisdiction=2;
						if(uc!=null){
							Jurisdiction= uc.getEvaluateVisible();
						}
			       responseDataMap.put("Jurisdiction",Jurisdiction);
			       responseDataMap.put("commentMap", commentMainlist);
		}else{
			 flag = false;
			 returnFailMSGNew("-1", "参数异常！");
		}
		   responseDataMap.put("success", flag);
	       notificationMap.put("notifCode", "0001");
		   notificationMap.put("notifInfo", "hello mobile app!");
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
	
	
	/**点赞
	 * @author zbb
	 */
	@ResponseBody
	@RequestMapping(value="/clickPraise.json",method=RequestMethod.POST)
	public Map<String, Object> clickPraise(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 String requestJson = getJsonParamStr(request);
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> responseDataMap = new HashMap<String, Object>();
		Map<String, Object> notificationMap = new HashMap<String, Object>();
        User userBasic=null;
        userBasic=getUser(request);
		boolean flag = true;
		if (requestJson != null && !"".equals(requestJson)){
			JSONObject jo = JSONObject.fromObject(requestJson);
			if (userBasic == null) {
				setSessionAndErr(request, response, "-1", "请登录以后再操作");
			} else {
				long commentid = CommonUtil.getLongFromJSONObject(jo, "commentid");
				int  usertype = jo.optInt("usertype");
				long userid = userBasic.getId();
				boolean praiseresult = commentPraiseService.selectUserPraiseCount(userBasic.getId(), commentid);
				if(praiseresult){
				setSessionAndErr(request, response, "-1", "给用户已经点过赞");
				}else{
					CommentPraise commentPraise=new CommentPraise();
					commentPraise.setCommentid(commentid);
					commentPraise.setUserid(userid);
					commentPraise.setUsername(userBasic.getName());
					commentPraise.setUserpic(userBasic.getPicPath());
					commentPraise.setUsertype(usertype);
					commentPraiseService.insertPraiseUser(commentPraise);
				}
			}
		}else{
			setSessionAndErr(request, response, "-1", "请完善信息！");
			 flag = false;
		}
		   responseDataMap.put("success", flag);
	       notificationMap.put("notifCode", "0001");
		   notificationMap.put("notifInfo", "hello mobile app!");
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
	
	
	/**取消点赞
	 * @author zbb
	 */
	@ResponseBody
	@RequestMapping(value="/removePraise.json",method=RequestMethod.POST)
	public Map<String, Object> removePraise(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 String requestJson = getJsonParamStr(request);
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> responseDataMap = new HashMap<String, Object>();
		Map<String, Object> notificationMap = new HashMap<String, Object>();
        User userBasic=null;
        userBasic=getUser(request);
		boolean flag = true;
		if (requestJson != null && !"".equals(requestJson)){
			JSONObject jo = JSONObject.fromObject(requestJson);
			if (userBasic == null) {
				setSessionAndErr(request, response, "-1", "请登录以后再操作");
			} else {
				long commentid = CommonUtil.getLongFromJSONObject(jo, "commentid");
				long userid = userBasic.getId();
				commentPraiseService.deleteById(userid,commentid);
			}
		}else{
			setSessionAndErr(request, response, "-1", "请完善信息！");
			 flag = false;
		}
		   responseDataMap.put("success", flag);
	       notificationMap.put("notifCode", "0001");
		   notificationMap.put("notifInfo", "hello mobile app!");
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
	
	
	/**回复评论
	 * @author zbb
	 */
	@ResponseBody
	@RequestMapping(value="/replyComment.json",method=RequestMethod.POST)
	public Map<String, Object> replyComment(HttpServletRequest request,
		HttpServletResponse response) throws IOException {
		String requestJson = getJsonParamStr(request);
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> responseDataMap = new HashMap<String, Object>();
		Map<String, Object> notificationMap = new HashMap<String, Object>();
        User userBasic=null;
        userBasic=getUser(request);

		boolean flag = true;
		if (requestJson != null && !"".equals(requestJson)){
			JSONObject jo = JSONObject.fromObject(requestJson);
			if (userBasic == null) {
				setSessionAndErr(request, response, "-1", "请登录以后再操作");
			} else {
				ObjectMapper objectMapper=new ObjectMapper();
				CommentReply commentReply=objectMapper.readValue(jo.toString(),CommentReply.class);
				String username=null;
				int anonymous = commentReply.getAnonymous();
			       if(anonymous==0){
			    	   username=userBasic.getName();
			       }else{
			    	   username="匿名用户";
			       }
				commentReply.setReplyuserid(userBasic.getId());
				commentReply.setReplyusername(username);
			    CommentReply id = ommentReplyService.savecommentReply(commentReply);
				responseDataMap.put("commentReply", id);
			}
		}else{
			setSessionAndErr(request, response, "-1", "请完善信息！");
			 flag = false;
		}
		   responseDataMap.put("success", flag);
	       notificationMap.put("notifCode", "0001");
		   notificationMap.put("notifInfo", "hello mobile app!");
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
	
	/**删除回复
	 * @author zbb
	 */
	@ResponseBody
	@RequestMapping(value="/removeReply.json",method=RequestMethod.POST)
	public Map<String, Object> removereply(HttpServletRequest request,
		HttpServletResponse response) throws IOException {
		String requestJson = getJsonParamStr(request);
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> responseDataMap = new HashMap<String, Object>();
		Map<String, Object> notificationMap = new HashMap<String, Object>();
        User userBasic=null;
        userBasic=getUser(request);


        boolean flag = true;
		if (requestJson != null && !"".equals(requestJson)){
			JSONObject jo = JSONObject.fromObject(requestJson);
			if (userBasic == null) {
				setSessionAndErr(request, response, "-1", "请登录以后再操作");
			} else {
				long id = CommonUtil.getLongFromJSONObject(jo, "id");
				ommentReplyService.deleteByUserid(id);
			}
		}else{
			setSessionAndErr(request, response, "-1", "请完善信息！");
			 flag = false;
		}
		   responseDataMap.put("success", flag);
	       notificationMap.put("notifCode", "0001");
		   notificationMap.put("notifInfo", "hello mobile app!");
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
	
	
	/**查询一条评论内容
	 * @author zbb
	 */
	@ResponseBody
	@RequestMapping(value="/findOne.json",method=RequestMethod.POST)
	public Map<String, Object> findOne(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 String requestJson = getJsonParamStr(request);
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> responseDataMap = new HashMap<String, Object>();
		Map<String, Object> notificationMap = new HashMap<String, Object>();
        User userBasic=null;
        userBasic=getUser(request);
		boolean flag = true;
		if (requestJson != null && !"".equals(requestJson)){
			JSONObject jo = JSONObject.fromObject(requestJson);
				  long id = CommonUtil.getLongFromJSONObject(jo, "id");  //品论的id
				  CommentMain commentMain = commentMainService.findOne(id);
			      Long praisecount = commentPraiseService.selectPraiseCount(id);
			      List<CommentPraise> praiseUser = commentPraiseService.selectCommentPraiseUser(id);
			      boolean praiseresult=false;
			      if(userBasic!=null){
			         praiseresult = commentPraiseService.selectUserPraiseCount(userBasic.getId(), id);
			      }
			      commentMain.setPraiseUser(praiseUser);
			      commentMain.setPraisecount(praisecount);
			      commentMain.setPraiseresult(praiseresult);
			      commentMain.setReplyMap(ommentReplyService.findByCommentidDesc(id));
			      commentMain.setReplyCount(ommentReplyService.findByCommentIdCount(id));
			      responseDataMap.put("commentMain", commentMain);
		}else{
			setSessionAndErr(request, response, "-1", "请完善信息！");
			 flag = false;
		}
		   responseDataMap.put("success", flag);
	       notificationMap.put("notifCode", "0001");
			notificationMap.put("notifInfo", "hello mobile app!");
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
	
	/**查询组织用户评价星级
	 * @author zbb
	 */
	@ResponseBody
	@RequestMapping(value="/selectStar.json",method=RequestMethod.POST)
	public Map<String, Object> selectStar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 String requestJson = getJsonParamStr(request);
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> responseDataMap = new HashMap<String, Object>();
		Map<String, Object> notificationMap = new HashMap<String, Object>();
        User userBasic=null;
        userBasic=getUser(request);
		boolean flag = true;
		if (requestJson != null && !"".equals(requestJson)){
			JSONObject jo = JSONObject.fromObject(requestJson);
				  long id = CommonUtil.getLongFromJSONObject(jo, "id");  //需要查询星级的组织用户id
				  int svgstar = commentMainService.findStar(id);

			      responseDataMap.put("star", svgstar);
		}else{
			setSessionAndErr(request, response, "-1", "请完善信息！");
			 flag = false;
		}
		   responseDataMap.put("success", flag);
	       notificationMap.put("notifCode", "0001");
			notificationMap.put("notifInfo", "hello mobile app!");
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
}
