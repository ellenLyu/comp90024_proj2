package com.comp90024.proj2;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EncryptorTest {

    @Autowired
    StringEncryptor encryptor;

    @Test
    public void encrypt() {
        String localhost = "";
        String url = "";
        String username = "";
        String password = "";

        System.out.println(encryptor);
        Stream.of(localhost, url, username, password)
                .forEach(s -> System.out.println(encryptor.encrypt(s)));

    }

}
