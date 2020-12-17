package com.leadingsoft.rundata.controller;

import com.leadingsoft.rundata.domain.model.RunDataInDto;
import com.leadingsoft.rundata.domain.model.RunDataOutDto;
import com.leadingsoft.rundata.domain.service.GetRunDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GetRunDataController {


  @Autowired
  private GetRunDataService runDataService;

  @PostMapping("/rundata")
  public Map<String, Object> getRunData(@RequestBody RunDataInDto inDto) {

    Map<String, Object> decodeDataResult = new HashMap<String, Object>();
    String encodeRunData = inDto.getEncodeRunData();
    String jsCode = inDto.getJsCode();
    String iv = inDto.getIv();
    String name = inDto.getName();

    if (StringUtils.isEmpty(encodeRunData)) {
      return null;
    }

    List<RunDataOutDto> decodeRunData = null;
    try {
      decodeRunData = runDataService.decodeRunData(encodeRunData, jsCode, iv, name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    decodeDataResult.put("decodeRunData", decodeRunData);

    return decodeDataResult;

  }

}
