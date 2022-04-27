package com.demo.marketdata.fix;

import quickfix.*;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntrySize;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MDUpdateAction;
import quickfix.field.NoMDEntries;
import quickfix.field.QuoteCondition;
import quickfix.field.SecurityType;
import quickfix.field.Symbol;
import quickfix.fix44.Logon;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataRequestReject;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.MessageCracker;

/**
 * @author Dani Mtz
 */
public class ServerFix extends MessageCracker implements Application {
    @Override
    public void onCreate(final SessionID sessionId) {
    }

    @Override
    public void onLogon(final SessionID sessionId) {
    }

    @Override
    public void onLogout(final SessionID sessionId) {

    }

    @Override
    public void toAdmin(final Message message, final SessionID sessionId) {

    }

    @Override
    public void fromAdmin(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        if(message instanceof Logon){
            if(!usuarioYpasswordCorrectos((Logon) message)){
                throw new RejectLogon();
            }
        }
    }

    @Override
    public void toApp(final Message message, final SessionID sessionId) throws DoNotSend {

    }

    @Override
    public void fromApp(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

    }
    /**Mensajes**/
    @Override
    public void onMessage(final MarketDataSnapshotFullRefresh message, final SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        MarketDataSnapshotFullRefresh marketDataSnapshotFullRefresh = new MarketDataSnapshotFullRefresh();
        String mdReqId="123";
        marketDataSnapshotFullRefresh.set(new MDReqID(mdReqId));
        marketDataSnapshotFullRefresh.set(new Symbol("CCY1/CCY2"));
        marketDataSnapshotFullRefresh.set(new SecurityType(SecurityType.FOREIGN_EXCHANGE_CONTRACT));
        marketDataSnapshotFullRefresh.set(new NoMDEntries(1));
        MarketDataSnapshotFullRefresh.NoMDEntries entryTypes = new MarketDataSnapshotFullRefresh.NoMDEntries();
        entryTypes.set(new MDEntryType(MDEntryType.BID));
        entryTypes.set(new MDEntryPx(100D));
        entryTypes.set(new MDEntrySize(1D));
        entryTypes.set(new QuoteCondition(QuoteCondition.NON_FIRM));
        marketDataSnapshotFullRefresh.addGroup(entryTypes);
        try {
            Session.sendToTarget(marketDataSnapshotFullRefresh,sessionID);
        } catch (SessionNotFound e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(final MarketDataIncrementalRefresh message, final SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        String mdReqId ="123";
        MarketDataIncrementalRefresh incrementalRefresh = new MarketDataIncrementalRefresh();
        incrementalRefresh.set(new MDReqID(mdReqId));
        incrementalRefresh.set(new NoMDEntries(1));
        MarketDataIncrementalRefresh.NoMDEntries noMDEntries = new MarketDataIncrementalRefresh.NoMDEntries();
        //279
        noMDEntries.set(new MDUpdateAction(MDUpdateAction.CHANGE));
        noMDEntries.set(new MDEntryType(MDEntryType.BID));
        noMDEntries.set(new MDEntryPx(100D));
        noMDEntries.set(new MDEntrySize(1D));
        noMDEntries.set(new QuoteCondition(QuoteCondition.NON_FIRM));
        noMDEntries.set(new Symbol("CCY1/CCy2"));
        noMDEntries.set(new SecurityType(SecurityType.FOREIGN_EXCHANGE_CONTRACT));
        incrementalRefresh.addGroup(noMDEntries);
        try {
            Session.sendToTarget(incrementalRefresh,sessionID);
        } catch (SessionNotFound e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(final MarketDataRequestReject message, final SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        super.onMessage(message, sessionID);
    }


    /**Validaciones**/
    private boolean usuarioYpasswordCorrectos(final Logon logon) throws FieldNotFound {
        return logon.getUsername().getValue().equals("usuario")
                && logon.getPassword().getValue().equals("password");
    }
    //TODO metodo generico para el envio de la informacion
    private void sendMessage(quickfix.fix44.Message message){
    }
}
