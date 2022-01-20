package com.ajay;

public class Vadafone implements Sim{
    @Override
    public void calling() {
        System.out.println("Calling using Vadafone Sim");
    }

    @Override
    public void Surfing() {
        System.out.println("surfing data using Vadafone");
    }
}
