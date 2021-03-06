package com.ginkgocap.parasol.knowledge.web.jetty.web.controller;

import com.ginkgocap.parasol.knowledge.web.jetty.web.ResponseError;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/knowledge_columncustom")
public class ColumnCustomController extends BaseControl {

    /*
	@Resource
	private IColumnCustomService columnCustomService;
	
	@RequestMapping("showColumn")
	@ResponseBody
	public InterfaceResult<List<ColumnCustom>> showColumn(HttpServletRequest request, HttpServletResponse response) throws Exception{
		InterfaceResult<List<ColumnCustom>> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		LoginInfo user = this.getLoginInfo(request);
		JSONObject requestJson = this.getRequestJson(request);
		if(!requestJson.containsKey("pid")){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
		}
		Long pid=requestJson.getLong("pid");
		List<ColumnCustom> list=null;
		long uid=0l;
		if(user!=null){
			uid=user.getUserId();
		}

		list=this.columnCustomService.queryListByPidAndUserId(uid, pid);
		result.setResponseData(list);
		return result;
	}
	
	@RequestMapping("addColumn")
	@ResponseBody
	public InterfaceResult<ColumnCustom> addColumn(HttpServletRequest request, HttpServletResponse response) throws Exception{
		InterfaceResult<ColumnCustom> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        LoginInfo user = this.getLoginInfo(request);
		JSONObject requestJson = this.getRequestJson(request);
		//{"columnname":"l2","pid":0,"type":1,"pathName":"l2","tags":"ddd,kkk"}:
		if(!requestJson.containsKey("columnname")||!requestJson.containsKey("pid")){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
		}
		Long pid=requestJson.getLong("pid");
		ColumnCustom cc=new ColumnCustom();
		cc.setColumnLevelPath("1111111111");
		cc.setColumnName(requestJson.getString("columnname"));
		cc.setCreateTime(new Date());
		cc.setUpdateTime(new Date());
		cc.setOrderNum(0);
		cc.setPathName(requestJson.getString("pathName"));
		cc.setTopColType(0);
		cc.setUserOrSystem((short)1);
		long uid=0l;
		if(user!=null){
			uid=user.getUserId();
		}
		ColumnCustom newCC=this.columnCustomService.insert(cc, uid);
		result.setResponseData(newCC);
		return result;
	}
	
	@RequestMapping("updateColumn")
	@ResponseBody
	public InterfaceResult<ColumnCustom> updateColumn(HttpServletRequest request, HttpServletResponse response) throws Exception{
		InterfaceResult<ColumnCustom> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        LoginInfo user = this.getLoginInfo(request);
		JSONObject requestJson = this.getRequestJson(request);
		//{"columnid":"2854","columnName":"l21","tags":"栏目标签"}:
		if(!requestJson.containsKey("columnname")||!requestJson.containsKey("columnid")){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
		}
		Long columnid=requestJson.getLong("columnid");
		ColumnCustom cc=this.columnCustomService.queryById(columnid);
		cc.setColumnName(requestJson.getString("columnName"));
		this.columnCustomService.update(cc);
		return result;
	}
	
	@RequestMapping("deleteColumn")
	@ResponseBody
	public InterfaceResult<ColumnCustom> deleteColumn(HttpServletRequest request, HttpServletResponse response) throws Exception{
		InterfaceResult<ColumnCustom> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        LoginInfo user = this.getLoginInfo(request);
		JSONObject requestJson = this.getRequestJson(request);
		//{"columnid":"2854"}:
		if(!requestJson.containsKey("columnid")){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
		}
		Long columnid=requestJson.getLong("columnid");
		this.columnCustomService.del(columnid);
		return result;
	}*/

	@Override
	protected <T> void processBusinessException(ResponseError error,
			Exception ex) {
		// TODO Auto-generated method stub
		
	}

}
