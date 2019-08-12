package com.geespace.microservices.deviceservice.server.service.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geespace.microservices.deviceservice.server.dto.message.DeviceLogDto;
import com.geespace.microservices.deviceservice.common.dto.device.DeviceLogSelectForm;
import com.geespace.microservices.deviceservice.common.response.PageResult;
import com.geespace.microservices.deviceservice.common.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zjr
 * @Date: 2019-07-03 16:48
 * @Version 1.0
 */
@Service
@Slf4j
public class EsServiceImpl implements EsService {

    @Override
    public PageResult<DeviceLogDto> selectDeviceLogFromEs(String url, DeviceLogSelectForm deviceLogSelectForm,
                                                          String sort) {
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(deviceLogSelectForm);
        // 创建搜索 DSL 查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();
        int pageNum = deviceLogSelectForm.getPageNum();
        int pageSize = deviceLogSelectForm.getPageSize();
        ResponseEntity<JSONObject> response = HttpUtils.searchDeviceLog(url, searchQuery, pageNum, pageSize, sort);
        PageResult<DeviceLogDto> result = new PageResult<DeviceLogDto>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        List<DeviceLogDto> list = new ArrayList<>();
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            JSONObject responseBody = response.getBody();
            JSONObject hits = responseBody.getJSONObject("hits");
            Long totalCount = hits.getLong("total");
            result.setTotalCount(totalCount);
            int remain = (int) (totalCount % pageSize);
            long totalPage = totalCount / pageSize;
            if (remain > 0) {
                totalPage += 1;
            }
            result.setTotalPage(totalPage);
            JSONArray hitArray = hits.getJSONArray("hits");
            for (int i = 0; i < hitArray.size(); i++) {
                JSONObject hit = hitArray.getJSONObject(i);
                JSONObject source = hit.getJSONObject("_source");
                DeviceLogDto deviceLogDto = JSONObject.toJavaObject(source, DeviceLogDto.class);
                list.add(deviceLogDto);
            }
        } else {
            result.setTotalCount(0);
            result.setTotalPage(0);
        }
        result.setList(list);
        return result;
    }

    @Override
    public void putDeviceLogDto(String url, DeviceLogDto deviceLogDto) {
        JSONObject params = (JSONObject) JSONObject.toJSON(deviceLogDto);

        ResponseEntity<JSONObject> response = HttpUtils.sendPostRequest(url, params);
        if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
            log.error("putDeviceLogDto error======={}", response);
        }
    }

    /**
     * 根据查询条件生成querybuilder
     *
     * @param deviceLogSelectForm
     *            查询条件
     * @return querybuilder
     */
    private BoolQueryBuilder getBoolQueryBuilder(DeviceLogSelectForm deviceLogSelectForm) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchQuery("productKey", deviceLogSelectForm.getProductKey()));
        boolQueryBuilder.must(QueryBuilders.matchQuery("type", deviceLogSelectForm.getType()));
        if (!StringUtils.isEmpty(deviceLogSelectForm.getDeviceName())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("deviceName", deviceLogSelectForm.getDeviceName()));
        }
        if (!StringUtils.isEmpty(deviceLogSelectForm.getKeyWord())) {
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(deviceLogSelectForm.getKeyWord()));
        }
        boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").gte(deviceLogSelectForm.getStartTime())
            .lte(deviceLogSelectForm.getEndTime()));
        return boolQueryBuilder;
    }
}
