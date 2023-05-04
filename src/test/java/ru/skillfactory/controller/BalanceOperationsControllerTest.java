package ru.skillfactory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skillfactory.service.UsersService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(BalanceOperationsController.class)
class BalanceOperationsControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private UsersService usersService;

    @Test
    void getBalanceTest() throws Exception {

        String uri = "/getBalance/1";
        int statusExpect = 200;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).param("id", "1")).andReturn();
        int result = mvcResult.getResponse().getStatus();

        assertEquals(statusExpect, result);
    }

    @Test
    void putMoneyTest() throws Exception {

        String uri = "/putMoney/1/500";
        int statusExpect = 200;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .param("id", "1")
                .param("sum", "500")).andReturn();
        int result = mvcResult.getResponse().getStatus();

        assertEquals(statusExpect, result);
    }

    @Test
    void takeMoneyTest() throws Exception {

        String uri = "/takeMoney/1/500";
        int statusExpect = 200;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .param("id", "1")
                .param("sum", "500")).andReturn();
        int result = mvcResult.getResponse().getStatus();

        assertEquals(statusExpect, result);
    }

    @Test
    void getOperationListTest() throws Exception {

        String uri = "/getOperationList?user_id=1&start=2023-04-01&end=2023-05-02";
        int statusExpect = 200;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .param("user_id", "1")
                .param("start", "2023-04-01")
                .param("end", "2023-05-02")).andReturn();
        int result = mvcResult.getResponse().getStatus();

        assertEquals(statusExpect, result);
    }

    @Test
    void transferMoneyTest() throws Exception {

        String uri = "/transferMoney?fromId=1&toId=2&amount=100";
        int statusExpect = 200;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .param("fromId", "1")
                .param("toId", "2")
                .param("amount", "100")).andReturn();
        int result = mvcResult.getResponse().getStatus();

        assertEquals(statusExpect, result);
    }
}