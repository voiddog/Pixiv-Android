package org.voiddog.pixiv.presentation.ui.common.activity.base;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * 提供Activity的内容
 * Created by qigengxin on 16/8/26.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ForActivity{
}
