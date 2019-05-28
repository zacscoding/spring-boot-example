## Spring cloud zuul demo  

- <a href="#dynamic-routes">Dynamic routes</a>  

---  

<div id="dynamic-routes"></div>  

## Dynamic routes  

this sample is change to target url by using ZuulFilter.  

> DynamicZuulPathFilter

```aidl
    @Override
    public Object run() throws ZuulException {
        logger.info("DynamicZuulPathFilter::run() is called");
        RequestContext context = RequestContext.getCurrentContext();
        displayRequestContext(context);

        if (isDynamicRequest(context)) {
            logger.info("RouteHost : {} --> {}", context.get(ROUTE_HOST), dynamicRouteService.getCurrentUrl());
            // getting current url
            context.put(ROUTE_HOST, dynamicRouteService.getCurrentUrl());
        }

        return null;
    }
```

for example,  

> first request & response  

```aidl
$ curl -XGET http://localhost:8081/dynamic
Foo
```  

> change target url(DynamicRouteController)  

```aidl
$ curl -XGET http://localhost:8081/dynamic-config/change
http://localhost:8081/dynamic-bar
```  

> request again

```aidl
$ curl -XGET http://localhost:8081/dynamic
Bar
```