package com.demo.marketdata.patron;

/**
 * @author Dani Mtz
 */
public class SolObservador extends Observador{
    private double valorCambio = 3.25;

    public SolObservador(Subject subject){
        this.subject= subject;
        this.subject.agregar(this);
    }
    @Override
    public void actualizar() {
        System.out.println("PEN:"+ (subject.getEstado()* valorCambio));
    }
}
