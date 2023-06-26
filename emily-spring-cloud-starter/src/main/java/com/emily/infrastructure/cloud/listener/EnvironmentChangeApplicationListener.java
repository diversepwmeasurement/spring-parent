package com.emily.infrastructure.cloud.listener;

import com.emily.infrastructure.core.context.ioc.IOCContext;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.util.Objects;

/**
 * @Description :  Environment环境配置更改监听器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/6/25 5:54 PM
 */
public class EnvironmentChangeApplicationListener implements ApplicationListener<EnvironmentChangeEvent> {
    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        Object source = event.getSource();
        if (Objects.isNull(source) || !(source instanceof ApplicationContext)) {
            return;
        }
        IOCContext.setCONTEXT((ApplicationContext) source);
    }
}
