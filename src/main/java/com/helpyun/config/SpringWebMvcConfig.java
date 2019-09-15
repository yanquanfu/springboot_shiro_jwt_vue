package com.helpyun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Author: yanquanfu rebort
 * @Description:
 * @Date:Create：in 2019/9/15 21:51
 * @Modified By：
 */
@Configuration
public class SpringWebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/resources/static/");
    }


    /**
     * 访问根路径默认跳转 index.html页面 （简化部署方案： 可以把前端打包直接放到项目的 webapp，上面的配置）
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
    }
}
