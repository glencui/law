package com.jfzy.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jfzy.service.OssUserService;
import com.jfzy.service.bo.OssUserBo;
import com.jfzy.service.bo.OssUserTypeEnum;
import com.jfzy.service.exception.JfApplicationRuntimeException;
import com.jfzy.service.exception.JfErrorCodeRuntimeException;
import com.jfzy.service.po.LawyerPo;
import com.jfzy.service.po.OssUserPo;
import com.jfzy.service.repository.LawyerRepository;
import com.jfzy.service.repository.OssUserRepository;

@Service
public class OssUserServiceImpl implements OssUserService {

	@Autowired
	private OssUserRepository ossUserRepo;

	@Autowired
	private LawyerRepository lawyerRepo;

	@Override
	public OssUserBo loginWithPassword(String loginName, String password) {
		String checksum = MD5.MD5Encode(password);

		List<OssUserPo> users = ossUserRepo.findByLoginNameAndPassword(loginName, checksum);
		if (users != null && users.size() == 1) {
			OssUserPo po = users.get(0);
			OssUserBo bo = poToBo(po);
			bo.setPassword("");
			return bo;
		} else {
			return null;
		}
	}

	@Override
	public OssUserBo login(String userName, String password) {
		String checksum = MD5.MD5Encode(password);

		List<OssUserPo> users = ossUserRepo.findByLoginNameAndPassword(userName, checksum);
		List<LawyerPo> lawyers = lawyerRepo.findByLoginNameAndPassword(userName, checksum);

		if (users != null && users.size() == 1) {
			OssUserPo po = users.get(0);
			OssUserBo bo = poToBo(po);
			bo.setType(OssUserTypeEnum.USER.getId());
			return bo;
		} else if (lawyers != null && lawyers.size() == 1) {
			LawyerPo po = lawyers.get(0);
			OssUserBo bo = poToBo(po);
			bo.setRole("lawyer");
			bo.setType(OssUserTypeEnum.LAWYER.getId());
			return bo;
		} else {
			return null;
		}
	}

	@Override
	public List<OssUserBo> getOssUsers(Pageable page) {
		Iterable<OssUserPo> values = ossUserRepo.findAll(page);
		List<OssUserBo> results = new ArrayList<OssUserBo>();
		values.forEach(po -> results.add(poToBo(po)));
		return results;
	}

	@Override
	public void updateStatus(int status, int id) {
		ossUserRepo.updateStatus(status, new Timestamp(System.currentTimeMillis()), id);

	}

	@Override
	public void updateAuth(String role, int id) {
		ossUserRepo.updateAuth(role, new Timestamp(System.currentTimeMillis()), id);
	}

	@Override
	public void create(OssUserBo bo) {
		List<OssUserPo> pos = ossUserRepo.findByPhoneNum(bo.getPhoneNum());

		if (pos != null && pos.size() > 0) {
			throw new JfErrorCodeRuntimeException(400, "用户手机已存在",
					String.format("OSSUSER-CREATE:PhoneNum exists:%s", bo.getPhoneNum()));
		}
		pos = ossUserRepo.findByLoginName(bo.getLoginName());
		if (pos != null && pos.size() > 0) {
			throw new JfErrorCodeRuntimeException(400, "用户名已经存在",
					String.format("OSSUSER-CREATE:Login name exists:%s", bo.getLoginName()));
		}

		bo.setPassword(MD5.MD5Encode(bo.getPassword()));
		OssUserPo po = boToPo(bo);
		ossUserRepo.save(po);
	}

	private static OssUserBo poToBo(OssUserPo po) {
		OssUserBo bo = new OssUserBo();
		BeanUtils.copyProperties(po, bo);
		return bo;
	}

	private static OssUserBo poToBo(LawyerPo po) {
		OssUserBo bo = new OssUserBo();
		BeanUtils.copyProperties(po, bo);
		return bo;
	}

	private static OssUserPo boToPo(OssUserBo bo) {
		OssUserPo po = new OssUserPo();
		BeanUtils.copyProperties(bo, po);
		return po;
	}

	public static void main(String[] args) {
		System.out.println(MD5.MD5Encode("123456789."));
	}

	@Override
	public OssUserBo getUserById(int id) {
		OssUserPo po = ossUserRepo.getOne(id);
		OssUserBo bo = poToBo(po);
		return bo;
	}
}
