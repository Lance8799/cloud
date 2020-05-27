package org.lance.cloud.sample.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lance.cloud.domain.entity.User;
import org.lance.cloud.sample.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lance
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User user = new User();
        user.setId(10000000006L);
        user.setName("周冬梅");
        user.setAge(23);
        user.setEmail("zhou@163.com");
        user.setCreateTime(LocalDateTime.now());
        user.setRemark("测试字段");
        userMapper.insert(user);
    }

    @Test
    public void testSelectBatchIds() {
        List<Long> ids = Arrays.asList(10000000002L, 10000000004L, 10000000005L);
        List<User> users = userMapper.selectBatchIds(ids);
        print(users);
    }

    @Test
    public void testSelectByMap(){
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "周冬梅");
        columnMap.put("age", 23);
        List<User> users = userMapper.selectByMap(columnMap);
        print(users);
    }

    /**
     * name like %大% and age between 20 and 30 and email is not null
     */
    @Test
    public void testSelectByWrapper(){
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.like("name", "大").between("age", 20, 30).isNotNull("email");
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    /**
     * name like %东 or age >= 25 order by age desc, id asc
     */
    @Test
    public void testSelectByWrapper2(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.likeLeft("name", "东").or()
                .ge("age", 25).orderByDesc("age").orderByAsc("id");
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    /**
     * date_format(create_time, '%Y-%m-%d') and superior_id in (select id from user where name like '刘%')
     */
    @Test
    public void testSelectByWrapper3(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.apply("date_format(create_time, '%Y-%m-%d')={0}", "2019-08-01")
                .inSql("superior_id", "select id from user where name like '刘%'");
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    /**
     * name like '陈%' and (age < 40 or email is not null)
     */
    @Test
    public void testSelectByWrapper4(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.likeRight("name", "陈").and(qw -> qw.lt("age", 40).or().isNotNull("email"));
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    /**
     * (age < 40 or email is not null) and name like 陈%'
     */
    @Test
    public void testSelectByWrapper5(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "陈");
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    /**
     * age in (23,25,30) limit 1
     */
    @Test
    public void testSelectByWrapper6(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.in("age", Arrays.asList(23, 25, 30)).last("limit 1");
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    /**
     * 只显示部分列
     * name like %大% and age between 20 and 30 and email is not null
     */
    @Test
    public void testSelectByWrapperSelect(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("id", "name").like("name", "大").between("age", 20, 30).isNotNull("email");
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    /**
     * 排查部分列
     * name like %大% and age between 20 and 30 and email is not null
     */
    @Test
    public void testSelectByWrapperSelect2(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.like("name", "大").between("age", 20, 30).isNotNull("email")
                .select(User.class, fieldInfo ->
                        !fieldInfo.getColumn().equals("create_time") &&
                        !fieldInfo.getColumn().equals("superior_id"));
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    @Test
    public void testSelectByWrapperCondition(){
        String name = "大";
        String email = "";

        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.like(StringUtils.hasText(name), "name", name)
                .like(StringUtils.hasText(email), "email", email);
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    @Test
    public void testSelectByWrapperEntity(){
        User queryUser = new User();
        queryUser.setName("周冬梅");
        queryUser.setAge(25);

        QueryWrapper<User> wrapper = new QueryWrapper<>(queryUser);
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }

    @Test
    public void testSelectByWrapperAllEq(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "周冬梅");
        params.put("age", null);
        wrapper.allEq(params, false);
        List<User> users = userMapper.selectList(wrapper);
        print(users);
    }


    /**
     * 只需要返回少数字段时
     */
    @Test
    public void testSelectByWrapperMaps(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("id", "name").like("name", "大");
        List<Map<String, Object>> users = userMapper.selectMaps(wrapper);
        print(users);
    }

    /**
     * 返回的字段是统计时
     */
    @Test
    public void testSelectByWrapperMaps2(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("min(age) min_age", "max(age) max_age")
                .groupBy("superior_id")
                .having("sum(age) < {0}", 300);
        List<Map<String, Object>> users = userMapper.selectMaps(wrapper);
        print(users);
    }

    /**
     * 返回第一列
     */
    @Test
    public void testSelectByWrapperObjs(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("id", "name").like("name", "大");
        List<Object> users = userMapper.selectObjs(wrapper);
        print(users);
    }

    @Test
    public void testSelectByWrapperOne(){
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.like("name", "陈");
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    /**
     * name like '%陈%' and age < 30
     */
    @Test
    public void testSelectLambda(){
//        LambdaQueryWrapper<User> lambdaWrapper = new QueryWrapper<User>().lambda();
//        LambdaQueryWrapper<User> lambdaWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambdaWrapper = Wrappers.lambdaQuery();
        lambdaWrapper.like(User::getName, "陈").lt(User::getAge, 30);

        List<User> users = userMapper.selectList(lambdaWrapper);
        print(users);
    }

    /**
     * name like '陈%' and (age < 40 or email is not null)
     */
    @Test
    public void testSelectLambda2(){
        LambdaQueryWrapper<User> lambdaWrapper = Wrappers.lambdaQuery();
        lambdaWrapper.like(User::getName, "陈").and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        List<User> users = userMapper.selectList(lambdaWrapper);
        print(users);
    }

    @Test
    public void testSelectLambda3(){
        List<User> users = new LambdaQueryChainWrapper<>(userMapper)
                .like(User::getName, "陈")
                .lt(User::getAge, 30)
                .list();
        print(users);
    }

    @Test
    public void testUpdateByWrapper(){
        UpdateWrapper<User> wrapper = Wrappers.update();
        wrapper.eq("name", "周冬梅").eq("age", 23).set("age", 25);
        int updated = userMapper.update(null, wrapper);
        System.out.println(updated);
    }

    @Test
    public void testUpdateByWrapperLambda(){
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getName, "周冬梅").eq(User::getAge, 25).set(User::getAge, 23);
        int updated = userMapper.update(null, wrapper);
        System.out.println(updated);
    }

    @Test
    public void testUpdateByWrapperLambdaChain(){
        boolean isUpdate = new LambdaUpdateChainWrapper<>(userMapper)
                .eq(User::getName, "周冬梅").eq(User::getAge, 23).set(User::getAge, 21)
                .update();
        System.out.println(isUpdate);
    }

    private void print(List<?> data){
        data.forEach(System.out::println);
    }
}
