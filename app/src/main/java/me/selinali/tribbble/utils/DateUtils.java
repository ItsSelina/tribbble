package me.selinali.tribbble.utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import me.selinali.tribbble.R;
import me.selinali.tribbble.TribbbleApp;

public class DateUtils {

  public static String formatDate(Date date) {
    long elapsedMillis = new Date().getTime() - date.getTime();
    int quantity;

    if ((int) (elapsedMillis / TimeUnit.DAYS.toMillis(1)) > 0) {
      return DateFormat.getDateInstance().format(date);
    } else if ((quantity = (int) (elapsedMillis / TimeUnit.HOURS.toMillis(1))) > 0) {
      return TribbbleApp.plural(R.plurals.about_hours_ago, quantity);
    } else {
      quantity = (int) (elapsedMillis / TimeUnit.MINUTES.toMillis(1));
      return TribbbleApp.plural(R.plurals.about_minutes_ago, quantity);
    }
  }
}
