package com.demo;

import quickfix.*;
import quickfix.field.ClOrdID;
import quickfix.field.CumQty;
import quickfix.field.ExecID;
import quickfix.field.ExecType;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.fix50.ExecutionReport;
import quickfix.fix50.NewOrderSingle;
import quickfix.fixt11.Logon;

/**
 * @author Dani Mtz
 */
public class ServerApplication extends ApplicationAdapter {

    @Override
    public void fromAdmin(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        if (message instanceof Logon){
           if(!usuarioYpasswordCorrectos((Logon) message)){
               throw new RejectLogon();
           }
        }
        super.fromAdmin(message, sessionId);
    }

    private boolean usuarioYpasswordCorrectos(final Logon logon) throws FieldNotFound {
        return logon.getUsername().getValue().equals("usuario")
                && logon.getPassword().getValue().equals("password");
    }

    @Override
    public void fromApp(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
       if(message instanceof NewOrderSingle){
           NewOrderSingle newOrderSingle = ((NewOrderSingle) message);
           ExecutionReport executionReport = new ExecutionReport();
           String clOrdID = newOrderSingle.getClOrdID().getValue();
           executionReport.set(new ClOrdID(clOrdID));
           executionReport.set(new ExecID("98765"));
           executionReport.set(new OrderID("9999"));
           executionReport.set(newOrderSingle.getSide());
           executionReport.set(new OrdStatus(OrdStatus.NEW));
           executionReport.set(new CumQty(0));
           executionReport.set(new ExecType(ExecType.NEW));
           executionReport.set(new LeavesQty(newOrderSingle.getOrderQty().getValue()));
           try {
               Session.sendToTarget(executionReport, sessionId);
           }catch (SessionNotFound e){
               throw new RuntimeException(e);
           }
       }
    }
}
