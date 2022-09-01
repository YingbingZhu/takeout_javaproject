package com.javaproject.takeout.filter;

import com.alibaba.fastjson.JSON;
import com.javaproject.takeout.common.BaseContext;
import com.javaproject.takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * check if user complete login
 */

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // URL MATCHER, SUPPORT REGEX
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // get request uri
        String requestURI = request.getRequestURI();

        log.info("filter: {}", requestURI);

        // set urls no need to filter
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**"
        };
        
        // check if need filter
        boolean check = check(urls, requestURI);

        // free to go
        if (check) {
            log.info("no need to filter: {}", requestURI);
             filterChain.doFilter(request, response);
             return;
        }

        // check if already logged in
        if(request.getSession().getAttribute("employee") != null) {
            log.info("already logged in, userid: {}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            long id = Thread.currentThread().getId();
            log.info("current thread is {}", id);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("user not logged in");
        // if not logged in, through outputstream to response to client
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * url match
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
           boolean match = PATH_MATCHER.match(url, requestURI);
           if (match) {
               return true;
           }
        }
        return false;
    }
}
