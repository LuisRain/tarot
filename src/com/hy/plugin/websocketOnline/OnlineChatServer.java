package com.hy.plugin.websocketOnline;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import net.sf.json.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class OnlineChatServer
  extends WebSocketServer
{
  public OnlineChatServer(int port)
    throws UnknownHostException
  {
    super(new InetSocketAddress(port));
  }
  
  public OnlineChatServer(InetSocketAddress address) {
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
    if ((message != null) && (message.startsWith("[join]")))
      userjoin(message.replaceFirst("\\[join\\]", ""), conn);
    if ((message != null) && (message.startsWith("[goOut]"))) {
      goOut(message.replaceFirst("\\[goOut\\]", ""));
    } else if ((message != null) && (message.startsWith("[leave]"))) {
      userLeave(conn);
    } else if ((message != null) && (message.startsWith("[changeMenu]"))) {
      changeMenu(conn);
    } else if ((message != null) && (message.startsWith("[count]"))) {
      getUserCount(conn);
    } else if ((message != null) && (message.startsWith("[getUserlist]"))) {
      getUserList(conn);
    } else {
      OnlineChatServerPool.sendMessageToUser(conn, message);
    }
  }
  
  public void onFragment(WebSocket conn, Framedata fragment) {}
  
  public void onError(WebSocket conn, Exception ex) {}
  
  public void userjoin(String user, WebSocket conn)
  {
    if (OnlineChatServerPool.getWebSocketByUser(user) == null) {
      OnlineChatServerPool.addUser(user, conn);
    } else {
      goOut(conn, "goOut");
    }
  }
  
  public void goOut(String user)
  {
    goOut(OnlineChatServerPool.getWebSocketByUser(user), "thegoout");
  }
  
  public void goOut(WebSocket conn, String type)
  {
    JSONObject result = new JSONObject();
    result.element("type", type);
    result.element("msg", "goOut");
    OnlineChatServerPool.sendMessageToUser(conn, result.toString());
  }
  
  public void userLeave(WebSocket conn)
  {
    OnlineChatServerPool.removeUser(conn);
  }
  
  public void changeMenu(WebSocket conn)
  {
    if (OnlineChatServerPool.removeUser(conn)) {
      JSONObject result = new JSONObject();
      result.element("type", "changeMenu");
      result.element("msg", "changeMenu");
      OnlineChatServerPool.sendMessageToUser(conn, result.toString());
    }
  }
  
  public void getUserCount(WebSocket conn)
  {
    JSONObject result = new JSONObject();
    result.element("type", "count");
    result.element("msg", OnlineChatServerPool.getUserCount());
    OnlineChatServerPool.sendMessageToUser(conn, result.toString());
  }
  
  public void getUserList(WebSocket conn)
  {
    JSONObject result = new JSONObject();
    result.element("type", "userlist");
    result.element("list", OnlineChatServerPool.getOnlineUser());
    OnlineChatServerPool.sendMessageToUser(conn, result.toString());
  }
  
  public static void main(String[] args) throws InterruptedException, IOException {
    org.java_websocket.WebSocketImpl.DEBUG = false;
    int port = 8887;
    OnlineChatServer s = new OnlineChatServer(port);
    s.start();
  }
}


