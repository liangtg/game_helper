package com.syberos.process;

/**
 * Created by liangtg on 18-2-7.
 */

public class Main {
    public static void main(String[] args) {
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]);
            }
        }
    }
}
