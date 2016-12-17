# spring-boost-enabler
Annotation that adds spring-boost features to a Spring Boost application.


@EnableSpringBoost

<groupId>com.shedhack.tool</groupId>
<artifactId>spring-boost-enabler</artifactId>
<version>1.0.0-SNAPSHOT</version>

trace.interceptor.endpoint:requests

@Value("${exception.interceptor.queue.size:50}")
private int queueSize = 50;

@Value("${exception.interceptor.stacktrace:false}")
private boolean showTrace;\
    
    
@Value("${trace.interceptor.queue.size:50}")


@Value("${exception.interceptor.stacktrace:true}")
private boolean showTrace;

@Value("${exception.interceptor.endpoint:exceptions}")
private String endpoint;



@Value("${spring.application.name}")
private String appName;

@Value("${spring.boost.api.root}:/")
private String apiRoot;

@Value("${spring.boost.api.package}:/")
private String apiPackage;