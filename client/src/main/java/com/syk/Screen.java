package com.syk;

import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class Screen {
    private String[][] map;
    private Color[][] color;
    private String[] instr;
    public String totalInstr;


    public Screen() {
        map = new String[60][40];
        color = new Color[60][40];
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 40; j++) {
                map[i][j] = null;
                color[i][j] = null;
            }
        }
        totalInstr = null;
    }

    public void displayOutput(AsciiPanel terminal) {
        Color tempColor;
        char tempChar;

        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 60; y++) {
                if (map[y][x] != null && color[y][x] != null) {
                    tempChar = map[y][x].charAt(0);
                    tempColor = color[y][x];
                }
                else {
                    tempChar = (char)250;
                    tempColor = Color.GRAY;
                }
                
                terminal.write(tempChar, x, y, tempColor);

            }
        }
    }

    public Screen respondToUserInput(KeyEvent key) {
        return this;
    }

    public void parse(){
        if (totalInstr == null) {
            return;
        }
        this.instr = totalInstr.split("@");
        int index = 0;
        for (int i = 0; i < 60; i++) {
            for(int j = 0; j < 40; j++) {
                char c = this.instr[0].charAt(index);
                if (c != 255) {
                    map[i][j] = String.valueOf(c);
                }
                else {
                    map[i][j] = null;
                }
                int co = (int)this.instr[1].charAt(index);
                if(co != 255) {
                    switch(co) {
                        case 1: color[i][j] = Color.BLUE;break;
                        case 2: color[i][j] = Color.GREEN;break;
                        case 3: color[i][j] = Color.YELLOW;break;
                        case 4: color[i][j] = Color.WHITE;break;
                        case 5: color[i][j] = Color.RED;break;
                        default: color[i][j] = null;break;
                    }
                }
                else {
                    color = null;
                }
                index++;
            }
        }
    }
}