## 登录请求流程

![img.png](img.png)

1. 用户向未获得授权资源发出未经身份验证的请求;
2. `FilterSecurityInterceptor` 抛出 `AccessDeniedException` 异常;
3. `ExceptionTranslationFilter` 启动身份验证，使用`AuthenticationEntryPoint` 重定向到登录页面;