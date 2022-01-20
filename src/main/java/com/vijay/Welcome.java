package com.vijay;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Welcome {
    public static void main(String[] args) {

        ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
        Vehicle vehicle=(Vehicle) context.getBean("vehicle");
        vehicle.ride();


    }


}
class Car implements Vehicle
{

    @Override
    public void ride() {
        System.out.println("riding car");
    }
}
class Bike implements Vehicle {

    @Override
    public void ride() {
        System.out.println("riding bike");
    }
}

