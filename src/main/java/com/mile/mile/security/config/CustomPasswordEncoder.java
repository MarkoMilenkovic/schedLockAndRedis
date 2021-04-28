package com.mile.mile.security.config;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.crypto.hash.format.Shiro1CryptFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

public class CustomPasswordEncoder implements PasswordEncoder {

    private DefaultPasswordService defaultPasswordService;

    public CustomPasswordEncoder(DefaultPasswordService defaultPasswordService) {
        this.defaultPasswordService = defaultPasswordService;
    }

//    @PostConstruct
//    public void init() {
//        defaultPasswordService = new DefaultPasswordService();
//        defaultPasswordService.setHashFormat(new Shiro1CryptFormat());
//        defaultPasswordService.setHashService(new DefaultHashService());
//        defaultPasswordService.setHashFormatFactory(new DefaultHashFormatFactory());
//    }

    @Override
    public String encode(CharSequence charSequence) {
//        DefaultPasswordService defaultPasswordService = new DefaultPasswordService();
//        defaultPasswordService.setHashFormat(new Shiro1CryptFormat());
//        defaultPasswordService.setHashService(new DefaultHashService());
//        defaultPasswordService.setHashFormatFactory(new DefaultHashFormatFactory());
        return defaultPasswordService.encryptPassword(charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
//        DefaultPasswordService defaultPasswordService = new DefaultPasswordService();
//        defaultPasswordService.setHashFormat(new Shiro1CryptFormat());
//        defaultPasswordService.setHashService(new DefaultHashService());
//        defaultPasswordService.setHashFormatFactory(new DefaultHashFormatFactory());
        return defaultPasswordService.passwordsMatch(charSequence, s);
    }
}
