package com.qfedu.fmmall.dao;

import com.qfedu.fmmall.entity.Category;
import com.qfedu.fmmall.entity.CategoryVO;
import com.qfedu.fmmall.general.GeneralDAO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends GeneralDAO<Category> {

    //一次性查询，但是必须已知多级菜单的级数，并且级数是固定的
    public List<CategoryVO> selectAllCategory();
    //递归式查询，使用<collection>的递归方法，进行多级子查询
    public List<CategoryVO> selectAllCategory2(int parentId);
    //查询一级菜单信息
    public List<CategoryVO> selectFirstLevelCategories();

}