package com.hy.util.led;

import java.io.PrintStream;

public class Led {
  static { //System.out.println("111111");
    System.loadLibrary("lv_led");
    //System.out.println("222222222222222");
  }
  
  public static native int CreateProgram(int paramInt1, int paramInt2, int paramInt3);
  
  public static native int AddProgram(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public static native int AddImageTextArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
  
  public static native int AddFileToImageTextArea(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4, int paramInt5, int paramInt6);
  
  public static native int AddMultiLineTextToImageTextArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, String paramString2, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14);
  
  public static native int AddStaticTextToImageTextArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, String paramString2, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12);
  
  public static native int AddSinglelineTextToImageTextArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, String paramString2, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12);
  
  public static native int AddDigitalClockArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, String paramString, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16, int paramInt17, int paramInt18, int paramInt19, int paramInt20, int paramInt21, int paramInt22, int paramInt23, int paramInt24, int paramInt25);
  
  public static native void DeleteProgram(int paramInt);
  
  public static native int NetWorkSend(String paramString, int paramInt);
  
  public static native int SetBasicInfo(String paramString, int paramInt1, int paramInt2, int paramInt3);
  
  public static native int SetOEDA(String paramString, int paramInt1, int paramInt2);
  
  public static native int AdjustTime(String paramString);
  
  public static native int PowerOnOff(String paramString, int paramInt);
  
  public static native int TimePowerOnOff(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public static native int SetBrightness(String paramString, int paramInt);
  
  public static native int LedTest(String paramString, int paramInt);
  
  public static native int SetLedCommunicationParameter(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);
}


