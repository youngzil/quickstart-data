package org.quickstart.jstorm.example4.utils;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 各种工具
 */
public class Utils {

  public static void sleep(long milliSeconds) {
    try {
      Thread.sleep(milliSeconds);
    } catch (Exception e) {
      Logging.error(e);
    }
  }

  public static class UID {

    /**
     * UID = JSTROM + yyyyMMddHHmmssSSS + RANDOM_CHAR + IP + NUM
     */

    private static volatile AtomicLong atomicLong = new AtomicLong(0l);
    private static final String[] RANDOM_CHAR_ARR = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
        "b",
        "b", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y", "z"};

    private static long NUM() {
      if (atomicLong.get() >= Long.MAX_VALUE) {
        atomicLong.set(0l);
      }
      return atomicLong.getAndIncrement();
    }

    private static String IP() {
      String ip = "127.0.0.1";
      try {
        ip = Inet4Address.getLocalHost().getHostAddress();
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
      return ip;
    }

    public static final String ID() {
      StringBuilder idBuilder = new StringBuilder("JSTROM").append("_");
      idBuilder.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())).append("_");
      Random random = new Random();
      for (int i = 0; i < 8; i++) {
        idBuilder.append(RANDOM_CHAR_ARR[random.nextInt(RANDOM_CHAR_ARR.length - 1)]);
      }
      idBuilder.append("_").append(IP());
      idBuilder.append("_").append(NUM());
      return idBuilder.toString();
    }

  }

  public static String formatException(Throwable throwable) {
    if (throwable == null) {
      return null;
    }
    return throwable.getMessage();
  }

}
