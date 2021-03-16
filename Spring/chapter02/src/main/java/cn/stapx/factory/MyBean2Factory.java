package cn.stapx.factory;

import cn.stapx.domain.Bean;


public class MyBean2Factory {
    public MyBean2Factory() {
        System.out.println("bean工厂实例化");
    }
    public Bean createBean(){
        return new Bean();
    }
}