package com.ginkgocap.parasol.knowledge.form;

import java.io.Serializable;
import java.util.List;
/**
 * node节点 
 * <p>于2014-10-10 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */
public class SubcribeNode implements Serializable{
    /**    
     * serialVersionUID <p>TODO（用一句话描述这个变量表示什么）</p>    
     *     
     */    
    
    private static final long serialVersionUID = 1L;
    private long id;
    private long count;
    private String state;
    private String name;
    private List<SubcribeNode> list;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
  
    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<SubcribeNode> getList() {
        return list;
    }
    public void setList(List<SubcribeNode> list) {
        this.list = list;
    }
   
    
}
