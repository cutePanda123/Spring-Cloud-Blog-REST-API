package com.panda.article.stream;

import com.panda.pojo.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(CustomStreamChannel.class)
public class StreamServiceImpl implements StreamService{
    @Autowired
    private CustomStreamChannel customStreamChannel;

    @Override
    public void sendMsg() {
        AppUser user = new AppUser();
        user.setId("10101");
        user.setNickname("testUser");

        customStreamChannel.output().send(MessageBuilder.withPayload(user).build());
    }
}
