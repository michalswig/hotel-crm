package com.hotelcrm.crmapp.config;

import java.util.HashSet;
import java.util.Set;

public class MyThred implements Runnable {


    @Override
    public void run() {
        System.out.println("New Thread");
    }


    public static void main(String[] args) {
        new Thread(new MyThred()).start();
        Set<Integer> set = new HashSet<>();
        for(Integer i : set){
            System.out.println(i);
        }

    }

}
