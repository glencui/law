package com.jfzy.mweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jfzy.mweb.util.ResponseStatusEnum;
import com.jfzy.mweb.vo.PropertyVo;
import com.jfzy.mweb.vo.ResponseVo;
import com.jfzy.mweb.vo.SimpleResponseVo;
import com.jfzy.service.OrderRoleService;
import com.jfzy.service.ProductService;
import com.jfzy.service.PropertyService;
import com.jfzy.service.QAService;
import com.jfzy.service.bo.PropertyBo;
import com.jfzy.service.bo.PropertyTypeEnum;
import com.jfzy.service.bo.QABo;
import com.jfzy.service.bo.QAStatusEnum;

@RestController
public class AskController {

	@Autowired
	private PropertyService propService;

	@Autowired
	private OrderRoleService roleService;
	
	@Autowired
	private QAService qaService;
	
	@Autowired
	private ProductService productService;

	@ResponseBody
	@GetMapping("/ask/props")
	public ResponseVo<List<Object>> getProps() {
		List<PropertyBo> roleProps = propService.getPropertyByType(PropertyTypeEnum.ROLE.getId());
		List<PropertyBo> signProps = propService.getPropertyByType(PropertyTypeEnum.SIGN.getId());

		List<PropertyVo> roleResult = new ArrayList<PropertyVo>(roleProps.size());
		for (PropertyBo bo : roleProps) {
			roleResult.add(boToVo(bo));
		}
		List<PropertyVo> signResult = new ArrayList<PropertyVo>(signProps.size());
		for (PropertyBo bo : signProps) {
			signResult.add(boToVo(bo));
		}

		List<Object> results = new ArrayList<Object>(3);
		results.add(roleResult);
		results.add(signResult);
		results.add(roleService.getAllRoles());
		results.add(productService.getProducts());
		return new ResponseVo<List<Object>>(ResponseStatusEnum.SUCCESS.getCode(), null, results);

	}

	@ResponseBody
	@PostMapping("/ask")
	public SimpleResponseVo createQA(String role, String phase, String questionDetail) {
		QABo bo = new QABo();
		bo.setCityId(0);// FIXME
		bo.setContent(questionDetail);
		bo.setPhase(phase);
		bo.setRole(role);
		bo.setStatus(QAStatusEnum.UNPROCESSED.getId());
		bo.setUserId(0);// FIXME
		bo.setUserRealName("");// FIXME
		qaService.createQA(bo);

		return new SimpleResponseVo(ResponseStatusEnum.SUCCESS.getCode(), null);
	}

	private static PropertyVo boToVo(PropertyBo bo) {
		PropertyVo vo = new PropertyVo();
		vo.setId(bo.getId());
		vo.setType(bo.getType());
		vo.setName(bo.getName());
		return vo;
	}

}
