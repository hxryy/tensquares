package com.tensquare.article.controller;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Article> pageList = articleService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",articleService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param article
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Article article  ){
		articleService.add(article);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param article
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Article article, @PathVariable String id ){
		article.setId(id);
		articleService.update(article);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		articleService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 文章审核
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT,value = "/examine/{id}")
	public Result examine(@PathVariable("id") String id){
		//1.审核文章
		articleService.examine(id);
		//2.创建返回值并返回
		return new Result(true,StatusCode.OK,"审核成功");
	}


	@Autowired
	private RedisTemplate redisTemplate;
	/**
	 * 文章点赞
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT,value = "/thumbup/{id}")
	public Result updateThumbup(@PathVariable("id") String id){
		String userid = "1001";//此时我们还没有用户登录的信息，所以此处的用户id需要写死。等第六天讲完，就可以使用动态获取了
		//判断当前登录的用户是否已经点过赞了，如果点过了就不让再点赞了
		Object value = redisTemplate.opsForValue().get("userid_"+userid+"_articleid_"+id);
		if(value != null){
			throw new IllegalStateException("请不要重复点赞");//非法状态异常
//			return new Result(false, StatusCode.ERROR, "请不要重复点赞");
		}

		//如果没点过赞，那就执行点赞
		//1.点赞
		articleService.updateThumbup(id);
		//把当前用户的点赞信息记录起来，定期清理记录的点赞信息，例如30天之内只能点赞一次
		redisTemplate.opsForValue().set("userid_"+userid+"_articleid_"+id,"thumpup",30, TimeUnit.SECONDS);

		//2.创建返回值并返回
		return new Result(true,StatusCode.OK,"点赞成功");
	}
}

