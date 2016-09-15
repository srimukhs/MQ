package mq;



import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import java.sql.*;
import java.util.StringTokenizer;
/**
 * Simple example program - get
 */
public class MQSampleGet {

  
  private static final String qManager = "TestQM";
  // and define the name of the Queue
  private static final String qName = "order";
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
  static final String DB_URL = "jdbc:mysql://localhost/test";
  static final String USER = "root";
  static final String PASS = "srimukh";

  /**
   * Main entry point
   * 
   * @param args - command line arguments (ignored)
   */
  public static void main(String args[]) {
	  Connection conn = null;
	   Statement pstmt = null;
    try {
      // Create a connection to the QueueManager
      System.out.println("Connecting to queue manager: " + qManager);
      MQQueueManager qMgr = new MQQueueManager(qManager);
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");
      
      //STEP 4: Execute a query
      System.out.println("Inserting records into the table...");
     
      
      // Set up the options on the queue we wish to open
      int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT;

      // Now specify the queue that we wish to open and the open options
      System.out.println("Accessing queue: " + qName);
      MQQueue queue = qMgr.accessQueue(qName, openOptions);

      // Now get the message back again. First define a WebSphere MQ
      // message
      // to receive the data
      MQMessage rcvMessage = new MQMessage();
     
      // Specify default get message options
      MQGetMessageOptions gmo = new MQGetMessageOptions();

// The following is needed to provide backward compatibility with MQ V6 Java code 
// to properly handle the "format" property of MQHRF2 from JMS messages
      gmo.options = gmo.options + MQConstants.MQGMO_PROPERTIES_FORCE_MQRFH2;
              
      // Get the message off the queue.
      System.out.println("Getting the message");
      queue.get(rcvMessage, gmo);

// The following 4 statements are to overcome a runtime problem with readUTF:
// An IOException occured whilst writing to the message buffer: java.io.EOFException: 
// MQJE086: End of file exception readUTF
//      String msgText = rcvMessage.readUTF();
int strLen = rcvMessage.getMessageLength();
byte[] strData = new byte[strLen];
rcvMessage.readFully(strData);
String msgText = new String(strData);

      System.out.println("The message is: " + msgText);
      String delimiter = ",";
      String[] temp = msgText.split(delimiter);
      
       
       String str = temp[0];   
      String str1 = temp[1]; 
      String str2 = temp[2]; 
      String str3 = temp[3]; 
      String str4 = temp[4]; 
      String str5 = temp[5]; 
		
      
	PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders (orderno,ordername,itemno,itemdesc,shipdate,orderdate) VALUE(?,?,?,?,?,?)");
    	
	    stmt.setString(1, str );
    	stmt.setString(2, str1 );
    	stmt.setString(3, str2 );
    	stmt.setString(4, str3 );
    	stmt.setString(5, str4 );
    	stmt.setString(6, str5 );
    	stmt.executeUpdate();

      String msgFormat = rcvMessage.format;
      System.out.println("The format is: " + msgFormat);

      // Close the queue
      System.out.println("Closing the queue");
      queue.close();

      // Disconnect from the QueueManager
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
      }}

    
    catch (java.io.IOException ex) {
      System.out.println("An IOException occured whilst writing to the message buffer: " + ex);
    }
    catch(SQLException se){
        //Handle errors for JDBC
        se.printStackTrace();
     }catch(Exception e){
        //Handle errors for Class.forName
        e.printStackTrace();
     }finally{
        //finally block used to close resources
        try{
           if(pstmt!=null)
              conn.close();
        }catch(SQLException se){
        }// do nothing
        try{
           if(conn!=null)
              conn.close();
        }catch(SQLException se){
           se.printStackTrace();
        }//end finally try

  }}}


// end of file
