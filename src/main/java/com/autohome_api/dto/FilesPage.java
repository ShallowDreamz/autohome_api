package com.autohome_api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@Getter
@Setter
@ToString
public class FilesPage {
    private int startIndex;     //起始数据页码
    private int totalCount;     //总记录数
    private int pageSize;       //每页显示的数据
    private int totalPage;      //总页数
    private int pageNum;        //当前页数
    private int start ;         //前一页
    private int end ;           //后一页
    private List<FilesPage> list;    //查询对象集合

    // 定义构造方法
    public FilesPage(int pageNum, int pageSize, int totalCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        // 总页数算法
        this.totalPage = (totalCount-1)/pageSize + 1;
        //分页查询的下标
        this.startIndex = (pageNum-1) * pageSize;
        //默认显示5页，总页数小于5页的时候显示总页数
        this.start=1;
        this.end=5;
        if (totalPage <= 5 ) {
            this.end = totalPage;
        }else{
            // 总页数大于5，根据当前页判断start和end
            this.start = pageNum-2;
            this.end = pageNum+2;
            if (start < 0) {
                //如果当前页是第一/二页，就不符合规则
                this.start = 1;
                this.end = 5;
            }
            if (end > this.totalPage) {
                //当前页是第二页或最后一页时候
                this.end = totalPage;
                this.start = end - 5;
            }
        }
    }
}
