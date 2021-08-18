package com.jay.instagram.kafka;


import com.alibaba.fastjson.JSON;
import com.jay.instagram.bean.Post;
import com.jay.instagram.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {
    @Autowired
    PostService postService;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    // 消费监听
    @KafkaListener(topics = {"post"})
    public void onPost(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        //System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
        Post post = JSON.parseObject((String) record.value(), Post.class);
        postService.createPost(post);
        kafkaTemplate.send("post-after", JSON.toJSON(post).toString());
    }

    @KafkaListener(topics = {"delete-post"})
    public void onDeletePost(ConsumerRecord<?, ?> record){
        String value = (String) record.value();
        postService.deletePost(Long.parseLong(value));
    }
}
