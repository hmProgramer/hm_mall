package com.hm.auth;

import com.hm.auth.entity.UserInfo;
import com.hm.auth.utils.JwtUtils;
import com.hm.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "2");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTYwMTUyNzU1OH0.BAphMod9vxT0-1w9E9jeosDB2MpG90jtvghY7SeqL_GQ17a0iCli0OiDjDqqIvbmf2lP33RF9VvxyORiOx0E30F80Hwes2VSFrLySprzc0tYjg8SdMMojEreSxuaeCZebTF0gDMINCpDXU6sEkK4ObvhsHBvKd6hIHdKGeMUWXU";

        // 解析token
        UserInfo user = JwtUtils.getUserInfo(publicKey,token);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getName());
    }
}
