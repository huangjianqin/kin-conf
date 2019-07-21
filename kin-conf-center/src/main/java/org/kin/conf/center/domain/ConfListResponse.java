package org.kin.conf.center.domain;

import org.kin.conf.center.entity.Conf;

import java.util.Collections;
import java.util.List;

/**
 * @author huangjianqin
 * @date 2019/7/16
 */
public class ConfListResponse {
    private List<Conf> data = Collections.emptyList();
    private long totalCount;

    //setter && getter
    public List<Conf> getData() {
        return data;
    }

    public void setData(List<Conf> data) {
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
