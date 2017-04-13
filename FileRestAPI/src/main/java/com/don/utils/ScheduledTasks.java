package com.don.utils;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.don.model.FileInfo;
import com.don.service.FileService;
import com.don.service.MailService;

@Component
//@ConfigurationProperties//("email")
@ConfigurationProperties
public class ScheduledTasks {

    @Autowired
    MailService mailservice;
    
    @Autowired
	FileService fileService;
    
    @Value("${sendTo}")
    String sendTo;
    
    @Value("${topic}")
    String topic;
    
    //@Scheduled(initialDelay=10000, fixedRate=10000)
    @Scheduled(cron="0 0 0/1 1/1 * ? *") //At every hour exactly
    public void sendEmail() throws MessagingException {
    	String body = getBody();
    	mailservice.send(sendTo, topic, body);
    	//System.out.println(body);
    }

	private String getBody() {
		StringBuilder sb = new StringBuilder();
		List<FileInfo> list = fileService.getLastHour();
		for (FileInfo f : list) {
			sb.append(f.toString()+"\r\n");
		}
		return sb.toString();
	}
    
}