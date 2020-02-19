package com.zenghui.tmall_springboot.controller;

import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.service.CategoryService;
import com.zenghui.tmall_springboot.util.ImageUtil;
import com.zenghui.tmall_springboot.util.PageNavigator;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 专门提供RestFUl服务的控制器
 *  REST格式：
 *  查询多个　返回json数组
 *  增加，查询，修改都返回当前对象的Json数组
 *  删除返回空字符串
 */
@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * @param start
     * @param size
     * @return  navigatePages 表示导航栏的大小
     */
    //带分页导航的数据
    @GetMapping("/categories")
    public PageNavigator<Category> list(@RequestParam(value = "start",defaultValue = "0") int start,
                                        @RequestParam(value = "size",defaultValue = "5") int size){
        start  = start<0?0:start;
        PageNavigator<Category> pageNavigator = categoryService.list(start,size,7);

        return pageNavigator;
    }

    @PostMapping("/categories")     //前端传入的formData中的name会自动注入到　bean中　image会注入到MulipartFile
    public Object add(@RequestBody Category bean, MultipartFile image, HttpServletRequest request) throws IOException {
        categoryService.add(bean);

        saveOrUpdateImageFile(bean,image,request);
        return bean;
    }

//传入request参数是为了得到文件路径然后删除．REST规定，删除返回空字符串；
    @DeleteMapping("/categories/{id}")
    public String delete (@PathVariable("id") int id, HttpServletRequest request){
        categoryService.delete(id);
        File imageFolder = new File(request.getServletContext().getRealPath("/img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return null;
    }

    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id){
        Category c = categoryService.get(id);
        return c;
    }

    /**
     *
     * @param id
     * @param image
     * @param request
     * @return
     * @throws IOException
     *
     *  put请求,不知道为什么不自动将name注入Category对象，所有使用request.getParameter获取请求的参数
     *  查阅资料后，没有肯定的答案，发现：
     *  上传图片，需要用到FormDate类，formDate类本质上就是一个html中的form(表单)，
     *  但是put方式和delete方式默认无法提交表单数据，所有自动注入category对象是不行的．
     *  当不使用FormDate时直接使用　JSON传递数据，通过@RequestBody讲JSON对象啊直接注入
     */


    @PutMapping("/categories/{id}")
    public Category update(@PathVariable("id") int id,MultipartFile image ,HttpServletRequest request) throws IOException {
       Category c = categoryService.get(id);
        c.setName(request.getParameter("name"));
        categoryService.update(c);

        if (image!=null){
            saveOrUpdateImageFile(c,image,request);
        }
        return c;
    }

    //工具方法，将上传的图片保存
    public void saveOrUpdateImageFile(Category bean,MultipartFile image,HttpServletRequest request) throws IOException {
        //得到路径,根据路径创建文件夹
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        //在文件夹下创建文件
        File file = new File(imageFolder,bean.getId()+".jpg");
        System.out.println(file.getPath());
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img,"jpg",file);
    }
}
