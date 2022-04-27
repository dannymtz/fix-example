package com.demo.marketdata.patron;

/**
 * @author Dani Mtz
 */
public abstract class Observador {
    protected Subject subject;
    public abstract void actualizar();
}
