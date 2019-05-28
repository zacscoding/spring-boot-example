package demo.dynamic.rest;

import demo.dynamic.route.DynamicRouteLocator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dynamic route urls controller
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/dynamic-config")
public class DynamicRouteController {

    private final DynamicRouteLocator dynamicRouteService;

    /**
     * Get current url about "dynamic" proxy
     */
    @GetMapping
    public String getCurrentURL() {
        return dynamicRouteService.getCurrentUrl().toExternalForm();
    }

    /**
     * Change to proxy URL
     */
    @GetMapping("/change")
    public String changeURL() {
        dynamicRouteService.changeUrl();
        return dynamicRouteService.getCurrentUrl().toExternalForm();
    }
}
