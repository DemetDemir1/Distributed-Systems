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

import java.util.ArrayList;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;

class Listener extends Thread {
	
	public static ArrayList<String[]> publisher = new ArrayList<String[]>();
	
	
	private static String listenerIP;
	
	public Listener(){
		listenerIP="localhost";
	}
	
	public Listener(String IP){
		listenerIP= IP;
	}
	
	public void run(){
		String [] args = new String[0];
		try{
		main(args);
		}catch(Exception e){
			
		}
	}

    public static void main(String []args) throws JMSException {

        String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        //String host = env("ACTIVEMQ_HOST", "localhost");
        String host = env("ACTIVEMQ_HOST", listenerIP);
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "61616"));
        String destination = arg(args, 0, "event");

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);

        Connection connection = factory.createConnection(user, password);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new ActiveMQTopic(destination);

        MessageConsumer consumer = session.createConsumer(dest);
        long start = System.currentTimeMillis();
        long count = 1;
        System.out.println("Waiting for messages...");
        while(true) {
            Message msg = consumer.receive();
            if( msg instanceof  TextMessage ) {
                String body = ((TextMessage) msg).getText();
                if( "SHUTDOWN".equals(body)) {
                    long diff = System.currentTimeMillis() - start;
                    //System.out.println(String.format("Received %d in %.2f seconds", count, (1.0*diff/1000.0)));
                    break;
                } else {
                    if( count != msg.getIntProperty("id") ) {
                        //System.out.println("mismatch: "+count+"!="+msg.getIntProperty("id"));
                    }
                    count = msg.getIntProperty("id");

                    if( count == 0 ) {
                        start = System.currentTimeMillis();
                    }
                    if( count % 1000 == 0 ) {
                       // System.out.println(String.format("Received %d messages.", count));
                    }
                    count ++;
                    //System.out.println(msg);
                    
                    //Sichere Publisher Messages
                    String[] pair = new String[2] ;
                    //System.out.println(msg.getJMSMessageID());
                    pair[0]= msg.getJMSMessageID().substring(0,27);
                    
                    
                   // System.out.println(pair[0]);
                    
                    pair[1]= ((TextMessage) msg).getText();
                    //System.out.println(pair[1]);
                    
                    
                    if(publisher.size()>0){
                    	//System.out.println("IN IF");
	                    for(int i=0;i<publisher.size();i++){
	                    	if(publisher.get(i)[0].equals(pair[0])){
	                    		//System.out.println("IN IF");
	                    		publisher.get(i)[1]=pair[1];
	                    		break;
	                    	}
	                    	if(i==publisher.size()-1){
	                    		publisher.add(pair);
	                    	}
	                    }
                    }else{
                    	//System.out.println("IN ELSE");
                    	publisher.add(pair);
                    }
                   // System.out.println("P-Size: "+publisher.size());
                   /*
                    for(int i=0;i<publisher.size();i++){
                    	System.out.println(publisher.get(i)[0]);
                    	System.out.println(publisher.get(i)[1]);
                    }
                    
                   //*******************************************
                    * 
                    */
                }

            } else {
                System.out.println("Unexpected message type: "+msg.getClass());
            }
        }
        connection.close();
    }

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
    
    public static ArrayList<String[]> getPublisher(){
    	return publisher;
    }
    
    public static int getSize(){
    	return publisher.size();
    }
    public static String getMessage(){
    	String mes="";
    	for(int i=0;i<publisher.size();i++){
    		mes+="Publisher "+i+": \n";
    		mes+=publisher.get(i)[1];
    		mes+="\n";
    	}
    	return mes;
    }
    
}
