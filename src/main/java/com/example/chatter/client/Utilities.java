package com.example.chatter.client;

import java.awt.Color;
import javax.swing.border.EmptyBorder;

public class Utilities {

  public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
  public static final Color PRIMARY_COLOR = Color.decode("#819A91");
  public static final Color SECONDARY_COLOR = Color.decode("#D1D8BE");

  public static final Color GRAY_COLOR = new Color(240, 242, 245);
  public static final Color CHATTER_PRIMARY_COLOR = Color.decode("#25c0cf");
  public static final Color CHATTER_SECONDARY_COLOR = Color.decode("#7334bf");
  public static final Color CHATTER_BACKGROUND_COLOR = Color.decode("#ffffff");

  public static final Color USER_CHAT_COLOR = new Color(115, 52, 191, 51);
  public static final Color NOT_USER_CHAT_COLOR = new Color(37, 192, 207, 51);

  public static final Color TEXT_COLOR = Color.decode("#000000");

  public static final Color TEST = Color.decode("#000000");

  public static EmptyBorder addPadding(
    int top,
    int left,
    int bottom,
    int right
  ) {
    return new EmptyBorder(top, left, bottom, right);
  }
}
