package com.demo.marketdata.patron;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dani Mtz
 */
public class Subject {
    private final List<Observador> observadores = new ArrayList<Observador>();
    private int estado;

    public int getEstado(){
        return estado;
    }
    public void setEstado(int estado){
        this.estado= estado;
        notificaObservadores();
    }
    public void agregar(Observador observador){
        this.observadores.add(observador);
    }
    public void notificaObservadores(){
    /*for(Observador observador: observadores){
            observador.actualizar();
        }*/
        observadores.forEach(Observador::actualizar);
    }
}
