package com.zenghui.tmall_springboot.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 前台页面转跳
 */
@Controller
public class ForePageController {
    @GetMapping("/")
    public String index(){
        return "redirect:home"; //重定向
    }

    @GetMapping("/home")
    public String home(){
        return "fore/home";
    }

    //注册页面转跳
    @GetMapping(value = "/register")
    public String register(){
        return "fore/register";
    }
    //支付宝页面转跳
    @GetMapping(value="/alipay")
    public String alipay(){
        return "fore/alipay";
    }
    @GetMapping(value="/bought")
    public String bought(){
        return "fore/bought";
    }
    @GetMapping(value="/buy")
    public String buy(){
        return "fore/buy";
    }
    @GetMapping(value="/cart")
    public String cart(){
        return "fore/cart";
    }
    @GetMapping(value="/category")
    public String category(){
        return "fore/category";
    }
    @GetMapping(value="/confirmPay")
    public String confirmPay(){
        return "fore/confirmPay";
    }
    @GetMapping(value="/login")
    public String login(){
        return "fore/login";
    }
    @GetMapping(value="/orderConfirmed")
    public String orderConfirmed(){
        return "fore/orderConfirmed";
    }
    @GetMapping(value="/payed")
    public String payed(){
        return "fore/payed";
    }
    @GetMapping(value="/product")
    public String product(){
        return "fore/product";
    }
    @GetMapping(value="/registerSuccess")
    public String registerSuccess(){
        return "fore/registerSuccess";
    }
    @GetMapping(value="/review")
    public String review(){
        return "fore/review";
    }
    @GetMapping(value="/search")
    public String searchResult(){
        return "fore/search";
    }
    @GetMapping("/forelogout")
    public String logout() {
        //获取对象
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated())  //用户处于登录状态
            subject.logout();
        return "redirect:home";
    }

}
