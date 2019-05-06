package com.hy.util;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code39Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

public class OneBarcodeUtil
{
  public static void createOneBarcodeOfNumber(String savePath, String paramString, String imgSuffix)
  {
    try
    {
      JBarcode localJBarcode = new JBarcode(EAN13Encoder.getInstance(), 
        WidthCodedPainter.getInstance(), 
        EAN13TextPainter.getInstance());
      
      BufferedImage localBufferedImage = localJBarcode.createBarcode(paramString);
      FileOutputStream localFileOutputStream = new FileOutputStream(
        savePath + paramString + "." + imgSuffix);
      ImageUtil.encodeAndWrite(localBufferedImage, imgSuffix, 
        localFileOutputStream, 96, 96);
      localFileOutputStream.close();
    }
    catch (Exception localException) {
      localException.printStackTrace();
    }
  }
  
  public static void createOneBarcodeOfString(String savePath, String paramString, String imgSuffix)
  {
    try
    {
      JBarcode localJBarcode = new JBarcode(EAN13Encoder.getInstance(), 
        WidthCodedPainter.getInstance(), 
        EAN13TextPainter.getInstance());
      localJBarcode.setEncoder(Code39Encoder.getInstance());
      localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
      localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());
      localJBarcode.setShowCheckDigit(false);
      BufferedImage localBufferedImage = localJBarcode.createBarcode(paramString);
      FileOutputStream localFileOutputStream = new FileOutputStream(
        savePath + paramString + "." + imgSuffix);
      ImageUtil.encodeAndWrite(localBufferedImage, imgSuffix, 
        localFileOutputStream, 96, 96);
      localFileOutputStream.close();
    }
    catch (Exception localException) {
      localException.printStackTrace();
    }
  }
  
  public static void main(String[] paramArrayOfString)
  {
    String[] bcs = { "694819580051", "690289022645", "693415831417", "694721080008", 
      "695655340030", "691478208238", "692530377390", "693794230975", "693794230065", 
      "692155558129", "692530377420", "691198801489", "692116851128", "692621537289", "691131663750", 
      "692397611117", "692530377310", "691001901002", "690123630027", "694149910081", "694149911212", 
      "693888888883", "695443271019", "692534730058", "692116850925", "693415831401", "693415831417" };
    for (int i = 0; i < bcs.length; i++) {
      createOneBarcodeOfNumber("d:\\images\\", bcs[i], "jpeg");
    }
  }
}


