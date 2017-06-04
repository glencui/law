package com.jfzy.mweb.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jfzf.core.Constants;
import com.jfzy.mweb.base.BaseController;
import com.jfzy.mweb.base.UserSession;
import com.jfzy.mweb.util.ResponseStatusEnum;
import com.jfzy.mweb.vo.InvestOrderVo;
import com.jfzy.mweb.vo.OrderCompleteVo;
import com.jfzy.mweb.vo.OrderVo;
import com.jfzy.mweb.vo.PrepayVo;
import com.jfzy.mweb.vo.ResponseVo;
import com.jfzy.mweb.vo.SearchOrderVo;
import com.jfzy.mweb.vo.WxPayCallbackDto;
import com.jfzy.mweb.vo.WxPayCallbackRespDto;
import com.jfzy.service.OrderService;
import com.jfzy.service.ProductService;
import com.jfzy.service.UserService;
import com.jfzy.service.bo.OrderBo;
import com.jfzy.service.bo.OrderStatusEnum;
import com.jfzy.service.bo.ProductBo;
import com.jfzy.service.bo.UserAccountBo;
import com.jfzy.service.bo.UserAccountTypeEnum;
import com.jfzy.service.bo.UserBo;
import com.jfzy.service.bo.WxPayEventBo;
import com.jfzy.service.bo.WxPayResponseDto;
import com.jfzy.service.impl.Signature;
import com.jfzy.service.impl.XmlUtil;

@RestController
public class OrderController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@ResponseBody
	@PostMapping(path = "/api/order/screate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseVo<OrderBo> createSOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SearchOrderVo vo) {
		OrderBo bo = svoToBo(vo);
		bo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		ProductBo pbo = productService.getProduct(vo.getProductId());
		UserBo ubo = userService.getUser(vo.getUserId());
		UserAccountBo uabo = userService.getUserAccountByUserId(vo.getUserId(), UserAccountTypeEnum.MOBILE.getId());
		bo.setProductName(pbo.getName());
		bo.setProductCode(pbo.getCode());
		bo.setRealPrice(pbo.getPrice());
		bo.setOriginPrice(pbo.getPrice());
		bo.setUserName(ubo.getRealName());
		bo.setUserPhoneNum(uabo.getValue());
		OrderBo newBo = orderService.createSOrder(bo);
		return new ResponseVo<OrderBo>(ResponseStatusEnum.SUCCESS.getCode(), null, newBo);
	}

	@ResponseBody
	@PostMapping(path = "/api/order/icreate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseVo<OrderBo> createIOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody InvestOrderVo vo) {
		OrderBo bo = ivoToBo(vo);
		bo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		ProductBo pbo = productService.getProduct(vo.getProductId());
		UserBo ubo = userService.getUser(vo.getUserId());
		UserAccountBo uabo = userService.getUserAccountByUserId(vo.getUserId(), UserAccountTypeEnum.MOBILE.getId());
		bo.setProductName(pbo.getName());
		bo.setProductCode(pbo.getCode());
		bo.setRealPrice(pbo.getPrice());
		bo.setOriginPrice(pbo.getPrice());
		bo.setUserName(ubo.getRealName());
		bo.setUserPhoneNum(uabo.getValue());
		OrderBo newBo = orderService.createIOrder(bo);
		return new ResponseVo<OrderBo>(ResponseStatusEnum.SUCCESS.getCode(), null, newBo);
	}

	@ResponseBody
	@PostMapping(value = "/api/wxpay/callback", produces = { "application/xml" })
	public WxPayCallbackRespDto callback(@RequestBody String reqStr) {
		logger.error(reqStr);
		WxPayCallbackDto dto = (WxPayCallbackDto) XmlUtil.fromXml(reqStr, WxPayCallbackDto.class);
		WxPayEventBo event = dtoToBo(dto);
		// check sign
		if (checkSign(dto)) {
			UserAccountBo user = userService.getUserAccountByOpenid(event.getOpenId());
			if (user == null) {
				logger.error("could not find openid");
			} else {
				orderService.markPayed(event, user.getUserId());
			}
		} else {
			logger.error("check sign failed");
		}

		WxPayCallbackRespDto result = new WxPayCallbackRespDto();
		result.setReturnCode("SUCCESS");
		result.setReturnMsg("OK");
		return result;
	}

	private static WxPayEventBo dtoToBo(WxPayCallbackDto dto) {
		WxPayEventBo bo = new WxPayEventBo();
		BeanUtils.copyProperties(dto, bo);
		return bo;
	}

	@ResponseBody
	@GetMapping(value = "/api/order/pay")
	public ResponseVo<PrepayVo> pay(int id) {
		UserSession session = getUserSession();

		if (session != null) {
			String openId = session.getOpenId();
			if (StringUtils.isNotBlank(openId) || UserSession.EMPTY_USER_ID != session.getUserId()) {
				WxPayResponseDto dto = orderService.pay(id, session.getUserId(), getClientIp(), openId);
				if (StringUtils.equals("SUCCESS", dto.getReturnCode())
						&& StringUtils.equals("SUCCESS", dto.getResultCode())) {
					PrepayVo vo = dtoToVo(dto);
					return new ResponseVo<PrepayVo>(ResponseStatusEnum.SUCCESS.getCode(), null, vo);
				} else {
					return new ResponseVo<PrepayVo>(ResponseStatusEnum.BAD_REQUEST.getCode(), "微信支付单生成失败", null);
				}
			}
		}

		return new ResponseVo<PrepayVo>(ResponseStatusEnum.BAD_REQUEST.getCode(), "未登录", null);

	}

	@ResponseBody
	@GetMapping(value = "/api/order/cancel")
	public ResponseVo<Object> cancel(int id) {
		orderService.cancel(id);
		return new ResponseVo<Object>(ResponseStatusEnum.SUCCESS.getCode(), null, null);
	}

	@ResponseBody
	@PostMapping(value = "/api/order/complete", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseVo<Object> complete(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OrderCompleteVo vo) {
		orderService.complete(vo.getId(), vo.getComment(), vo.getPicList());
		return new ResponseVo<Object>(ResponseStatusEnum.SUCCESS.getCode(), null, null);
	}

	@ResponseBody
	@GetMapping("/api/order/listbyuser")
	public ResponseVo<List<OrderVo>> getOrders(int userId, int page, int size) {
		if (this.request != null && this.request.getRequestedSessionId() != null) {
			logger.error(
					String.format("======Session id for user %s is %s", userId, this.request.getRequestedSessionId()));
		}

		if (page < 0) {
			page = 0;
		}
		Sort sort = new Sort(Direction.DESC, "createTime");
		List<OrderBo> values = orderService.getOrdersByUser(userId, new PageRequest(page, size, sort));
		List<OrderVo> result = new ArrayList<OrderVo>(values.size());
		for (OrderBo bo : values) {
			result.add(boToVo(bo));
		}
		return new ResponseVo<List<OrderVo>>(ResponseStatusEnum.SUCCESS.getCode(), null, result);
	}

	private static OrderBo svoToBo(SearchOrderVo vo) {
		OrderBo bo = new OrderBo();
		BeanUtils.copyProperties(vo, bo);
		return bo;
	}

	private static OrderBo ivoToBo(InvestOrderVo vo) {
		OrderBo bo = new OrderBo();
		BeanUtils.copyProperties(vo, bo);
		return bo;
	}

	private static OrderVo boToVo(OrderBo bo) {
		OrderVo vo = new OrderVo();
		BeanUtils.copyProperties(bo, vo);
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyyMMdd");
		if (bo.getCreateTime() != null)
			vo.setCreateTime(myFmt.format(bo.getCreateTime()));
		// 订单编号：时间+code+id
		vo.setOrderCode(myFmt2.format(bo.getCreateTime()) + bo.getProductCode() + bo.getId());
		if (bo.getUpdateTime() != null)
			vo.setUpdateTime(myFmt.format(bo.getUpdateTime()));
		if (bo.getStartTime() != null)
			vo.setStartTime(myFmt.format(bo.getStartTime()));
		if (bo.getEndTime() != null)
			vo.setEndTime(myFmt.format(bo.getEndTime()));
		if (bo.getStartTime() != null && bo.getEndTime() != null) {
			if (new Date().getTime() >= bo.getEndTime().getTime()
					|| bo.getStatus() == OrderStatusEnum.FINISHED.getId()) {
				if (bo.getPhoneEndTime() != null)
					vo.setPhoneEndTime(myFmt.format(bo.getPhoneEndTime()));
				if (bo.getStartTime() != null && bo.getEndTime() != null) {
					if (new Date().getTime() >= bo.getEndTime().getTime()
							|| bo.getStatus() == OrderStatusEnum.FINISHED.getId()) {
						vo.setProcessPer("100%");
					} else if (new Date().getTime() > bo.getStartTime().getTime()
							&& new Date().getTime() < bo.getEndTime().getTime()) {
						vo.setProcessPer(Math.round((new Date().getTime() - bo.getStartTime().getTime()) * 100
								/ (bo.getEndTime().getTime() - bo.getStartTime().getTime())) + "%");
					} else {
						vo.setProcessPer("0%");
					}
				}

			}
		}
		return vo;
	}

	private static PrepayVo dtoToVo(WxPayResponseDto dto) {
		PrepayVo vo = new PrepayVo();
		vo.setAppId(dto.getAppId());
		vo.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
		vo.setNonceStr(dto.getNonceStr());
		vo.setPkg(String.format("prepay_id=%s", dto.getPrepayId()));
		vo.setPaySign(Signature.getSign(toh5ParamMap(vo), Constants.PAY_SECRET));
		return vo;
	}

	private static Map<String, String> toh5ParamMap(PrepayVo vo) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", vo.getAppId());
		paramMap.put("timeStamp", vo.getTimestamp());
		paramMap.put("nonceStr", vo.getNonceStr());
		paramMap.put("package", vo.getPkg());
		paramMap.put("signType", vo.getSignType());
		return paramMap;
	}

	private static boolean checkSign(WxPayCallbackDto dto) {
		Map<String, String> paramMap = toCallbackParamMap(dto);
		return Signature.checkSign(paramMap, Constants.PAY_SECRET, dto.getSign());
	}

	private static Map<String, String> toCallbackParamMap(WxPayCallbackDto dto) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("return_code", dto.getReturnCode());
		paramMap.put("return_msg", dto.getReturnMsg());
		paramMap.put("appid", dto.getAppId());
		paramMap.put("mch_id", dto.getMchId());
		paramMap.put("device_info", dto.getDeviceInfo());
		paramMap.put("nonce_str", dto.getNonceStr());
		paramMap.put("sign_type", dto.getSignType());
		paramMap.put("result_code", dto.getResultCode());
		paramMap.put("err_code", dto.getErrCode());
		paramMap.put("err_code_des", dto.getReturnMsg());
		paramMap.put("openid", dto.getOpenId());
		paramMap.put("is_subscribe", dto.getIsSubscribe());
		paramMap.put("trade_type", dto.getTradeType());
		paramMap.put("bank_type", dto.getBankType());
		paramMap.put("total_fee", dto.getTotalFee());
		paramMap.put("settlement_total_fee", dto.getSettlementTotalFee());
		paramMap.put("fee_type", dto.getFeeType());
		paramMap.put("transaction_id", dto.getTransactionId());
		paramMap.put("out_trade_no", dto.getOutTradeNo());
		paramMap.put("attach", dto.getAttach());
		paramMap.put("time_end", dto.getTimeEnd());
		paramMap.put("cash_fee", dto.getCashFee());

		return paramMap;
	}
}