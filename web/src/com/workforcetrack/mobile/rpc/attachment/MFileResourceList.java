package com.workforcetrack.mobile.rpc.attachment;

import com.workforcetrack.api.base.APIRepresentation;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 02.05.12
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MFileResourceList implements APIRepresentation {

    public static final String TOTAL_COUNT = "totalCount";
    public static final String ITEMS = "items";

    private Integer totalCount;

    private List<MFileResource> file;

    public MFileResourceList() {
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MFileResource> getFile() {
        return file;
    }

    public void setFile(List<MFileResource> file) {
        this.file = file;
    }

    @Override
    public Map<String, Object> getAsMap(String... ignoreFields) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(TOTAL_COUNT, getTotalCount());
        resultMap.put(ITEMS, getFile());
        return resultMap;
    }

    @Override
    public List<String> getFieldsName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
