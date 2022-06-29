package com.qfedu.fmmall;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.qfedu.fmmall.dao.*;
import com.qfedu.fmmall.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class ApiApplicationTests {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCommentsMapper productCommentsMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private IndexImgMapper indexImgMapper;

    @Test
    public void contextLoads() {
        List<CategoryVO> categoryVOS = categoryMapper.selectAllCategory2(0);
        for (CategoryVO c1 : categoryVOS){
            System.out.println(c1);
            for (CategoryVO c2 : c1.getCategories()){
                System.out.println("\t"+c2);
                for (CategoryVO c3 : c2.getCategories()){
                    System.out.println("\t"+"\t"+c3);
                }
            }
        }
    }

    @Test
    public void testRecommendProduct(){
        List<ProductVO> products = productMapper.selectRecommendProducts();
        for (ProductVO p : products){
            System.out.println(p);
        }
    }

    @Test
    public void testCategoryFirst(){
        List<CategoryVO> categoryVOS = categoryMapper.selectFirstLevelCategories();
        for (CategoryVO c:categoryVOS){
            System.out.println(c);
        }
    }

    @Test
    public void testSelectComments(){
        List<IndexImg> indexImgs = indexImgMapper.listIndexImgs();
        for (IndexImg indexImg : indexImgs) {
            System.out.println(indexImg);
        }
    }

    @Test
    public void testShoppingCartsByCids(){
        String cids = "8,10";
        String[] arr = cids.split(",");
        List<Integer> cidsList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            cidsList.add(Integer.parseInt(arr[i]));
        }
        List<ShoppingCartVO> list = shoppingCartMapper.selectShopCartByCids(cidsList);
        list.forEach(System.out::println);
    }

}
