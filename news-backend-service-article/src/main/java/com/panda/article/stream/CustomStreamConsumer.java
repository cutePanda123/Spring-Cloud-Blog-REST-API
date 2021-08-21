package com.panda.article.stream;

import com.panda.pojo.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(CustomStreamChannel.class)
public class CustomStreamConsumer {
    @Autowired
    private CustomStreamChannel customStreamChannel;

    @StreamListener(CustomStreamChannel.INPUT)
    public void receiveMsg(AppUser user) {
        System.out.println(user.getNickname());
    }
}
