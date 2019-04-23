package com.lion.canvas.path.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.*;

/**
 * @author
 * @date 2019/3/13
 */
@Data
public class PageResultBean extends ResultBean {
    private int size;
    private int pages;
    private int curPage;

    private void pageResultInit(Page data){
        pages = data.getTotalPages();
        size = data.getSize();
        curPage = data.getNumber();
    }
    public PageResultBean(Page data) {
        this.pageResultInit(data);
        this.setData(data.getContent());
    }
    public PageResultBean(Page data, boolean mapLowerKey) {
        this.pageResultInit(data);
        if (mapLowerKey){
            List<Map<String, Object>> res = new ArrayList<>();
            data.getContent().stream().forEach(map -> res.add(transformUpperCase((Map<String, Object>)map)));
            this.setData(res);
        }else{
            this.setData(data.getContent());
        }
    }
    public static Map<String, Object> transformUpperCase(Map<String, Object> orgMap) {
        Map<String, Object> resultMap = new HashMap<>();
        if (orgMap == null || orgMap.isEmpty()) {
            return resultMap;
        }
        Set<String> keySet = orgMap.keySet();
        for (String key : keySet) {
            String newKey = key.toLowerCase();
            resultMap.put(newKey, orgMap.get(key));
        }
        return resultMap;
    }

    public static PageResultBean success(Page data){
        return new PageResultBean(data);
    }
    public static PageResultBean success(Page data, boolean mapLowerKey){
        return new PageResultBean(data, mapLowerKey);
    }
}
