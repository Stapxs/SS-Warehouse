package cn.stapx.staticFactory;

import cn.stapx.domain.Bean;


public class MyBeanFactory {
    public static Bean createBean(){
        return new Bean();
    }
}