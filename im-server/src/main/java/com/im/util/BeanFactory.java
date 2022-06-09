package com.im.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author pang-yun
 * Date:  2022-06-09 16:55
 * Description: 有些地方不能直接注入 需要实现 ApplicationContextAware， 将 context 注入进来 以获取到想要的 bean
 */
@Component
public final class BeanFactory implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    /**
     * 根据class 获取相应的bean
     *
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> c) {
        return context.getBean(c);
    }


    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

}
