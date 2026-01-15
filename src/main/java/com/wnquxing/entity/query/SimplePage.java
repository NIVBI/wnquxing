package com.wnquxing.entity.query;

import com.wnquxing.entity.enums.PageSize;

public class SimplePage {
    private int pageNo;
    private Integer countTotal;
    private Integer pageSize;
    private Integer pageTotal;
    private int start;
    private int end;

    public SimplePage() {
        action();
    }

    public SimplePage(Integer pageNo, Integer countTotal, Integer pageSize) {
        if (pageNo == null) {
            pageNo = 0;
        }
        this.pageNo = pageNo;
        this.countTotal = countTotal;
        this.pageSize = pageSize;
        action();
    }

    public SimplePage(int start, int end) {
        this.start = start;
        this.end = end;
        action();
    }

    public void action() {
        if (this.pageSize <= 0) {
            this.pageSize = PageSize.SIZE20.getSize();
        }
        if (this.countTotal > 0) {
            this.pageTotal = this.countTotal % this.pageSize == 0 ?
                this.countTotal / this.pageSize :
                this.countTotal / this.pageSize + 1;
        } else {
            pageTotal = 1;
        }
        if (pageNo <= 1) {
            pageNo = 1;
        }
        if (pageNo > pageTotal) {
            pageNo = pageTotal;
        }
        this.start = (pageNo - 1) * pageSize;
        this.end = this.start + pageSize;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Integer getCountTotal() {
        return countTotal;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
