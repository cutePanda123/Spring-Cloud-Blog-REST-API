package com.panda.utils;

import com.azure.communication.sms.SmsClient;
import com.azure.communication.sms.SmsClientBuilder;
import com.azure.communication.sms.models.SmsSendResult;
import com.panda.utils.extend.AzureResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SMSUtils {
    Logger logger = LoggerFactory.getLogger(SMSUtils.class);

    @Autowired
    private AzureResource azureResource;

    String connectionString = azureResource.getCommunicationServiceConnectionString();
    private final String fromPhoneNumber = "";

    SmsClient smsClient = new SmsClientBuilder()
            .connectionString(connectionString)
            .buildClient();

    public void sendSMS(String toPhoneNumber, String code) {
        SmsSendResult sendResult = smsClient.send(
                fromPhoneNumber,
                toPhoneNumber,
                "Your security code is: " + code + ". It expires in 10 minutes. Do not share this code with anyone.");
        logger.info("Message Id: " + sendResult.getMessageId());
        logger.info("Recipient Number: " + sendResult.getTo());
        logger.info("Send Result Successful:" + sendResult.isSuccessful());
    }
}
