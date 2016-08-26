package org.voiddog.pixiv.presentation.ui.common.activity.base;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * activity 生命周期注解
 * Created by qigengxin on 16/8/26.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope{
}
