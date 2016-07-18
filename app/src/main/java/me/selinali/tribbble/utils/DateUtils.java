/*
 * Copyright 2016 Selina Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
