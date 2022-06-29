package com.qfedu.fmmall.service;

import com.qfedu.fmmall.vo.ResultVO;
import org.springframework.stereotype.Repository;

/**
 * @author chen
 * @date 2021/8/18-23:51
 */
@Repository
public interface CategoryService {

    public ResultVO listCategories();

    public ResultVO listFirstLevelCategories();
}
