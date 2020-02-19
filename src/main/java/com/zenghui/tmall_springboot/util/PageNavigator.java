package com.zenghui.tmall_springboot.util;

//spring提供的分页类
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * 分页导航
 */
//泛型类
public class PageNavigator<T>{
    //JPA传递出来的分页对象，PageNavigator类就是对他进行封装以达到扩展的效果
    private Page<T> pageFromJPA;
    //分页的时候，如果总页数比较多，那么显示出来的分页超链一共有几个．如果分页出来的超链接是[8,9,10,11,12]那么navigatePages＝＝５
    private int navigatePages;
    //总页面数
    private int totalPages;
    //第几页（０）
    private int number;
    //总共有多少条数据
    private long totalElements;
    //一页最多有多少条数据
    private int size;
    //当前页有多少条数据（与size不同的是，最后一页可能不满size个）
    private int numberOfElements;
    //数据集合
    private List<T> content;
    //是否有数据
    private boolean isHasContent;
    //是否是首页
    private boolean first;
    //是否是末页
    private boolean last;
    //是否有下一页
    private boolean isHasNext;
    //是否有上一页
    private boolean isHasPrevious;

    //分页的时候，如果总页数比较多，那么显示处理的分页页数　数组
    private int[] navigatePageNums;

    //无参构造器，为Redis从json转换为PageNavigator对象提供的
    public PageNavigator() {

    }


    //传入的参数为JPA 和导航栏的个数,对JPA的封装，和扩展
    public PageNavigator(Page<T> pageFromJPA, int navigatePages) {
        this.pageFromJPA = pageFromJPA;     //这个地方单词打错了，懒得改了，反正已经封装了
        this.navigatePages = navigatePages;

        this.totalPages =pageFromJPA.getTotalPages();
        this.number = pageFromJPA.getNumber();
        this.totalElements = pageFromJPA.getTotalElements();
        this.size = pageFromJPA.getSize();
        this.numberOfElements = pageFromJPA.getNumberOfElements();
        this.content = pageFromJPA.getContent();
        this.isHasContent = pageFromJPA.hasContent();
        this.first = pageFromJPA.isFirst();
        this.last = pageFromJPA.isLast();
        this.isHasNext = pageFromJPA.hasNext();
        this.isHasPrevious = pageFromJPA.hasPrevious();

        //生成页码数组的函数
        calcNavigatePageNums();
    }

    private void calcNavigatePageNums(){
        int[] navigatePageNums;//页帧数组
        int totalPages = getTotalPages();
        int num = getNumber();

        //当总页数小于或等于导航页码数时，[0,1,2,3,4]
        if (totalPages <= navigatePages){
            navigatePageNums = new int[totalPages];
            for (int i=0;i<totalPages;i++){
                navigatePageNums[i] = i+1;
            }
        }else {//当总页数大于导航页码数时
            navigatePageNums = new  int[navigatePages];
            //计算导航开始页数和结束页数
            int startNum = num -navigatePages/2;    //[4,5,6,7,8], 4 = 6 - 5/2;  [2,3,4,5,6,7,8] 2=5-7/2
            int endNum = num + navigatePages/2;  //8 = 6+5/2;

            //导航第一页小于1时
            if(startNum < 1){
                startNum = 1;
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNums[i] = startNum++;
                }
            }else if (endNum > totalPages){ //导航栏最后大于最后一页时
                endNum = totalPages;
                //倒过来，从最后传导航栏的值
                for (int i = navigatePages-1; i >= 0; i--) {
                    navigatePageNums[i] = endNum--;
                }
            }else {     //属于中间的部分，直接从头加就行
                startNum = startNum+1;
                for (int i = 0; i < navigatePages; i++) {
                    System.out.println(i+"-"+startNum);
                    navigatePageNums[i] = startNum++;
                }
            }
        }
//        setNavigatePageNums(navigatePageNums);
        this.navigatePageNums = navigatePageNums;
    }



//       这个东西redis序列化时,要删除,我也不知道为什么
//    public Page<T> getPageFromJPA() {
//        return pageFromJPA;
//    }
//
//    public void setPageFromJPA(Page<T> pageFromJPA) {
//        this.pageFromJPA = pageFromJPA;
//    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isHasContent() {
        return isHasContent;
    }

    public void setHasContent(boolean hasContent) {
        isHasContent = hasContent;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isHasNext() {
        return isHasNext;
    }

    public void setHasNext(boolean hasNext) {
        isHasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return isHasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        isHasPrevious = hasPrevious;
    }

    public int[] getNavigatePageNums() {
        return navigatePageNums;
    }

    public void setNavigatePageNums(int[] navigatePageNums) {
        this.navigatePageNums = navigatePageNums;
    }

}
