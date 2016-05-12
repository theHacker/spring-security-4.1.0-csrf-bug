package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.Test;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(classes = {MvcConfig.class, SecurityConfig.class})
})
public class MyControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    protected Filter springSecurityFilterChain;

    @Test
    public void testMe() throws Exception {
        MyController myController = new MyController();

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(myController)
                .addFilters(springSecurityFilterChain)
                .build();

        mockMvc.perform(post("/foo.html")     // Spring Security default requires a CSRF token for POST
                .with(csrf()))                // Yes, we have a valid token
                .andExpect(status().is(302)); // expect redirection since we are not logged in
    }

}