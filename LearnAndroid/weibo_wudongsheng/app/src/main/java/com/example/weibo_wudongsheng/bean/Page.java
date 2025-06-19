package com.example.weibo_wudongsheng.bean;

import java.util.List;

/**
 * @author wellorbetter
 * HomePageRequest返回的HomePageResponse的Page信息
 */
public class Page <T>{
    private List<T> records;
    private Integer total;
    private Integer size;
    private Integer current;
    private Integer pages;

    public Page(List<T> records, Integer total, Integer size, Integer current, Integer pages) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
