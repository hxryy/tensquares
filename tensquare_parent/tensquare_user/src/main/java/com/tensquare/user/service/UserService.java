package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RedisTemplate redisTemplate;
	/**
	 * 发送手机验证码，就是往队列里写入一个消息
	 * 同时还要往redis里写入一个缓存
	 * @param mobile
	 * 生成验证码的需求是一个6位数字
	 */
	public void sendsms(String mobile){
		//1.定义随机数对象
		Random random = new Random();
		//2.生成一个6位的随机数
		Integer code = random.nextInt(999999);
		//3.判断随机数是否够6位，不够补全
		if(code < 100000){
			code = code + 100000;
		}
		//4.把生成的信息和手机号创建一个map存起来
		Map<String,String> map = new HashMap();
		map.put("mobile",mobile);
		map.put("code", code.toString());
		//5.把生成的map写入消息队列
		rabbitTemplate.convertAndSend("sms",map);
		//6.把验证码和手机号存入redis，用于注册时的验证
		redisTemplate.opsForValue().set(mobile,code.toString(),10, TimeUnit.MINUTES);

		System.out.println(code);
	}

	/**
	 * 用户注册
	 * @param user
	 * @param code
	 */
	public void register(User user,String code){
//		//1.使用手机号查询用户，如果发现已经有了，提示不能重复注册
//		User dbuser = userDao.findByMobile(user.getMobile());
//		if(dbuser != null){
//			throw new IllegalStateException("请不要重复注册");
//		}
//		//2.使用手机号取出redis中的验证码
//		String redisCode = (String)redisTemplate.opsForValue().get(user.getMobile());
//		if(StringUtils.isEmpty(code)){
//			throw new NullPointerException("请输入验证码！");
//		}
//		if(!code.equals(redisCode)){
//			throw new IllegalArgumentException("验证码不正确，请重新输入");
//		}
		//3.保存用户
		user.setId(String.valueOf(idWorker.nextId()));//给user生成主键
		//给其他字段赋值
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(new Date());//注册日期
		user.setUpdatedate(new Date());//更新日期
		user.setLastdate(new Date());//最后登陆日期
		//密码加密
		user.setPassword(encoder.encode(user.getPassword()));
		//实现保存
		userDao.save(user);
	}

	/**
	 * 用户登录
	 * @param mobile
	 * @param password
	 * @return
	 */
	public User userLogin(String mobile,String password){
		//1.使用手机号查询用户
		User user = userDao.findByMobile(mobile);
		//2.判断是否有此用户
		if(user == null ){
			throw new IllegalStateException("手机号或者密码有误，请核对后在输入");
		}
		//3.校验密码
		boolean check = encoder.matches(password,user.getPassword());
		if(!check){
			throw new IllegalStateException("手机号或者密码有误，请核对后在输入");
		}
		//4.返回
		return user;
	}

	/**
	 * 更新用户的粉丝数
	 * @param userid  用户的id
	 * @param fans    粉丝数量（它只能是1或者-1）
	 */
	@Transactional
	public void updateFans(String userid,int fans){
		userDao.updateFans(userid,fans);
	}


	/**
	 * 更新用户的关注数
	 * @param userid  用户的id
	 * @param fans    关注数量（它只能是1或者-1）
	 */
	@Transactional
	public void updateFollow(String userid,int fans){
		userDao.updateFollow(userid,fans);
	}
}
