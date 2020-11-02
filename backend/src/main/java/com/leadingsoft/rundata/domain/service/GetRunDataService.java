package com.leadingsoft.rundata.domain.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadingsoft.rundata.domain.model.RunDataOutDto;
import com.leadingsoft.rundata.utils.DateUtil;
import com.leadingsoft.rundata.utils.DecodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
public class GetRunDataService {

  private static String APP_ID = "wxff58170d9ec04acb";
  private static String APP_SECRET = "7d387e4ae361d1cbbc9a3c97f556bad2";

  private static String REQ_SESSION_KEY_URL = "https://api.weixin.qq.com/sns/jscode2session?";

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  public List<RunDataOutDto> decodeRunData(String encodeData, String jsCode, String iv) throws Exception {

    List<RunDataOutDto> stepMapList = new ArrayList<>();

    System.out.println("------encodeData-------");
    System.out.println(encodeData);
//    System.out.println(jsCode);

    StringBuilder sb = new StringBuilder(REQ_SESSION_KEY_URL);
    sb.append("appid=").append(APP_ID);
    sb.append("&secret=").append(APP_SECRET);
    sb.append("&js_code=").append(jsCode);
    sb.append("&grant_type=").append("authorization_code");

    ResponseEntity<String> response = restTemplate.getForEntity(sb.toString(), String.class);
    String responseData = response.getBody();
    System.out.println(responseData);


    Map<String, Object> userBaseMap = null;
    try {
      userBaseMap = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {
      });
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    String sessionKey = (String) userBaseMap.get("session_key");
    String result = DecodeUtil.onDecrypt(encodeData, sessionKey, iv);

    JSONObject userInfoJSON = JSON.parseObject(result);
    System.out.println(userInfoJSON);
    JSONArray stepList = userInfoJSON.getJSONArray("stepInfoList");


    for (Object mapList : stepList) {

      RunDataOutDto outDto = new RunDataOutDto();

      for (Object entry : ((Map) mapList).entrySet()) {
        String key = (String) ((Map.Entry) entry).getKey();
        Integer val = (Integer) ((Map.Entry) entry).getValue();
        String valStr = String.valueOf(val);

        if (StringUtils.equals(key, "timestamp")) {
          valStr = DateUtil.formatTimestampVal(val, DateUtil.TIME_FORMAT_YYYYMMDD);
          outDto.setRunDate(valStr);
        } else if (StringUtils.equals(key, "step")) {
          outDto.setRunStep(valStr);
        }
      }

      stepMapList.add(outDto);
      stepMapList.sort(new Comparator<RunDataOutDto>() {
        @Override
        public int compare(RunDataOutDto o1, RunDataOutDto o2) {
          return o2.getRunDate().compareTo(o1.getRunDate());
        }
      });
    }

      return stepMapList;
    }


    @Bean
    public RestTemplate restTemplate () {
      SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
      factory.setReadTimeout(5000);//5秒
      factory.setConnectTimeout(15000);//15秒
      return new RestTemplate(factory);
    }
  }
