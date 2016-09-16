package mq;

import java.io.IOException;

/*
MQSampleGet - uses MQ Base Java classes V6
It is a trimmed down version of MQSample (without the Put portion) from:
  C:\Program Files\IBM\WebSphere MQ\tools\wmqjava\samples
  
*/

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

/**
 * Simple example program - get
 */
public class MQ {

 
  private static final String qManager = "TestQM";
  // and define the name of the Queue
  private static final String qName = "order1";
  static String msg = "Hello World, WelCome to MQ.";
  static String msg1 = "Hello World";
  
public static void main(String args[]) {
    try {
      
      System.out.println("Connecting to queue manager: " + qManager);
      MQQueueManager qMgr = new MQQueueManager(qManager);

      
      int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT;


      System.out.println("Accessing queue: " + qName);
      MQQueue queue = qMgr.accessQueue(qName, openOptions);

      MQMessage putMessage = new MQMessage();
      putMessage.writeUTF(msg);
      
      //specify the message options...
      MQPutMessageOptions pmo = new MQPutMessageOptions(); 
      // accept 
      // put the message on the queue
      queue.put(putMessage, pmo);
      System.out.println("Message is put on MQ.");
      System.out.println("Closing the queue");
      queue.close();

      System.out.println("Disconnecting from the Queue Manager");
      qMgr.disconnect();
      System.out.println("Done!");
    }
    catch (MQException ex) {
      System.out.println("A WebSphere MQ Error occured : Completion Code " + ex.completionCode
          + " Reason Code " + ex.reasonCode);
      ex.printStackTrace();
      for (Throwable t = ex.getCause(); t != null; t = t.getCause()) {
        System.out.println("... Caused by ");
        t.printStackTrace();
      }

    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}

// end of file

