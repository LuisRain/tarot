package com.hy.plugin.websocketInstantMsg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import net.sf.json.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ChatServer
  extends WebSocketServer
{
  public ChatServer(int port)
    throws UnknownHostException
  {
    super(new InetSocketAddress(port));
  }
  
  public ChatServer(InetSocketAddress address) {
    super(address);
  }
  
  public void onOpen(WebSocket conn, ClientHandshake handshake) {}
  
  public void onClose(WebSocket conn, int code, String reason, boolean remote)
  {
    userLeave(conn);
  }
  
  public void onMessage(WebSocket conn, String message)
  {
    message = message.toString();
    if ((message != null) && (message.startsWith("FHadminqq313596790")))
      userjoin(message.replaceFirst("FHadminqq313596790", ""), conn);
    if ((message != null) && (message.startsWith("LeaveFHadminqq313596790")))
      userLeave(conn);
    if ((message != null) && (message.contains("fhadmin886"))) {
      String toUser = message.substring(message.indexOf("fhadmin886") + 10, message.indexOf("fhfhadmin888"));
      message = message.substring(0, message.indexOf("fhadmin886")) + "[私信]  " + message.substring(message.indexOf("fhfhadmin888") + 12, message.length());
      ChatServerPool.sendMessageToUser(ChatServerPool.getWebSocketByUser(toUser), message);
      ChatServerPool.sendMessageToUser(conn, message);
    } else {
      ChatServerPool.sendMessage(message.toString());
    }
  }
  
  public void onFragment(WebSocket conn, Framedata fragment) {}
  
  public void onError(WebSocket conn, Exception ex)
  {
    ex.printStackTrace();
  }
  
  public void userjoin(String user, WebSocket conn)
  {
    JSONObject result = new JSONObject();
    result.element("type", "user_join");
    result.element("user", "<a onclick=\"toUserMsg('" + user + "');\">" + user + "</a>");
    ChatServerPool.sendMessage(result.toString());
    String joinMsg = "{\"from\":\"[系统]\",\"content\":\"" + user + "上线了\",\"timestamp\":" + new Date().getTime() + ",\"type\":\"message\"}";
    ChatServerPool.sendMessage(joinMsg);
    result = new JSONObject();
    result.element("type", "get_online_user");
    ChatServerPool.addUser(user, conn);
    result.element("list", ChatServerPool.getOnlineUser());
    ChatServerPool.sendMessageToUser(conn, result.toString());
  }
  
  public void userLeave(WebSocket conn)
  {
    String user = ChatServerPool.getUserByKey(conn);
    boolean b = ChatServerPool.removeUser(conn);
    if (b) {
      JSONObject result = new JSONObject();
      result.element("type", "user_leave");
      result.element("user", "<a onclick=\"toUserMsg('" + user + "');\">" + user + "</a>");
      ChatServerPool.sendMessage(result.toString());
      String joinMsg = "{\"from\":\"[系统]\",\"content\":\"" + user + "下线了\",\"timestamp\":" + new Date().getTime() + ",\"type\":\"message\"}";
      ChatServerPool.sendMessage(joinMsg);
    }
  }
  
  public static void main(String[] args) throws InterruptedException, IOException { org.java_websocket.WebSocketImpl.DEBUG = false;
    int port = 8887;
    ChatServer s = new ChatServer(port);
    s.start();
  }
}


