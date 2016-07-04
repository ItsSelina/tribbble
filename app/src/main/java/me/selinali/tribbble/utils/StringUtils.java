package me.selinali.tribbble.utils;

public class StringUtils {
  public static CharSequence trimTrailingNewLines(CharSequence text) {
    while (text.charAt(text.length() - 1) == '\n') {
      text = text.subSequence(0, text.length() - 1);
    }
    return text;
  }
}
