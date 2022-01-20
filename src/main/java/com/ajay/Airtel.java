package com.ajay;

public class Airtel implements Sim{
    @Override
    public void calling() {
        System.out.println("Calling using Airtel Sim");

    }

    @Override
    public void Surfing() {
        System.out.println("surfing data using Airtel");

    }
}
