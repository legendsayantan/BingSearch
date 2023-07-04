package com.legendsayantan.bingsearch;

public class launcher {
    public static void main(String[] args) {
        try {
            MainApplication.main(args);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            e.printStackTrace();
        }
    }
}
