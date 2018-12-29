package demo.index;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import demo.common.BaseControllerTest;
import org.junit.Test;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
public class IndexControllerTest extends BaseControllerTest {

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/api/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_links.events").exists());
    }
}
