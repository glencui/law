package com.jfzy.web.service.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jfzy.service.AccessTokenService;

@EnableScheduling
@Service
public class AccessTokenStarter {

	private static Logger logger = LoggerFactory.getLogger(AccessTokenStarter.class);

	@Autowired
	private AccessTokenService service;

	@PostConstruct
	public void init() {
		refreshAccessToken();
	}

	@Scheduled(fixedRate = 5000)
	public void refreshAccessToken() {
		service.refreshAccessToken();
	}

}
