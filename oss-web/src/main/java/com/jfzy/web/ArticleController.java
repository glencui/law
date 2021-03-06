package com.jfzy.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.jfzy.base.AuthCheck;
import com.jfzy.service.ArticleService;
import com.jfzy.service.TagService;
import com.jfzy.service.bo.ArticleBo;
import com.jfzy.service.bo.TagBo;
import com.jfzy.web.vo.ArticleVo;
import com.jfzy.web.vo.ResponseStatusEnum;
import com.jfzy.web.vo.ResponseVo;
import com.jfzy.web.vo.SimpleArticleVo;
import com.jfzy.web.vo.TagVo;

@AuthCheck(privileges = { "admin", "editor" })
@RestController
public class ArticleController {

	@Autowired
	private TagService tagService;

	@Autowired
	private ArticleService articleService;

	@ResponseBody
	@GetMapping("/api/article/tag")
	public ResponseVo<List<TagVo>> getTags() {

		List<TagBo> tags = tagService.getAllTags();
		List<TagVo> resultTags = new ArrayList<TagVo>(tags.size());
		for (TagBo bo : tags) {
			resultTags.add(boToVo(bo));
		}

		return new ResponseVo<List<TagVo>>(ResponseStatusEnum.SUCCESS.getCode(), null, resultTags);
	}

	@ResponseBody
	@GetMapping("/api/article/list")
	public ResponseVo<List<SimpleArticleVo>> getArticles(int page, int size) {
		if (page < 0) {
			page = 0;
		}
		Sort sort = new Sort(Direction.DESC, "createTime");
		List<ArticleBo> values = articleService.getArticles(new PageRequest(page, size, sort));
		List<SimpleArticleVo> resultArticles = new ArrayList<SimpleArticleVo>(values.size());
		for (ArticleBo bo : values) {
			resultArticles.add(boToSVo(bo));
		}
		return new ResponseVo<List<SimpleArticleVo>>(ResponseStatusEnum.SUCCESS.getCode(), null, resultArticles);
	}

	@ResponseBody
	@GetMapping("/api/article/listqa")
	public ResponseVo<List<SimpleArticleVo>> getQAs(String tags, int page, int size) {
		if (page < 0) {
			page = 0;
		}
		Sort sort = new Sort(Direction.DESC, "createTime");
		List<ArticleBo> values = articleService.getQAs(new PageRequest(page, size, sort));
		List<SimpleArticleVo> resultArticles = new ArrayList<SimpleArticleVo>(values.size());
		for (ArticleBo bo : values) {
			resultArticles.add(boToSVo(bo));
		}
		return new ResponseVo<List<SimpleArticleVo>>(ResponseStatusEnum.SUCCESS.getCode(), null, resultArticles);
	}

	@ResponseBody
	@PostMapping(path = "/api/article/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseVo<Object> createArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArticleVo vo) {
		/*
		 * try { vo.setTagStr(new
		 * String(vo.getTagStr().getBytes("ISO-8859-1"),"UTF-8"));
		 * vo.setSummary(new
		 * String(vo.getSummary().getBytes("ISO-8859-1"),"UTF-8"));
		 * vo.setContent(new
		 * String(vo.getContent().getBytes("ISO-8859-1"),"UTF-8")); } catch
		 * (UnsupportedEncodingException e) { e.printStackTrace(); }
		 */
		ArticleBo bo = voToBo(vo);
		bo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		articleService.create(bo);
		return new ResponseVo<Object>(ResponseStatusEnum.SUCCESS.getCode(), null, null);
	}

	@ResponseBody
	@GetMapping(value = "/api/article/detail")
	public ResponseVo<ArticleVo> articleDetail(int id) {
		ArticleBo bo = articleService.get(id);
		if (bo != null) {
			return new ResponseVo<ArticleVo>(ResponseStatusEnum.SUCCESS.getCode(), null, boToVo(bo));
		} else {
			return new ResponseVo<ArticleVo>(ResponseStatusEnum.SERVER_ERROR.getCode(), null, null);
		}
	}

	@ResponseBody
	@GetMapping(value = "/api/article/delete")
	public ResponseVo<Object> articleDelete(int id) {
		articleService.delete(id);
		return new ResponseVo<Object>(ResponseStatusEnum.SUCCESS.getCode(), null, null);
	}

	private static ArticleBo voToBo(ArticleVo vo) {
		ArticleBo bo = new ArticleBo();
		BeanUtils.copyProperties(vo, bo);
		bo.setTags(vo.getTagStr().split(","));
		bo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		return bo;
	}

	private static ArticleVo boToVo(ArticleBo bo) {
		ArticleVo vo = new ArticleVo();
		BeanUtils.copyProperties(bo, vo);
		return vo;
	}

	private static SimpleArticleVo boToSVo(ArticleBo bo) {
		SimpleArticleVo vo = new SimpleArticleVo();
		BeanUtils.copyProperties(bo, vo);
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy年MM月dd日");
		vo.setCreateTime(myFmt.format(bo.getCreateTime()));
		if (bo.getUpdateTime() != null) {
			vo.setUpdateTime(myFmt.format(bo.getUpdateTime()));
		}
		return vo;
	}

	private static TagVo boToVo(TagBo bo) {
		TagVo vo = new TagVo();
		BeanUtils.copyProperties(bo, vo);
		return vo;
	}
}
