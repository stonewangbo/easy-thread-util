/*
 * 宜人贷
 * FindBatchErrorUtils.java
 * Copyright (c) 2012 普信恒业科技发展(北京)有限公司
 * All rights reserved.
 * --------------------------
 * Oct 21, 2015 Created
 */
package pers.stone.utils.count;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 类名: FindBatchErrorUtils <br/>
 * 用途: 快速查找批量操作时的错误 <br/>
 * 
 * @author wangbo <br/>
 *         Oct 21, 2015 11:02:20 AM
 */
public class FindBatchErrorUtils {
    /**
     * find error in list when do opter
     * @param list
     * @param opter to deal with list
     * @param run methonName to deal
     * @return
     * @throws Exception
     */
    public static <E> E  findError(List<E> list,Object opter,String methonName) throws Exception  {
        Method method;
        if(list==null || list.size()==0){
            throw new Exception("list is empty");
        }
        try {    
            method = opter.getClass().getMethod(methonName, List.class);
        } catch (Exception e) {
           throw new Exception("can not find methonName:"+methonName+" in opter:"+opter,e);
        } 
        return find(list,opter,method);
    }
    
    private static <E> E find(List<E> infoList,Object opter,Method method){
        List<List<E>> list = new ArrayList<List<E>>();
        if(infoList.size()>1){
            list.add(infoList.subList(0, infoList.size()/2));
            list.add(infoList.subList(infoList.size()/2, infoList.size()));
        }else{
            if(infoList.size()==1){               
                try{
                    method.invoke(opter, infoList);
                }catch(Exception e){
                    return infoList.iterator().next();
                }
            }
        }
        
        for(List<E> sublist:list){
            if(sublist!=null && sublist.size()>1){
                try{
                    method.invoke(opter, sublist);
                }catch(Exception e){
                    find(sublist,opter,method);
                }
            }else if(sublist!=null && sublist.size()==1){
                find(sublist,opter,method);
            }          
        }
        return null;
    }
}
