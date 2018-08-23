package org.quickstart.elasticsearch.transport.v5.sample.util;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by http://quanke.name on 2017/11/10.
 */
public class Utils {
    public static void println(SearchResponse searchResponse) {
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            System.out.println(JSON.toJSONString(searchHit.getSource(), SerializerFeature.PrettyFormat));
        }
    }

}
