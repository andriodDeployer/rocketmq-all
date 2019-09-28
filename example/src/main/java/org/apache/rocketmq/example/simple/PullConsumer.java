/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.example.simple;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PullConsumer {
    private static final Map<MessageQueue, Long> OFFSE_TABLE = new HashMap<MessageQueue, Long>();

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("please_rename_unique_group_name_5");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.start();

        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("TopicTest");//获取broker-a的所有messageQueue信息。


        MessageQueue mq1 = mqs.iterator().next();

        PullResult pullResult1 =
                consumer.pullBlockIfNotFound(mq1, null, getMessageQueueOffset(mq1), 1);//如果是拉的话，有客户端自己维护offset信息
        long nextBeginOffset = pullResult1.getNextBeginOffset();
        System.out.println(nextBeginOffset);

        PullResult pullResult2 =
                consumer.pullBlockIfNotFound(mq1, null, getMessageQueueOffset(mq1), 1);//如果是拉的话，有客户端自己维护offset信息
        long nextBeginOffset2 = pullResult2.getNextBeginOffset();
        System.out.println(nextBeginOffset2);



       // System.out.printf("%s%n", pullResult2);

        try {
            Thread.sleep(1000 * 1000);
        }catch (Exception e){

        }
        consumer.shutdown();
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSE_TABLE.get(mq);
        if (offset != null)
            return offset;

        return 0;
    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSE_TABLE.put(mq, offset);
    }

}
