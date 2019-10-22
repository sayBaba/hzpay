package com.hz.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import javax.servlet.http.HttpServletRequest;

/**
 * 验证数据真实性，token校验
 */
public class LoanAppFilter extends ZuulFilter {


    /**
     *返回字符串代表过滤器的类型
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 通过返回的int值来定义过滤器的执行顺序，数字越小优先级越高。
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 返回一个Boolean值，判断该过滤器是否需要执行。返回true执行，返回false不执行。
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的具体业务逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //验证数据的请求数据是否串改，md5+明文
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        System.out.println("---------------------------------");
//        //获取请求的参数
//        if("user/login".equals(request.getRequestURI())){
//            return null;
//        }

//        //登录成功生成
//        //验证用户是否登录,JWT来做
//        String token = request.getParameter("token");
//        System.out.println("----------token:"+ token);
//        if(!StringUtils.isEmpty(token)){
//            boolean flag = JWTUtils.verify(token);
//            if(!flag){
//                System.out.println("---token失效----");
//                requestContext.setSendZuulResponse(false); // 过滤该请求，不对其进行路由
//                requestContext.setResponseStatusCode(401); // 设置响应状态码
//                requestContext.setResponseBody("无效的token"); // 设置响应状态码
//            }
//        }
        return null;
    }
}
