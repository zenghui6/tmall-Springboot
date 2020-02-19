package com.zenghui.tmall_springboot.controller;

import com.zenghui.tmall_springboot.comparator.*;
import com.zenghui.tmall_springboot.entity.*;
import com.zenghui.tmall_springboot.service.*;
import com.zenghui.tmall_springboot.util.Result;
import javafx.beans.binding.ObjectExpression;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 前台REST接口
 */
@RestController
public class ForeRestController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderService orderService;

    @GetMapping("/forehome")
    public Object home(){
        List<Category> categories = categoryService.list();
        //填入对应的导航信息
        productService.fill(categories);    //同时为category 对应的　products的firstImage字段赋上值
        productService.fillByRow(categories);

        //清除循环嵌套
        categoryService.removeCategoryFromProduct(categories);
        return categories;
    }

    /**
     * 返回的Result对象，１代表注册成功 0代表注册失败．
     * @param user
     * @return
     */
    @PostMapping("/foreregister")
    public Object register(@RequestBody User user){ //POST请求的主体要用@RequestBody接收
        String name = user.getName();
        String password = user.getPassword();
        //将特殊字符转义
        name = HtmlUtils.htmlEscape(name);

        boolean exits = userService.isExist(name);

        if (exits){
            String message = "用户名已经被使用，不能使用";
            return Result.fail(message);
        }

        //安全随机数字生成器
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times =2; //加密次数
        String algorithmName = "md5";
        //生成加密后的密码
        String encodedPassword = new SimpleHash(algorithmName,password,salt,times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);
        return Result.success();
    }

    //检查登录状态
    @GetMapping("/forecheckLogin")
    public Object checkLogin(){
        //改为Shiro的方式进行登录判断
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated())
            return Result.success();
        else
            return Result.fail("未登录");
    }

    @PostMapping("/forelogin")
    public Object login(@RequestBody User userParam, HttpSession session){
        String name = userParam.getName();
        name = HtmlUtils.htmlEscape(name);  //对特殊字符转义

        //使用Shiro进行登录验证
        Subject subject = SecurityUtils.getSubject();
        //使用用户名和密码生成token
        UsernamePasswordToken token = new UsernamePasswordToken(name,userParam.getPassword());

        try {
            subject.login(token);
            //登录成功将用户名写入session
            User user = userService.getByName(name);
            session.setAttribute("user",user);
            return Result.success();
        } catch (AuthenticationException e){
            String message = "账户密码错误";
            return Result.fail(message);
        }
    }

    //商品详情页
    @GetMapping("/foreproducts/{pid}")
    public Object product(@PathVariable("pid") int pid){
        Product product = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        //获取产品所有的属性值
        List<PropertyValue> propertyValues  = propertyValueService.list(product);
        //因为propertyValue与product关联了，这样会让接口看起来轻松一点，删除点没用的调用信息
        for (PropertyValue pv : propertyValues) {
            pv.setProduct(null);
        }

        //获取产品的所有评价
        List<Review> reviews  = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);     //赋值销量和评论数
        productImageService.setFirstProductImage(product);  //赋值首页图片

        //以Map的格式将数据传递到前端，因为是多个集合这样更简洁，更容易操作
        Map<String,Object> map = new HashMap<>();
        map.put("product",product);
        map.put("pvs",propertyValues);
        map.put("reviews",reviews);

        return Result.success(map);
    }

    //商品种类分类
    @GetMapping("/forecategory/{cid}")      //../forecategory/81?sort=review    路径元素使用@PathVariable ,
    public Object category(@PathVariable("cid") int cid, String sort){  //sort是传入的排序方式
        //获取Category对象c
        Category c = categoryService.get(cid);
        //为C填充product和销量评价
        productService.fill(c);     //为c中products字段赋值
        productService.setSaleAndReviewNumber(c.getProducts()); //为所有商品赋值
        //为了防止循环调用,将填充的product下的category字段赋值为空
        categoryService.removeCategoryFromProduct(c);

        if(sort != null){
            switch (sort){
                case "review":
                    //集合比较排序方法
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;

                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
            }
        return c;
        }

    //搜索
    @PostMapping("/foresearch")
    public Object search(String keyword){       //?keyword
        System.out.println(keyword);
        if (keyword == null)
            keyword = " ";
        List<Product> ps = productService.search(keyword,0,20);
        productService.setSaleAndReviewNumber(ps);
        productImageService.setFirstProductImages(ps);
        return ps;
    }

    //直接购买或加入购物车
    @GetMapping("/forebuyone")
    public Object buyone(int pid,int num,HttpSession session){
        return buyoneAndAddCart(pid,num,session);
    }
    //直接购买该商品或者将该商品加入购物车
    public int buyoneAndAddCart(int pid,int num,HttpSession session){
        //获取商品对象
        Product product = productService.get(pid);
        int oiid = 0;   //订单项id

        //确定用户
        User user = (User) session.getAttribute("user");
        //判断该用户的购物车中是否已经有了该商品的订单项,如果已经有了,则只改变数量,否则就新建订单项
        boolean found = false;
        List<OrderItem> orderItems = orderItemService.ListByUser(user);
        for (OrderItem oi : orderItems) {
            if(oi.getProduct().getId() == pid){ //已经存在该订单项
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);//更新数据
                found = true;
                oiid = oi.getId();
                break;
            }
        }
        //没有找到订单项,创建订单项对象,为购物车增加订单项
        if(!found){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setUser(user);
            orderItem.setNumber(num);
            orderItemService.add(orderItem);
            oiid = orderItem.getId();
        }
        return oiid;
    }

    /**
     *立即购买,计算总价   购物车勾选订单项,将选择的订单项写入session中,在结算界面就可以直接在session中获取所购买的商品
     * @param oiid
     * @param httpSession
     * @return
     */
    @GetMapping("/forebuy")
    public Object buy(String[] oiid,HttpSession httpSession){
        System.out.println(oiid);
        //购物车订单项数组
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0; //总价

        for (String strid : oiid) {
            int id = Integer.parseInt(strid);//转int
            OrderItem orderItem = orderItemService.get(id);
            //每一项都计算价钱后增加
            total += orderItem.getProduct().getPromotePrice()*orderItem.getNumber();    //商品单价*数量
            //加入购物车订单数组
            orderItems.add(orderItem);
        }
        productImageService.setFirstProductImagesOnOrderItems(orderItems);

        httpSession.setAttribute("ois",orderItems);

        Map<String,Object> map = new HashMap<>();
        map.put("orderItems",orderItems);
        map.put("total",total);
        return Result.success(map);

    }

    /**
     * 加入购物车,将商品,数量加入购物车
     * @param pid
     * @param num
     * @param session
     * @return
     */
    @GetMapping("/foreaddCart")
    public Object addCart(int pid, int num,HttpSession session){
        buyoneAndAddCart(pid, num, session);
        return Result.success();
    }

    /**
     * 查看购物车
     * @param session
     * @return
     */
    @GetMapping("/forecart")
    public Object cart(HttpSession session){
    //session查找用户,再根据用户找到对应的所有订单项 ----购物车
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.ListByUser(user);

        productImageService.setFirstProductImagesOnOrderItems(ois);
        return ois;
    }

    /**
     * 更改购物车的商品购买数
     * @param session
     * @param pid
     * @param num
     * @return  Result.success()
     */
    @GetMapping("/forechangeOrderItem")
    public Object changeOrderItem(HttpSession session,int pid, int num){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return  Result.fail("未登录");
        }

        //找到对应的购物车中的商品信息然后更改,更新,返回成功的结果
        List<OrderItem> ois = orderItemService.ListByUser(user);
        for (OrderItem oi : ois) {
            if (oi.getProduct().getId()==pid){
                oi.setNumber(num);
                orderItemService.update(oi);
                break;
            }
        }
        return Result.success();
    }

    /**
     * 删除购物车中的订单项
     * @param session
     * @param oiid
     * @return
     */
    @GetMapping("/foredeleteOrderItem")
    public Object deleteOrderItem(HttpSession session, int oiid){
        User user = (User) session.getAttribute("user");
        if (user == null)
            return Result.fail("未登录");

        //判断它要删除的订单项是不是属于他的
        List<OrderItem> ois = orderItemService.ListByUser(user);
        for (OrderItem oi : ois ) {
            if (oi.getId() == oiid){
                orderItemService.delete(oi);
                return Result.success();
            }
        }
        return Result.fail("订单项不属于该用户");
    }

    /**
     * 创建订单,订单信息和总钱数
     * @param order
     * @param session
     * @return
     */
    @PostMapping("/forecreateOrder")
    public Object createOrder(@RequestBody Order order,HttpSession session){
        System.out.println("创建订单函数被调用");
        User user = (User) session.getAttribute("user");
        if ((user == null))
            return Result.fail("未登录");

        //生成订单号,时间加随机4位数
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderService.waitPay);
        orderService.add(order);

        //用户的购物车项
        //List<OrderItem> ois = orderItemService.ListByUser(user);  //错了,这样必须是清空购物车
        List<OrderItem> ois = (List<OrderItem>) session.getAttribute("ois");

        //计算总价
        float total=0;
        for (OrderItem oi: ois ) {
            oi.setOrder(order); //为订单项设置订单号,就可以从购物车中清除了
            orderItemService.update(oi);//持久化更新数据库
            total += oi.getProduct().getPromotePrice()*oi.getNumber();
        }

        Map<String,Object> map = new HashMap<>();
        map.put("oid",order.getId());
        map.put("total",total);

        return Result.success(map);
    }

    /**
     * 付款后更新订单状态
     * @param oid
     * @return
     */
    @GetMapping("/forepayed")
    public Object payed(int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

    /**
     * 确认收货
     * @param oid
     * @return
     */
    @GetMapping("/foreconfirmPay")
    public Object confirmPay(int oid){
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        orderService.cacl(o);
        orderService.removeOrderFormOrderItem(o);
        return o;
    }

    /**
     *
     * @param session
     * @return
     */
    @GetMapping("/forebought")
    public Object bought(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null)
            return Result.fail("未登录");

        List<Order> os = orderService.listByUserWithoutDelete(user);
        orderService.removeOrderFormOrderItem(os);
        return os;
    }

    /**
     * 确认收货后更改状态
     * @param oid
     * @return
     */
    @GetMapping("foreorderConfirmed")
    public Object orderConfirmed(int oid){
        Order o = orderService.get(oid);
        //确认收货后订单状态变为待评价
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);
        return Result.success();
    }

    /**
     * 删除订单信息,但是因为订单信息是比较重要的,并不直接从数据库中删除,而是更改订单状态为delete
     * @param oid
     * @return
     */
    @PutMapping("foredeleteOrder")
    public Object deleteOrder(int oid){
        Order o = orderService.get(oid);
        //更改订单状态为delete
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return Result.success();
    }

    /**
     * 对每个订单的第一个商品进行评价,显示评价信息
     * @param oid
     * @return
     */
    @GetMapping("forereview")
    public Object review(int oid){
        Order o  = orderService.get(oid);
        orderItemService.fill(o);
        orderService.removeOrderFormOrderItem(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p);
        //为商品生成销量和评价信息
        productService.setSaleAndReviewNumber(p);

        Map<String,Object> map = new HashMap<>();
        map.put("p",p);
        map.put("o",o);
        map.put("review",reviews);

        return Result.success(map);
    }

    /**
     * 添加评价
     * @param session
     * @param oid
     * @param pid
     * @param content
     * @return
     */
    @PostMapping("/foredoreview")
    public Object doreview(HttpSession session,int oid,int pid,String content){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        User user = (User) session.getAttribute("user");
        Review review = new Review();
        review.setProduct(p);
        review.setCreateDate(new Date());
        review.setUser(user);
        review.setContent(content);
        reviewService.add(review);
        return Result.success();
    }
}
