package com.ajay;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Mobile {

    public static void main(String[] args) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring1.xml");
        Sim sim=(Sim) applicationContext.getBean("sim");
        sim.calling();
        sim.Surfing();
    }
}
