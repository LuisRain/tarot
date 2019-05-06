package com.hy.plugin.websocketInstantMsg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.java_websocket.WebSocket;

public class ChatServerPool
{
  private static final Map<WebSocket, String> userconnections = new HashMap();
  
  public static String getUserByKey(WebSocket conn)
  {
    return (String)userconnections.get(conn);
  }
  
  public static WebSocket getWebSocketByUser(String user)
  {
    Set<WebSocket> keySet = userconnections.keySet();
    synchronized (keySet) {
      for (WebSocket conn : keySet) {
        String cuser = (String)userconnections.get(conn);
        if (cuser.equals(user)) {
          return conn;
        }
      }
    }
    return null;
  }
  
  public static void addUser(String user, WebSocket conn)
  {
    userconnections.put(conn, user);
  }
  
  public static Collection<String> getOnlineUser()
  {
    List<String> setUsers = new ArrayList();
    Collection<String> setUser = userconnections.values();
    for (String u : setUser) {
      setUsers.add("<a onclick=\"toUserMsg('" + u + "');\">" + u + "</a>");
    }
    return setUsers;
  }
  
  public static boolean removeUser(WebSocket conn)
  {
    if (userconnections.containsKey(conn)) {
      userconnections.remove(conn);
      return true;
    }
    return false;
  }
  
  public static void sendMessageToUser(WebSocket conn, String message)
  {
    if ((conn != null) && (userconnections.get(conn) != null)) {
      conn.send(message);
    }
  }
  
  public static void sendMessage(String message)
  {
    Set<WebSocket> keySet = userconnections.keySet();
    synchronized (keySet) {
      for (WebSocket conn : keySet) {
        String user = (String)userconnections.get(conn);
        if (user != null) {
          conn.send(message);
        }
      }
    }
  }
}


