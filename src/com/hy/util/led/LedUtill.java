package com.hy.util.led;

import com.hy.entity.product.Merchant;
import java.io.PrintStream;
import java.util.List;

public class LedUtill
{
  public static int toLed(List<Merchant> merchantListP)
  {
    int hProgram = Led.CreateProgram(192, 32, 1);
    Led.AddProgram(hProgram, 1, 0, 1);
    Led.AddImageTextArea(hProgram, 1, 1, 0, 0, 64, 32, 1);
    int i = 5;
    if (merchantListP.size() > 46) {
      //System.out.println("客户组选择过多");
    } else {
      for (int k = 0; k < merchantListP.size(); k++) {
        Led.AddMultiLineTextToImageTextArea(hProgram, 1, 1, 0, ((Merchant)merchantListP.get(k)).getMerchantName(), "宋体", 9, 255, 0, 0, 0, 1, 4, 2, 1, 1);
        Led.NetWorkSend("192.168.1." + k, hProgram);
        i++;
      }
    }
    Led.DeleteProgram(hProgram);
    return i;
  }
  
  public static void main(String[] args)
  {
    int hProgram = Led.CreateProgram(64, 32, 1);
    Led.AddProgram(hProgram, 1, 0, 1);
    Led.AddImageTextArea(hProgram, 1, 1, 0, 0, 64, 32, 1);
    Led.AddMultiLineTextToImageTextArea(hProgram, 1, 1, 0, "hello world", "Tahoma", 9, 255, 0, 0, 0, 1, 4, 2, 1, 1);
    
    Led.AddProgram(hProgram, 2, 0, 1);
    Led.AddImageTextArea(hProgram, 2, 1, 0, 0, 64, 16, 1);
    Led.AddSinglelineTextToImageTextArea(hProgram, 2, 1, 0, "welcome to listen vision", "Tahoma", 12, 255, 0, 0, 0, 6, 4, 1);
    
    Led.AddDigitalClockArea(hProgram, 2, 2, 0, 16, 64, 16, "Tahoma", 9, 255, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 255, 0, 255, 2, 255);
    
    //System.out.println(Led.NetWorkSend("192.168.1.188", hProgram));
    Led.DeleteProgram(hProgram);
  }
}


