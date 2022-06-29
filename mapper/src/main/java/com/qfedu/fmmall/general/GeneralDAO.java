package com.qfedu.fmmall.general;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author chen
 * @date 2021/8/10-15:58
 */
public interface GeneralDAO<T> extends Mapper<T>, MySqlMapper<T> {
}
