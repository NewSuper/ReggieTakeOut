package cn.tqxd.reggie.filter;

import cn.tqxd.reggie.utils.BaseContext;
import cn.tqxd.reggie.vo.Result;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 完善登录功能
 * 问题分析
 * 如果用户不登录,直接访问系统首页，照样可正常访问，不合理
 * 解决方案：
 * 1.创建自定义过滤器LoginCheckFilter
 * 2.在启动类上加上注解@ServletComponentScan
 * 3.完善过滤器的处理逻辑
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = {"/*"})  //   代表拦截所有,可后面再细分过滤/*
@Slf4j
public class LoginCheckFilter implements Filter{

    //路径匹配器.支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 操作步骤:
     * 1.获取本次请求的uri  例如：/backend/index.html
     * 2.判断本次请求是否需要处理
     * 3.如不需要处理，直接放行
     * 4-1 判断后台系统登录状态。如已登录，直接放行
     * 4-2 判断app端登录状态。如已登录，直接放行
     * 5 如果未登录则返回未登录结果，通过输出流的方式向客户端响应数据
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //1.获取本次请求的uri  例如：/backend/index.html
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",//登录
                "/employee/logout",//退出
                "/backend/**",//页面是可以看的，主要是控制ajax请求
                "/front/**",//页面是可以看的，主要是控制ajax请求
                "/common/**",//
                "/user/sendMsg",//
                "/user/login"//
        };

        //2.判断本次请求是否需要处理
         boolean check = check(urls,requestURI);
        //3.如不需要处理，直接放行
        if (check){
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1 判断后台系统登录状态。如已登录，直接放行
        if (request.getSession().getAttribute("employee")!= null){
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        //4-2 判断app端登录状态。如已登录，直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        //5 如果未登录则返回未登录结果，通过输出流的方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requesrURI
     * @return
     */
    public boolean check(String[] urls,String requesrURI){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url,requesrURI);
            if (match){
                return  true;
            }
        }
        return false;
    }
}
