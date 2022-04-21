package com.demo;

import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataRequestReject;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.Logon;


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
    public void fromApp(final Message message, final SessionID sessionId){
        String mdReqId="123";
        try {
            Session.sendToTarget(incrementalRefresh(), sessionId);
        }catch (SessionNotFound e){
            rejectOrder(mdReqId,sessionId);
        }
    }


    //W
    public quickfix.fix44.Message snapshotFullRefresh(){
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
        return marketDataSnapshotFullRefresh;
    }

    public Message incrementalRefresh(){
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
        return incrementalRefresh;
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
