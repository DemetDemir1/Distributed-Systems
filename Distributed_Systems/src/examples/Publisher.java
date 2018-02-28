/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples;

import java.lang.reflect.Array;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;

class Publisher {
	public static int i=0;
	private MessageProducer producer;
	private Session session;
	private Connection connection;
		
	
	public Publisher()throws JMSException{
		String []args=new String[0];
		String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "61616"));
        String destination = arg(args, 0, "event");
        
        
        
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);

        connection = factory.createConnection(user, password);
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new ActiveMQTopic(destination);
        producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}
	public Publisher(String IP)throws JMSException{
		String []args=new String[0];
		String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        //String host = env("ACTIVEMQ_HOST", "localhost");
        String host = env("ACTIVEMQ_HOST", IP);
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "61616"));
        String destination = arg(args, 0, "event");
        
        
        
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);

        connection = factory.createConnection(user, password);
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new ActiveMQTopic(destination);
        producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}
	
	
/*
    public static void main(String []args) throws JMSException {

        

        int messages = 10000;
        int size = 256;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body = "";
        for( int i=0; i < size; i ++) {
            body += DATA.charAt(i%DATA.length());
        }

        

        for( int i=1; i <= messages; i ++) {
            TextMessage msg = session.createTextMessage(body);
            msg.setIntProperty("id", i);
            producer.send(msg);
            if( (i % 1000) == 0) {
                System.out.println(String.format("Sent %d messages", i));
            }
        }




    }
*/

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }
    
    public void sendMessage(String body) throws JMSException {
    		i++;
    		TextMessage msg = session.createTextMessage(body);
            msg.setIntProperty("id", i);
            producer.send(msg);
    }
    
    public void Shutdown() throws JMSException{
        producer.send(session.createTextMessage("SHUTDOWN"));
        connection.close();
    }
    

}