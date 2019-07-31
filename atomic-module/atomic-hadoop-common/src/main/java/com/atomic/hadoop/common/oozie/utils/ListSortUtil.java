package com.atomic.hadoop.common.oozie.utils;

import com.atomic.common.utils.DateFormatUtil;

import java.lang.reflect.Method;
import java.util.Collections;  
import java.util.Comparator;
import java.util.Date;
import java.util.List;  
  
public class ListSortUtil<T> {  
    /** 
     *  
     * @param targetList    要排序的集合（使用泛型） 
     * @param sortField     要排序的集合中的实体类的某个字段 
     * @param sortMode      排序的方式（升序asc/降序desc） 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })    
    public void sort(List<T> targetList, final String sortField, final String sortMode) {    
    //使用集合的sort方法  ，并且自定义一个排序的比较器  
    /** 
     * API文档： 
     * public static <T> void sort(List<T> list,Comparator<? super T> c) 
     * 根据指定比较器产生的顺序对指定列表进行排序。此列表内的所有元素都必须可使用指定比较器 相互比较 
     * （也就是说，对于列表中的任意 e1 和 e2 元素， c.compare(e1, e2) 不得抛出 ClassCastException）。  
     * 参数：     list - 要排序的列表。  
     *      c - 确定列表顺序的比较器。 null 值指示应该使用元素的 自然顺序。  
     */  
     Collections.sort(targetList, new Comparator() {      
            //匿名内部类，重写compare方法   
            public int compare(Object obj1, Object obj2) {     
                int result = 0;    
                try {    
                    //首字母转大写    
                    String newStr = sortField.substring(0, 1).toUpperCase()+sortField.replaceFirst("\\w","");     
                    //获取需要排序字段的“get方法名”  
                    String methodStr = "get"+newStr;    
                    /** API文档：： 
                     *  getMethod(String name, Class<?>... parameterTypes) 
                     *  返回一个 Method 对象，它反映此 Class 对象所表示的类或接口的指定公共成员方法。 
                     */  
                    Method method1 = ((T)obj1).getClass().getMethod(methodStr, null);    
                    Method method2 = ((T)obj2).getClass().getMethod(methodStr, null);    
                    if (sortMode != null && "desc".equals(sortMode)) {    
                        /** 
                         * Method类代表一个方法，invoke（调用）就是调用Method类代表的方法。 
                         * 它可以让你实现动态调用，也可以动态的传入参数。 
                         * API文档：（这个英文解释更地道易懂，原谅我是英文渣,哎！） 
                         * invoke(Object obj, Object... args) 
                         * Invokes the underlying method represented by this Method object, 
                         *  on the specified object with the specified parameters. 
                         */  
                        /** API文档： 
                         * String-----public int compareTo(String anotherString) 
                         * 按字典顺序比较两个字符串。该比较基于字符串中各个字符的 Unicode 值。 
                         * 按字典顺序将此 String 对象表示的字符序列与参数字符串所表示的字符序列进行比较 
                         */  
                        result = method2.invoke(((T) obj2), null).toString()  
                                .compareTo(method1.invoke(((T) obj1), null).toString()); // 倒序    
                    } else {
                        result = method1.invoke(((T) obj1), null).toString()  
                                .compareTo(method2.invoke(((T) obj2), null).toString()); // 正序    
                    }    
                } catch (Exception e) {    
                    throw new RuntimeException();    
                }    
                return result;    
            }    
        });    
    }
    
    /** 
     * 时间比较排序
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })    
    public void sortDate(List<T> targetList, final String sortField, final String sortMode) {    
     Collections.sort(targetList, new Comparator() {      
            @Override
            public int compare(Object obj1, Object obj2) {
                int result = 0;    
                try {    
                    String newStr = sortField.substring(0, 1).toUpperCase()+sortField.replaceFirst("\\w","");     
                    String methodStr = "get"+newStr;    
                    Method method1 = ((T)obj1).getClass().getMethod(methodStr, null);    
                    Method method2 = ((T)obj2).getClass().getMethod(methodStr, null);    
                    if (sortMode != null && "desc".equals(sortMode)) {    
                    	String date1= DateFormatUtil.formatLong19((Date)method2.invoke(((T) obj2)));
                    	String date2=DateFormatUtil.formatLong19((Date)method1.invoke(((T) obj1)));
                    	result=date1.compareTo(date2);
                    } else {
                    	String date2=DateFormatUtil.formatLong19((Date)method2.invoke(((T) obj2)));
                    	String date1=DateFormatUtil.formatLong19((Date)method1.invoke(((T) obj1)));
                    	result = date1.compareTo(date2);
                    }    
                } catch (Exception e) {    
                    throw new RuntimeException();    
                }    
                return result;    
            }    
        });    
    }  
}  
