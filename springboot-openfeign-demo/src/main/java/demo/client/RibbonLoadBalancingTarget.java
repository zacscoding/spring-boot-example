/**
 * Copyright 2012-2019 The Feign Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package demo.client;

import static com.netflix.client.ClientFactory.getNamedLoadBalancer;
import static feign.Util.checkNotNull;
import static java.lang.String.format;

import java.net.URI;
import java.util.Iterator;

import com.netflix.loadbalancer.AbstractLoadBalancer;
import com.netflix.loadbalancer.Server;

import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RibbonLoadBalancingTarget<T> implements Target<T> {

    private final String name;
    private final String scheme;
    private final String path;
    private final Class<T> type;
    private final AbstractLoadBalancer lb;

    public static <T> RibbonLoadBalancingTarget<T> create(Class<T> type, String url) {
        URI asUri = URI.create(url);
        return new RibbonLoadBalancingTarget<>(type, asUri.getScheme(), asUri.getHost(), asUri.getPath());
    }

    public static <T> RibbonLoadBalancingTarget<T> create(Class<T> type, String url, AbstractLoadBalancer lb) {
        URI asUri = URI.create(url);
        return new RibbonLoadBalancingTarget<>(type, asUri.getScheme(), asUri.getHost(), asUri.getPath(), lb);
    }

    protected RibbonLoadBalancingTarget(Class<T> type, String scheme, String name, String path) {
        this(type, scheme, name, path, AbstractLoadBalancer.class.cast(getNamedLoadBalancer(name)));
    }

    protected RibbonLoadBalancingTarget(Class<T> type, String scheme, String name,
                                        String path, AbstractLoadBalancer lb) {

        this.type = checkNotNull(type, "type");
        this.scheme = checkNotNull(scheme, "scheme");
        this.name = checkNotNull(name, "name");
        this.path = checkNotNull(path, "path");
        this.lb = lb;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String url() {
        return String.format("%s://%s", scheme, path);
    }

    /**
     * current load balancer for the target.
     */
    public AbstractLoadBalancer lb() {
        return lb;
    }

    @Override
    public Request apply(RequestTemplate input) {
        Server currentServer = lb.chooseServer(extractLoadBalancerKey(input));
        // null 처리
        String url;
        if (currentServer == null) {
            url = format("%s://%s%s", scheme, "127.0.0.1", path);
        } else {
            url = format("%s://%s%s", scheme, currentServer.getHostPort(), path);
        }
        input.target(url);
        try {
            return input.request();
        } finally {
            lb.getLoadBalancerStats().incrementNumRequests(currentServer);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RibbonLoadBalancingTarget) {
            RibbonLoadBalancingTarget<?> other = (RibbonLoadBalancingTarget<?>) obj;
            return type.equals(other.type) && name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + type.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CustomLoadBalancingTarget(type=" + type.getSimpleName() + ", name=" + name + ", path=" + path
               + ")";
    }

    private Object extractLoadBalancerKey(RequestTemplate input) {
        Iterator<String> iterator = input.headers().get("Service-Target").iterator();

        final Object lbKey = iterator.hasNext() ? iterator.next() : null;
        logger.info(">> Check load balancer key : {} from Target", lbKey);

        return lbKey;
    }
}
