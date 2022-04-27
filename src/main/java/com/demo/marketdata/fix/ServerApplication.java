package com.demo.marketdata.fix;

import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataRequest;
import quickfix.fix44.MarketDataRequestReject;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.Logon;


/**
 * @author Dani Mtz
 */
public class ServerApplication extends ApplicationAdapter {

    /***
     * Se reciben mensajes relativos a la sesión
     * - Rechazo de mensaje mal formado
     * @param message QuickFIX message
     * @param sessionId QuickFIX session ID
     * @throws FieldNotFound
     * @throws IncorrectDataFormat
     * @throws IncorrectTagValue
     * @throws RejectLogon
     */
    @Override
    public void fromAdmin(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        if (message instanceof Logon){
           if(!usuarioYpasswordCorrectos((Logon) message)){
               throw new RejectLogon();
           }
        }
        super.fromAdmin(message, sessionId);
    }

    /****
     * Se reciben mensajes de negocio,
     * ExecutionReport, mensaje que reporta cambios de estado de una orden
     * @param message QuickFIX message
     * @param sessionId QuickFIX session ID
     */
    @Override
    public void fromApp(final Message message, final SessionID sessionId) throws FieldNotFound {
        MarketDataRequest marketDataRequest = (MarketDataRequest) message;
        if(marketDataRequest.getMDUpdateType().getObject().equals(MDUpdateType.INCREMENTAL_REFRESH)){
            System.out.println("Incremental Refresh");
            snapshotFullRefresh(sessionId);
        } else if (marketDataRequest.getMDUpdateType().getObject().equals(MDUpdateType.FULL_REFRESH)){
            System.out.println("Full Refresh");
            incrementalRefresh(sessionId);
        }
    }

    private boolean usuarioYpasswordCorrectos(final Logon logon) throws FieldNotFound {
        return logon.getUsername().getValue().equals("usuario")
                && logon.getPassword().getValue().equals("password");
    }


    //W
    public void snapshotFullRefresh(SessionID sessionID){
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
            Session.sendToTarget(marketDataSnapshotFullRefresh, sessionID);
        } catch (SessionNotFound e) {
            throw new RuntimeException(e);
        }

    }

    public void incrementalRefresh(SessionID sessionID){
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
        //285 noMDEntries.set(new DeleteReason(DeleteReason.ERROR));
        /**
         * If specified, must be unique among currently active entries if MDUpdateAction (279) = New (0),
         * must be the same as a previous MDEntryID (278) if MDUpdateAction (279) = Delete (2),
         * and must be the same as a previous MDEntryID (278) if MDUpdateAction (279) = Change (1) and MDEntryRefID (280) is not specified,
         * or must be unique among currently active entries if MDUpdateAction (279) = Change(1) and MDEntryRefID (280) is specified.
         * **/
        //278 noMDEntries.set(new MDEntryID());

        incrementalRefresh.addGroup(noMDEntries);
        try {
            Session.sendToTarget(incrementalRefresh,sessionID);
        } catch (SessionNotFound e){
            throw new RuntimeException(e);
        }
    }

    private void rejectOrder(String mdReqId, SessionID sessionId) {
        MarketDataRequestReject requestReject = new MarketDataRequestReject(
                new MDReqID(mdReqId));
        try {
            Session.sendToTarget(requestReject, sessionId);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
    }
}
