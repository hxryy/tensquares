package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
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
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 删除，需要管理员权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id, HttpServletRequest request){
		//1.获取管理员权限
		Claims claims = (Claims)request.getAttribute("admin_claims");
		if(claims == null){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 删除
	 * @param id

	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id,@RequestHeader(value="Authorization",required = false) String header){
		//1.判断是否有此消息头
		if(StringUtils.isEmpty(header)){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限，没有此消息头");
		}
		//2.判断消息头是否按照格式书写
		if(!header.startsWith("Bearer ")){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限，消息头格式不对");
		}
		//3.取出消息头中的token
		String token = header.split(" ")[1];//Bearer token
		//4.解析token
		Claims claims = null;
		try {
			claims = jwtUtil.parseJWT(token);
		}catch (Exception e){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限，解析出异常了");
		}
		//5.判断是否解析成功
		if(claims == null){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限，解析的不对");
		}
		//6.判断是不是管理员
		String roles = (String)claims.get("roles");
		if(!"admin_role".equals(roles)){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限，不是管理员");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}*/

	/**
	 * 发送手机验证码
	 * 此时不是真的发送，而是往队列里写个消息
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendsms(@PathVariable("mobile") String mobile){
		userService.sendsms(mobile);
		return new Result(true,StatusCode.OK,"发送成功!");
	}

	/**
	 * 用户注册
	 * @param user
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result register(@RequestBody User user,@PathVariable("code") String code){
		userService.register(user,code);
		return new Result(true,StatusCode.OK,"注册成功!");
	}

	@Autowired
	private JwtUtil jwtUtil;
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public Result userLogin(@RequestBody User user){
		//1.用户登录
		User sysuser = userService.userLogin(user.getMobile(),user.getPassword());
		//2.签发token
		String token = jwtUtil.createJWT(sysuser.getId(),sysuser.getMobile(),"user_role");
		//3.创建返回值对象
		Map<String,String> map = new HashMap<>();
		map.put("name",sysuser.getNickname());
		map.put("token",token);
		map.put("avatar",sysuser.getAvatar());
		return new Result(true,StatusCode.OK,"登录成功!",map);
	}

	/**
	 * 更新用户粉丝数
	 * @param userid
	 * @param fans
	 */
	@RequestMapping(method = RequestMethod.PUT,value = "/incfans/{userid}/{fans}")
	public void updateFans(@PathVariable("userid") String userid,@PathVariable("fans") int fans){
		userService.updateFans(userid,fans);
	}


	/**
	 * 更新用户关注数
	 * @param userid
	 * @param follow
	 */
	@RequestMapping(method = RequestMethod.PUT,value = "/incfollow/{userid}/{follow}")
	public void updateFollow(@PathVariable("userid") String userid,@PathVariable("follow") int follow){
		userService.updateFollow(userid,follow);
	}
}
