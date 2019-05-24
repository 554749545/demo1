package cn.itcast;

import cn.itcast.domain.User;
import cn.itcast.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootQuick2Application.class)
public class RedisTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test() throws JsonProcessingException {
        //1.从redis中获得数据，数据的形式json字符串
        String user = redisTemplate.boundValueOps("user").get();
        //2.判读那redis中是否存在数据
        if (null==user){
            //3.不存在数据，从数据路中查询
            List<User> list = userRepository.findAll();
            //4.将查询到的数据存储到redis缓存中
            //将list转换成json字符串，使用jackson进行转换
            ObjectMapper objectMapper=new ObjectMapper();
            user = objectMapper.writeValueAsString(list);

            redisTemplate.boundValueOps("user").set(user);

            System.out.println("======从数据库中查询数据======");
        }else {
            System.out.println("=======从缓存中查询数据=======");
        }

        //5.将数据在控制台打印
        System.out.println(user);
    }
}
