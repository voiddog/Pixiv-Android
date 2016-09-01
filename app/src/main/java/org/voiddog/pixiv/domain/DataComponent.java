package org.voiddog.pixiv.domain;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 数据层Component
 * Created by qigengxin on 16/9/1.
 */
@Singleton
@Component(modules = DataModel.class)
public interface DataComponent {
    IDataCore getIDataCore();
}
