package com.tensquare.qa.controller;
import com.tensquare.qa.client.LabelClient;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

	@Autowired
	private ProblemService problemService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findById(id));
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
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",problemService.findSearch(searchMap));
    }
	
	/**
	 * 增加（发布问题，需要用户权限）
	 * @param problem
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Problem problem, HttpServletRequest request){
		//1.获取请求域的用户权限信息
		Claims claims = (Claims) request.getAttribute("user_claims");
		if(claims == null){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限");
		}
		problemService.add(problem);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param problem
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Problem problem, @PathVariable String id ){
		problem.setId(id);
		problemService.update(problem);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		problemService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 最新回答的问题列表
	 * @param labelid
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/newlist/{labelid}/{page}/{size}")
	public Result newlist(@PathVariable("labelid") String labelid,@PathVariable("page") int page,@PathVariable("size") int size){
		//1.调用业务层查询
		Page<Problem> problemPage = problemService.findNewList(labelid,page,size);
		//2.创建自定义分页返回值对象
		PageResult<Problem> pageResult = new PageResult<>(problemPage.getTotalElements(),problemPage.getContent());
		//3.创建返回值并返回
		return new Result(true,StatusCode.OK,"查询成功",pageResult);
	}

	/**
	 * 热门回答的问题列表
	 * @param labelid
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/hotlist/{labelid}/{page}/{size}")
	public Result hotlist(@PathVariable("labelid") String labelid,@PathVariable("page") int page,@PathVariable("size") int size){
		//1.调用业务层查询
		Page<Problem> problemPage = problemService.findHotList(labelid,page,size);
		//2.创建自定义分页返回值对象
		PageResult<Problem> pageResult = new PageResult<>(problemPage.getTotalElements(),problemPage.getContent());
		//3.创建返回值并返回
		return new Result(true,StatusCode.OK,"查询成功",pageResult);
	}

	/**
	 * 等待回答的问题列表
	 * @param labelid
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/waitlist/{labelid}/{page}/{size}")
	public Result waitlist(@PathVariable("labelid") String labelid,@PathVariable("page") int page,@PathVariable("size") int size){
		//1.调用业务层查询
		Page<Problem> problemPage = problemService.findWaitList(labelid,page,size);
		//2.创建自定义分页返回值对象
		PageResult<Problem> pageResult = new PageResult<>(problemPage.getTotalElements(),problemPage.getContent());
		//3.创建返回值并返回
		return new Result(true,StatusCode.OK,"查询成功",pageResult);
	}


	@Autowired
	private LabelClient labelClient;
	/**
	 * 根据标签id查询标签
	 * @param labelid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/label/{labelid}")
	public Result findByLabelid(@PathVariable("labelid") String labelid){
		return labelClient.findById(labelid);
	}
}
