package com.jfzy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jfzf.core.Utils;
import com.jfzy.service.SmsService;
import com.jfzy.web.vo.ResponseStatusEnum;
import com.jfzy.web.vo.ResponseVo;

@RestController
public class SSMController {
	
	@Autowired
	private SmsService smsService;
	
	@ResponseBody
	@GetMapping("/api/ssm/code")
	public ResponseVo<String> getCode(String phoneNum) {
		smsService.sendRegisterCode(phoneNum, Utils.getSmsCode(4));
		return new ResponseVo<String>(ResponseStatusEnum.SUCCESS.getCode(), "发送成功", null);
	}
}