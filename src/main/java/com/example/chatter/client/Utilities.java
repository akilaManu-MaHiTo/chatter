package com.example.chatter.client;

import java.awt.Color;

import javax.swing.border.EmptyBorder;

public class Utilities {
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0); // Transparent version of PRIMARY_COLOR
    public static final Color PRIMARY_COLOR = Color.decode("#819A91");
    public static final Color SECONDARY_COLOR = Color.decode("#D1D8BE");
    public static final Color TEXT_COLOR = Color.decode("#000000");

    public static EmptyBorder addPadding(int top,int left, int bottom,int right){
        return new EmptyBorder(top,left,bottom,right);
    }
}
