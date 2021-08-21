package com.panda.article.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface CustomStreamChannel {
    String OUTPUT = "myOutput";
    String INPUT = "myInput";

    @Output(CustomStreamChannel.OUTPUT)
    MessageChannel output();

    @Input(CustomStreamChannel.INPUT)
    SubscribableChannel input();
}
