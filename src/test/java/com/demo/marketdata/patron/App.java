package com.demo.marketdata.patron;

import org.junit.jupiter.api.Test;

/**
 * @author Dani Mtz
 */
public class App {
    @Test
    void test(){
        Subject subject = new Subject();
        new SolObservador(subject);
        System.out.println("primer estado");
        subject.setEstado(10);
        System.out.println("segundo estado");
        subject.setEstado(20);
    }
}
