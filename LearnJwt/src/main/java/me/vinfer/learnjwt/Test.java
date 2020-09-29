package me.vinfer.learnjwt;

import com.sun.org.apache.bcel.internal.generic.NEW;
import me.vinfer.learnjwt.model.User;
import me.vinfer.learnjwt.utils.JwtUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-21    16:51
 * @description
 **/
public class Test {


    public static void main(String[] args) {

        String pass = "123456";

        Map<String,Object> map = new HashMap<>(5);
        map.put("data", new User(12316546654L,"vinfer","asdkljhfasuhd"));
        map.put("timestamp", 41326851984L);
        Map<String,Object> dataMap = new HashMap<>(10);
        dataMap.put("data", map);
        String encode = JwtUtil.encode(pass,1000*60*60*24,dataMap);
        System.out.println(encode);
        System.out.println(JwtUtil.decode(encode));

    }

}
