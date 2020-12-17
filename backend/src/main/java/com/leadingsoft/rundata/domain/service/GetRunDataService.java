package com.leadingsoft.rundata.domain.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadingsoft.rundata.domain.entity.RunDataEntity;
import com.leadingsoft.rundata.domain.model.RunDataInDto;
import com.leadingsoft.rundata.domain.model.RunDataOutDto;
import com.leadingsoft.rundata.domain.repository.RunDataRepository;
import com.leadingsoft.rundata.utils.DateUtil;
import com.leadingsoft.rundata.utils.DecodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GetRunDataService {

  private static String APP_ID = "wx5e8d7972ab8c10c1";
  private static String APP_SECRET = "61de282495ea644bfd40d6f625e5d0ea";

  private static String REQ_SESSION_KEY_URL = "https://api.weixin.qq.com/sns/jscode2session?";

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RunDataRepository runDataRepository;

  public List<RunDataOutDto> decodeRunData(String encodeData, String jsCode, String iv, String name) throws Exception {

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

    Object dataObj = stepList.get(0);
    RunDataOutDto outDto = new RunDataOutDto();
    for (Object entry : ((Map) dataObj).entrySet()) {
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

    outDto.setUserName(name);
    ////stepMapList.add(outDto);

    // 保存至DB
    saveRunData(outDto);
    //从DB取出数据
    getRunData(stepMapList);
    
    
    	
//    for (Object mapList : stepList) {
//
//      RunDataOutDto outDto = new RunDataOutDto();
//
//      for (Object entry : ((Map) mapList).entrySet()) {
//        String key = (String) ((Map.Entry) entry).getKey();
//        Integer val = (Integer) ((Map.Entry) entry).getValue();
//        String valStr = String.valueOf(val);
//
//        if (StringUtils.equals(key, "timestamp")) {
//          valStr = DateUtil.formatTimestampVal(val, DateUtil.TIME_FORMAT_YYYYMMDD);
//          outDto.setRunDate(valStr);
//        } else if (StringUtils.equals(key, "step")) {
//          outDto.setRunStep(valStr);
//        }
//      }
//
//      outDto.setUserName(name);
//      
//      stepMapList.add(outDto);
//      stepMapList.sort(new Comparator<RunDataOutDto>() {
//        @Override
//        public int compare(RunDataOutDto o1, RunDataOutDto o2) {
//          return o2.getRunDate().compareTo(o1.getRunDate());
//        }
//      });
//    }
      return stepMapList;
    }

    private void saveRunData(RunDataOutDto outDto) {
      RunDataEntity entity = new RunDataEntity();
      entity.setUserName(outDto.getUserName());
      entity.setStatus("A");
      entity.setRunDate(DateUtil.formatDateStringVal(outDto.getRunDate()));
      entity.setRunSteps(Integer.parseInt(outDto.getRunStep()));
      //runDataRepository.save(entity);
      if (runDataRepository.existsById(entity.getUserName())) {//用户存在的情况先删除再插入
    	  runDataRepository.deleteById(entity.getUserName());
    	  runDataRepository.save(entity);
      }else {//不存在的情况下直接插入
    	  runDataRepository.save(entity);
      }
    }
    
    private void getRunData(List<RunDataOutDto> stepMapList) {
        RunDataEntity entity = new RunDataEntity();
        Date date = new Date(); // 今日の日付
        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd");//设置当天日期格式
        String eventDate = today.format(date);
//        System.out.print(eventDate);
//        String eventName = "Ba";
//        System.out.print(eventName);
        
        
        //List<RunDataEntity> dbDataList = runDataRepository.findAll(Sort.by(Sort.Direction.DESC,("runSteps")));//从DB取值按步数降序排列
        List<RunDataEntity> dbDataList = runDataRepository.findByMatchMonthAndMatchDay(eventDate);//取出当日的数据
        System.out.print(dbDataList);
        for(RunDataEntity data : dbDataList) {
        	RunDataOutDto outDto = new RunDataOutDto();
        	outDto.setRunDate(data.getRunDate().toString().substring(0, 11));
        	outDto.setUserName(data.getUserName());
        	outDto.setRunStep(Integer.toString(data.getRunSteps()));
        	stepMapList.add(outDto);
        }
      }


    @Bean
    public RestTemplate restTemplate () {
      SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
      factory.setReadTimeout(5000);//5秒
      factory.setConnectTimeout(15000);//15秒
      return new RestTemplate(factory);
    }
  }
