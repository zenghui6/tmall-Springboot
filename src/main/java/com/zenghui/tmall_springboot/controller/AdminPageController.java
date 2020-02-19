package com.zenghui.tmall_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面转跳，所有的后台页面转跳都放在AdminPageController这个控制器里
 * 而RESTFUL专门放在CategoryController里面，这样代码更清晰
 */
@Controller
public class AdminPageController {

    //种类相关转跳
    @GetMapping(value = "/admin")
    public String admin(){
        return "redirect:admin_category_list";     //重定向
    }

    @GetMapping(value = "/admin_category_list")
    public String listCategory(){
        return "admin/listCategory";    //访问admin/listCategory.html文件
    }

    @GetMapping(value = "/admin_category_edit")
    public String editCategory(){
        return "admin/editCategory";
    }


    //属性相关转跳
    @GetMapping("/admin_property_list")
    public String listProperty(){
        return "admin/listProperty";
    }

    @GetMapping("/admin_property_edit")
    public String editProperty(){
        return "admin/editProperty";
    }

    @GetMapping("/admin_propertyValue_edit")
    public String editPropertyValue(){
        return "admin/editPropertyValue";
    }


    //产品相关转跳
    @GetMapping("/admin_product_list")
    public String listProduct(){
        return "admin/listProduct";
    }

    @GetMapping("/admin_product_edit")
    public String editProduct(){
        return "admin/editProduct";
    }

    //产品图片相关转跳
    @GetMapping("/admin_productImage_list")
    public String listProductImage(){
        return "admin/listProductImage";
    }

    //用户管理页面转跳
    @GetMapping("/admin_user_list")
    public String listUser(){
        return "admin/listUser";
    }

    //订单转跳
    @GetMapping("/admin_order_list")
    public String listOrder(){
        return "admin/listOrder";
    }

}
