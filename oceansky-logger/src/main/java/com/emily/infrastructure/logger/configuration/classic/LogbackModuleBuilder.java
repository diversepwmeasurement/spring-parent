package com.emily.infrastructure.logger.configuration.classic;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.emily.infrastructure.logger.common.CommonKeys;
import com.emily.infrastructure.logger.configuration.appender.AbstractAppender;
import com.emily.infrastructure.logger.configuration.appender.AsyncAppenderBuilder;
import com.emily.infrastructure.logger.configuration.appender.ConsoleAppenderBuilder;
import com.emily.infrastructure.logger.configuration.appender.RollingFileAppenderBuilder;
import com.emily.infrastructure.logger.configuration.property.LoggerProperties;

/**
 * 分组记录日志
 *
 * @author Emily
 * @since : 2021/12/12
 */
public class LogbackModuleBuilder extends AbstractLogback {
    private final LoggerProperties properties;
    private final LoggerContext lc;

    private LogbackModuleBuilder(LoggerProperties properties, LoggerContext lc) {
        this.properties = properties;
        this.lc = lc;
    }

    /**
     * 构建Logger对象
     * 日志级别以及优先级排序: OFF &gt; ERROR &gt; WARN &gt; INFO &gt; DEBUG &gt; TRACE &gt;ALL
     *
     * @param commonKeys 属性配置传递类
     * @return 日志对象
     */
    @Override
    public Logger getLogger(CommonKeys commonKeys) {
        // 获取Logger对象
        Logger logger = lc.getLogger(commonKeys.getLoggerName());
        // 设置是否向上级打印信息
        logger.setAdditive(false);
        // 设置日志级别
        logger.setLevel(Level.toLevel(properties.getModule().getLevel().levelStr));
        // appender对象
        AbstractAppender appender = RollingFileAppenderBuilder.create(properties, lc, commonKeys);
        // 是否开启异步日志
        if (properties.getAppender().getAsync().isEnabled()) {
            //异步appender
            AsyncAppenderBuilder asyncAppender = AsyncAppenderBuilder.create(properties, lc);
            if (logger.getLevel().levelInt == Level.ERROR_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.ERROR)));
            }
            if (logger.getLevel().levelInt == Level.WARN_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.WARN)));
            }
            if (logger.getLevel().levelInt == Level.INFO_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.INFO)));
            }
            if (logger.getLevel().levelInt == Level.DEBUG_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.DEBUG)));
            }
            if (logger.getLevel().levelInt == Level.TRACE_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.TRACE)));
            }
        } else {
            if (logger.getLevel().levelInt == Level.ERROR_INT) {
                logger.addAppender(appender.build(Level.ERROR));
            }
            if (logger.getLevel().levelInt == Level.WARN_INT) {
                logger.addAppender(appender.build(Level.WARN));
            }
            if (logger.getLevel().levelInt == Level.INFO_INT) {
                logger.addAppender(appender.build(Level.INFO));
            }
            if (logger.getLevel().levelInt == Level.DEBUG_INT) {
                logger.addAppender(appender.build(Level.DEBUG));
            }
            if (logger.getLevel().levelInt == Level.TRACE_INT) {
                logger.addAppender(appender.build(Level.TRACE));
            }
        }
        if (properties.getModule().isConsole()) {
            // 添加控制台appender
            logger.addAppender(ConsoleAppenderBuilder.create(properties, lc).build(logger.getLevel()));
        }

        return logger;
    }

    public static AbstractLogback create(LoggerProperties properties, LoggerContext lc) {
        return new LogbackModuleBuilder(properties, lc);
    }
}