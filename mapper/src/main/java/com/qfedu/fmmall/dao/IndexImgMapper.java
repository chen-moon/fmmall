package com.qfedu.fmmall.dao;

import com.qfedu.fmmall.entity.IndexImg;
import com.qfedu.fmmall.general.GeneralDAO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexImgMapper extends GeneralDAO<IndexImg> {
    public List<IndexImg> listIndexImgs();
}