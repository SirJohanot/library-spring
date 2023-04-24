package com.patiun.libraryspring.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookOrderRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class BookOrderRestControllerTest {

    private static final String BASE_URL = "/orders";

    @MockBean
    private BookOrderService service;

    private final MockMvc mvc;

    @Autowired
    public BookOrderRestControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    public void testApproveOrderShouldInvokeTheApproveOrderMethodOfTheServiceOnce() throws Exception {
        //given
        Integer orderId = 57;
        //then
        mvc.perform(patch(BASE_URL + "/" + orderId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .approveOrderById(orderId);
    }

}
