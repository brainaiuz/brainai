package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.util.List;

/**
  * Created by Abdurakhmonov Farrukh on 3/14/2018.
 **/
public class EntityCategoryTO extends ResponseData {
    private Integer total_count;
    private Integer offset;
    private Integer count;
    private Integer left;
    private List<CategoryTO> list;

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public List<CategoryTO> getList() {
        return list;
    }

    public void setList(List<CategoryTO> list) {
        this.list = list;
    }
}
