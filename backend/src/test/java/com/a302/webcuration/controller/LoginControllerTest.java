package com.a302.webcuration.controller;

import com.a302.webcuration.common.BaseControllerTest;
import com.a302.webcuration.domain.Account.AccountDto;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginControllerTest extends BaseControllerTest {


    //---------------------------------로그인-------------------------------------------

    @Test
    public void Login_이메일요청_성공() throws Exception {

        AccountDto.LoginRequest account = new AccountDto.LoginRequest();
        account.setAct("login-request");
        account.setAccountEmail("dntjr4772@naver.com");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void Login_이메일요청_실패_형식오류() throws Exception {

        AccountDto.LoginRequest account = new AccountDto.LoginRequest();
        account.setAct("login-request");
        account.setAccountEmail("ㄹasdkom");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void Login_이메일요청_실패_존재하지않음() throws Exception {

        AccountDto.LoginRequest account = new AccountDto.LoginRequest();
        account.setAct("login-request");
        account.setAccountEmail("none@test.com");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void Login_AuthKey_Off_성공() throws Exception {

        String AuthKey = "awr8nhoh";

        AccountDto.LoginRequest  account = new AccountDto.LoginRequest();
        account.setAct("check-authKey-off");
        account.setAccountEmail("dntjr4772@naver.com");
        account.setAccountAuthKey(AuthKey);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void Login_AuthKey_Off_실패() throws Exception {

        String AuthKey = "gzyiru";

        AccountDto.LoginRequest  account = new AccountDto.LoginRequest();
        account.setAct("check-authKey-off");
        account.setAccountEmail("jason967@naver.com");
        account.setAccountAuthKey(AuthKey);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void Login_AuthKey_On_성공() throws Exception {


        String AuthKey = "n3bpclje";

        AccountDto.LoginRequest  account = new AccountDto.LoginRequest();
        account.setAct("check-authKey-on");
        account.setAccountEmail("dntjr4772@naver.com");
        account.setAccountAuthKey(AuthKey);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void auto_login_성공() throws Exception {

        String AuthKey = "cs9cz1b";
        Long id = 8L;
        String email = "jason967@naver.com";
        AccountDto.LoginRequest  account = new AccountDto.LoginRequest();
        account.setAccountEmail(email);
        account.setAccountId(id);

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLroZzqt7jsnbjthqDtgbAiLCJleHAiOjE2MTE1NzU5NzUsImFjY291bnRJZCI6OCwiYWNjb3VudEVtYWlsIjoiamFzb245NjdAbmF2ZXIuY29tIn0.sWbVaWkClcwxqi6SMr-A64yce14qIYvB__9tHtuDits";

        mockMvc.perform(post("/api/auto-login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+token)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andDo(print());
    }

}