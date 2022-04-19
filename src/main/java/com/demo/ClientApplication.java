package com.demo;

import quickfix.*;
import quickfix.field.ExecType;
import quickfix.field.Password;
import quickfix.field.Username;
import quickfix.fix50.ExecutionReport;
import quickfix.fix50.NewOrderSingle;
import quickfix.fixt11.Logon;

/**
 * @author Dani Mtz
 */
public class ClientApplication extends ApplicationAdapter {
    private boolean seEjecutoOrdenCorrectamente = false;
    private  boolean estaLogueado = false;

    private final String usuario;
    private final String password;
    private final NewOrderSingle newOrder;

    public ClientApplication(final NewOrderSingle newOrder, final String usuario, final String password) {
        this.newOrder = newOrder;
        this.usuario = usuario;
        this.password = password;
    }


    @Override
    public void fromApp(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        if(message instanceof ExecutionReport) {
            ExecutionReport executionReport = (ExecutionReport) message;
            if(esExecutionReportNew(executionReport)){
                if(esClOrdIDCorrecto(executionReport)){
                    this.seEjecutoOrdenCorrectamente = true;
                }
            }
        }
    }
    private  boolean esClOrdIDCorrecto(ExecutionReport executionReport) throws FieldNotFound {
        return executionReport.getClOrdID().getValue().equals(this.newOrder.getClOrdID().getValue());
    }

    private  boolean esExecutionReportNew(ExecutionReport executionReport) throws FieldNotFound {
        return executionReport.getExecType().getValue() == ExecType.NEW;
    }


    @Override
    public void onLogon(final SessionID sessionId) {
        this.estaLogueado = true;
        try {
            Session.sendToTarget(this.newOrder, sessionId);
        } catch (SessionNotFound e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void toAdmin(final Message message, final SessionID sessionId) {
        if(message instanceof Logon) {
            message.setField(new Username(usuario));
            message.setField(new Password(password));
        }
    }

    public  boolean estaLogueado() {
        return estaLogueado;
    }

    public  boolean seEjecutoOrdenCorrectamente() {
        return seEjecutoOrdenCorrectamente;
    }

}
