package com.ginkgocap.parasol.organ.web.jetty.web.controller;

import com.ginkgocap.ywxt.organ.model.Area;
import com.ginkgocap.ywxt.organ.model.Customer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OrganUtils {
	
	
	public static JSONArray createMoudles(Customer customer){

    	
 	   JSONArray moudles=new JSONArray();
 	   
 	   JSONObject baiscInfoJobj=new JSONObject();
 	    int orgType= customer.getOrgType();
 	    
 	    long templateId=customer.getTemplateId();
 	    
 	    if(templateId!=0){
 	    	
 	    	if(templateId==1||templateId==5){
 	    		 baiscInfoJobj.put("moudleId", 1);
 	    	}else{
 	    		 baiscInfoJobj.put("moudleId", 2);
 	    	}
 	    	
 	    }else{
 	    	 if(orgType==2){// 政府
 	 	    	 baiscInfoJobj.put("moudleId", 2);
 	 	    }else{
 	 	    	 baiscInfoJobj.put("moudleId", 1);
 	 	    }
 	    }
 	   
 	    
 	    baiscInfoJobj.put("level", 0);
 	    baiscInfoJobj.put("desc", "基本信息");
 	    baiscInfoJobj.put("isVisible", 1);
 	    baiscInfoJobj.put("name", "basicInfo");
 	    baiscInfoJobj.put("type", "single");
 	    
 	    JSONArray basicControlList=new JSONArray();
 	    
 	    
 	    // 增加简称组建
 	    JSONObject shortNameObject=new JSONObject();
 	    
 	    shortNameObject.put("desc", "简称");
 	   
 	    shortNameObject.put("name", "shortName");
 	    shortNameObject.put("isMust", true);
 	    shortNameObject.put("maxlength", 30);
 	    shortNameObject.put("type", "text");
 	    shortNameObject.put("value", customer.getName());
 	    basicControlList.add(shortNameObject);
 	    
 	    
 	    
 	    // 增加名称模块
 	    JSONObject nameObject=new JSONObject();
 	    
 	    nameObject.put("desc", "全称");
 	    nameObject.put("name", "name");
 	    nameObject.put("isMust", false);
 	    nameObject.put("maxlength", 50);
 	    nameObject.put("type", "text");
 	    nameObject.put("value", customer.getOrganAllName());
 	    basicControlList.add(nameObject);
 	    
 	    
 	
 	    
 	    
 	    
 	    // 增加简类型组建
 	    JSONObject orgTypeObject=new JSONObject();
 	    orgTypeObject.put("desc", "类型");
 	    orgTypeObject.put("name", "orgType");
 	    orgTypeObject.put("isMust", false);
 	    orgTypeObject.put("type", "select");
 	 
 	    
 	    JSONArray  itemArray=new JSONArray();
 	    
 	    JSONObject  qiYeItem=new JSONObject();
 	    qiYeItem.put("name", "企业");
 	    qiYeItem.put("value", 1);
 	    if(orgType==1){
 	    	qiYeItem.put("checked", true);
 	    }else{
 	    	qiYeItem.put("checked", false);
 	    }
 	    itemArray.add(qiYeItem);
 	    
 	    JSONObject  zhengfuItem=new JSONObject();
 	    zhengfuItem.put("name", "政府/事业单位");
 	    zhengfuItem.put("value", 2);
 	    if(orgType==2){
 	    	zhengfuItem.put("checked", true);
 	    }else{
 	    	zhengfuItem.put("checked", false);
 	    }
 	    itemArray.add(zhengfuItem);
 	    
 	    
 	    
 	    JSONObject  meitiItem=new JSONObject();
 	    meitiItem.put("name", "媒体");
 	    meitiItem.put("value", 3);
 	    if(orgType==3){
 	    	meitiItem.put("checked", true);
 	    }else{
 	    	meitiItem.put("checked", false);
 	    }
 	    itemArray.add(meitiItem);
 	    
 	    
 	    JSONObject  qitaItem=new JSONObject();
 	    qitaItem.put("name", "其它");
 	    qitaItem.put("value", 4);
 	    if(orgType==4){
 	    	qitaItem.put("checked", true);
 	    }else{
 	    	qitaItem.put("checked", false);
 	    }
 	    itemArray.add(qitaItem);
 	    orgTypeObject.put("items", itemArray);
 	    basicControlList.add(orgTypeObject);
 	    
 	    
 	    
 	    
 	    JSONObject  districtObject=new JSONObject();
 	    districtObject.put("name", "district");
 	    districtObject.put("isMust", false);
 	    districtObject.put("type", "region");
 	    districtObject.put("desc", "所在地区");
 	    
 	    JSONObject  districtVlueObject=new JSONObject();
 	    String areaString=customer.getAreaString();
 	    
 	    if(areaString!=null&&!"".equals(areaString)){
 	    	String[] aresStrings=areaString.split("-");
 	    	
 	    	districtVlueObject.put("province", aresStrings[0]);
 	    	if(aresStrings.length==3){
 	    		districtVlueObject.put("city", aresStrings[1]);
 	    		districtVlueObject.put("county", aresStrings[2]);
 	    	}else if(aresStrings.length==2){
 	    		districtVlueObject.put("city", aresStrings[0]);
 	    		districtVlueObject.put("county", aresStrings[1]);
 	    	}
 	    	
 	    }
 	    
 	    districtObject.put("value", districtVlueObject);
 	    basicControlList.add(districtObject);
 	    
 	    
 	    
 	    
 	    JSONObject  industryObject=new JSONObject();
 	    
 	    industryObject.put("name", "industry");
 	    industryObject.put("isMust", false);
 	    industryObject.put("type", "industry");
 	    industryObject.put("desc", "所属行业");
 	    
 	    JSONObject industryValueObject=new JSONObject();
 	    industryValueObject.put("industry", customer.getIndustry());
 	    industryValueObject.put("industryId", customer.getIndustryId());
 	    industryObject.put("value", industryValueObject);
 	    basicControlList.add(industryObject);
 	    
 	    
 	    if(templateId==1&&templateId==5||orgType==1){// 企业和媒体 有是否上市
 	    	
 	    	    JSONObject  listTingObject=new JSONObject();
 	    	    listTingObject.put("name", "isListing");
 	    	    listTingObject.put("isMust", false);
 	    	    listTingObject.put("type", "radio");
 	    	    listTingObject.put("desc", "是否上市");
 	    	    
 	    	    JSONArray listItemArray=new JSONArray();
 	    	    
 	    	    JSONObject yesItemObj=new JSONObject();
 	    	    yesItemObj.put("name", "是");
 	    	    yesItemObj.put("value", "1");
 	    	    
 	    	    if(customer.getIsListing().equals("1")){
 	    	    	yesItemObj.put("checked", true);
 	    	    }else {
 	    	    	yesItemObj.put("checked", false);
 	    	    }
 	    	    
 	    	    listItemArray.add(yesItemObj);
 	    	    
 	    	    
 	    	    JSONObject noItemObj=new JSONObject();
 	    	    noItemObj.put("name", "否");
 	    	    noItemObj.put("value", "0");
 	    	    
 	    	    if(customer.getIsListing().equals("0")){
 	    	    	noItemObj.put("checked", true);
 	    	    }else {
 	    	    	noItemObj.put("checked", false);
 	    	    }
 	    	    
 	    	    listItemArray.add(noItemObj);
 	    	    
 	    	    listTingObject.put("items", listItemArray);
 	    	    
 	    	    basicControlList.add(listTingObject);
 	    	    
 	    	    
 	    	    
 	    	    JSONObject  stockNum=new JSONObject();
 	    	    
 	    	    stockNum.put("name", "stockNum");
 	    	    stockNum.put("isMust", false);
 	    	    stockNum.put("type", "text");
 	    	    stockNum.put("desc", "股票代码");
 	    	    stockNum.put("maxlength", 30);
 	    	    stockNum.put("value", customer.getStockNum());
 	    	    basicControlList.add(stockNum);
 	    }
 	    
 	    baiscInfoJobj.put("controlList", basicControlList);
 	    moudles.add(baiscInfoJobj);
 	    
 
 	    
 	    
 	   JSONObject briefMoudle=new JSONObject();
 	   briefMoudle.put("level", 0);
 	   
 	   if(templateId!=0){
 		   if(templateId==1){
 			  briefMoudle.put("desc", "企业简介");
 		   }else if(templateId==2||templateId==5){
 			  briefMoudle.put("desc", "单位简介");
 		   }else if(templateId==3){
  			  briefMoudle.put("desc", "学校简介");
  		   }else if(templateId==4){
   			  briefMoudle.put("desc", "医院简介");
   		   }
 		   
 	   }else{
 		   
 		  if(customer.getOrgType()==1||customer.getOrgType()==4){
 	 		   briefMoudle.put("desc", "企业简介");
 	 	   }else{
 	 		   briefMoudle.put("desc", "单位简介");
 	 	   }
 	   }
 	  
 	
 	   briefMoudle.put("isVisible", 1);
 	   briefMoudle.put("name", "brief");
 	   briefMoudle.put("type", "single");
 	   briefMoudle.put("moudleId", 3);
 	    
 	   JSONArray briefControlList=new JSONArray();
 	   JSONObject briefControl=new JSONObject();
 	   
 	   briefControl.put("isMust", false);
 	   briefControl.put("type", "editor");
 	   briefControl.put("desc", briefMoudle.getString("desc"));
 	   briefControl.put("name", "brief");
 	   briefControl.put("value", "");
 	    
 	   briefControlList.add(briefControl);
 	   briefMoudle.put("controlList", briefControlList);
 	   moudles.add(briefMoudle);
 	   
 	   
 	   return moudles;
 
		
		
	}
	
	
	
	
	/**
	 * 初始化老子段
	 * @param customer
	 */
	public static boolean initCustomerOldField(Customer customer){
		JSONArray moudles=customer.getMoudles();
		JSONObject basiInfoMoudle=null;
		JSONObject briefMoudle=null;
		
		try{
			
			for(int i=0;i<moudles.size();i++){
				 JSONObject jsonObject=moudles.getJSONObject(i);
				 if(jsonObject.getInt("moudleId")==1||jsonObject.getInt("moudleId")==2){// 基本信息模块
					 basiInfoMoudle=jsonObject;
				 }
				 
				 if(jsonObject.getInt("moudleId")==3){// 企业简介模块
					 briefMoudle=jsonObject;
				 }
				 if(briefMoudle!=null&&basiInfoMoudle!=null){
					 break;
				 }
			}
			
			if(basiInfoMoudle!=null){
				
				JSONArray controlList=basiInfoMoudle.getJSONArray("controlList");
				for(int i=0;i<controlList.size();i++){
					JSONObject control=controlList.getJSONObject(i);
					System.out.println("control:"+control);
					String name=control.getString("name");
					if("name".equals(name)){
						customer.setOrganAllName(control.getString("value"));
					}else if("shortName".equals(name)){
						customer.setName(control.getString("value"));
						customer.setShotName(control.getString("value"));
					}
					
//					else if("orgType".equals(name)){
//						
//						JSONArray  itemsArray=control.getJSONArray("items");
//						
//						for(int k=0;k<itemsArray.size();k++){
//							
//							JSONObject itemJsonObj=itemsArray.getJSONObject(k);
//							if(itemJsonObj.getBoolean("checked")){
//								
//								customer.setOrgType(itemJsonObj.getInt("value"));
//							}
//						}
//						
//					
//					}
					
					else if("district".equals(name)){
						
						JSONObject valueJo=control.getJSONObject("value");
						String province=valueJo.getString("province");
						String city=valueJo.getString("city");
						String county=valueJo.getString("county");
						
						if(province.equals(city)){
							customer.setAreaString(city+"-"+county);

						}else{
							customer.setAreaString(province+"-"+city+"-"+county);
						}
						
						Area area=new Area();
						area.setCity(city);
						area.setProvince(province);
						area.setCounty(county);
						customer.setArea(area);
					}else if("industry".equals(name)){
						JSONObject valueJo=control.getJSONObject("value");
						customer.setIndustry(valueJo.getString("industry"));
						if("".equals(valueJo.getString("industryId"))){
							customer.setIndustryId(-1);
						}else{
							customer.setIndustryId(Long.parseLong(valueJo.getString("industryId")));
						}
						
					}else if("isListing".equals(name)){
						
							JSONArray  itemsArray=control.getJSONArray("items");
						
							for(int k=0;k<itemsArray.size();k++){
								
								JSONObject itemJsonObj=itemsArray.getJSONObject(k);
								if(itemJsonObj.getBoolean("checked")){
									customer.setIsListing(itemJsonObj.getString("value"));
								}
							}
							
					}else if("stockNum".equals(name)){
						
						customer.setStockNum(control.getString("value"));
					}
				}
				
				
			}

			if(briefMoudle!=null){
				
				JSONArray controlList=briefMoudle.getJSONArray("controlList");
				JSONObject  briefObj=controlList.getJSONObject(0);
				customer.setDiscribe(briefObj.getString("value"));
			}
			
			
			
		}catch(Exception e){
			
			e.printStackTrace();
			return false;
		}
	
		
		long tempalteId=customer.getTemplateId();
		
		if(tempalteId==1){
			customer.setOrgType(1);
		}else if(tempalteId==2){
			customer.setOrgType(2);
		}else if(tempalteId==5){
			customer.setOrgType(3);
		}else {
			customer.setOrgType(4);
		}
			
		return true;
	}
	
	

}
