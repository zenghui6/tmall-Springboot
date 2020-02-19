package com.zenghui.tmall_springboot.controller;

import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.ProductImage;
import com.zenghui.tmall_springboot.service.ProductImageService;
import com.zenghui.tmall_springboot.service.ProductService;
import com.zenghui.tmall_springboot.service.impl.ProductImageServiceImpl;
import com.zenghui.tmall_springboot.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductImageController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;

    //获取pid 对应下的图片
    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@PathVariable("pid") int pid, @RequestParam("type") String type){
        Product product = productService.get(pid);

        //如果请求的是查看单个图片
        if (ProductImageServiceImpl.type_single.equals(type)){
            List<ProductImage> singles = productImageService.listSingleProductImages(product);
            return singles;//如果请求的是查看详情图片
        }else if (ProductImageServiceImpl.type_detail.equals(type)){
            List<ProductImage> details = productImageService.listDetailProductImages(product);
            return details;
        }
        else
            return new ArrayList<>();
    }

    //上传图片,保存,路径巨烦
    @PostMapping("/productImages")
    public ProductImage add(@RequestParam("pid") int pid, @RequestParam("type") String type,
                            MultipartFile image, HttpServletRequest request){
        //创建实例，并赋予数据，将image 与　product (manyToOne)关联
        ProductImage productImage = new ProductImage();

        Product product = productService.get(pid);
        productImage.setProduct(product);
        productImage.setType(type);

        productImageService.add(productImage);

        //  保存图片　文件操作
        String folder = "img/";
        //传入图片type 为single
        if (productImage.getType().equals("single")){
            folder += "productSingle";
        }else if (productImage.getType().equals("detail")){
            folder += "productDetail";
        }
        //获取绝对路径
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        System.out.println(imageFolder);
        //  productSingle/1.jpg  ,这时候还是个壳，还没transferto
        File file = new File(imageFolder,productImage.getId()+".jpg");
        String fileName = file.getName();
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            image.transferTo(file);
            //格式转换
            BufferedImage image1 = ImageUtil.change2jpg(file);
            ImageIO.write(image1,"jpg",file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //single的缩略图处理,处理成大中小
        if (productImage.getType().equals("single")){
            String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");

            File file_small = new File(imageFolder_small,fileName);
            File file_middle = new File(imageFolder_middle,fileName);

            //创建对应的文件夹
            if (!file_small.getParentFile().exists())
                file_small.getParentFile().mkdirs();
            if (!file_middle.getParentFile().exists())
                file_middle.getParentFile().mkdirs();

            ImageUtil.resizeImage(file,56,56,file_small);
            ImageUtil.resizeImage(file,217,217,file_middle);
        }

        return productImage;
    }


    //删除图片记录，包括数据库中的和路径中的文件
    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") int id,HttpServletRequest request){
        ProductImage productImage = productImageService.get(id);
        productImageService.delete(id);

        //删除文件，要找到对应的目录，
        String folder = "img/";
        if (productImage.getType().equals("single")){
            folder += "productSingle";
        }else if (productImage.getType().equals("Detail")){
            folder += "productsDetail";
        }

        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,productImage.getId()+"jpg");
        String fileName = file.getName();
        file.delete();

        //如果图片是single 图片，还要删除　对应　small 和　middle 下的图片
        if (productImage.getType().equals("single")) {
            String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");

            File file_small = new File(imageFolder_small, fileName);
            File file_middle = new File(imageFolder_middle, fileName);

            file_small.delete();
            file_middle.delete();
        }

        return null;
    }


}
