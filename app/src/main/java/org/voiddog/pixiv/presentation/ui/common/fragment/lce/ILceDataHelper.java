package org.voiddog.pixiv.presentation.ui.common.fragment.lce;

/**
 * 数据添加通知帮助类
 * Created by qigengxin on 16/8/26.
 */
public interface ILceDataHelper<M> {
    /**
     * 添加数据, 会往后添加
     * @param data
     */
    void addData(M data);

    /**
     * 删除数据,会清空原先的数据
     * @param data
     */
    void setData(M data);

    /**
     * 是否需要加载更多数据
     */
    boolean isNeedLoadMore();
}
