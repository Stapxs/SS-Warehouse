package cn.stapx.ioc.config;

import cn.stapx.ioc.bean.MyBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IoCConfig.class})
class IoCConfigTest {

    @Autowired
    MyBean myBean1;
    @Autowired
    MyBean myBean2;

    @Test
    void test(){
        System.out.println(myBean1);
        System.out.println(myBean2);
    }
}