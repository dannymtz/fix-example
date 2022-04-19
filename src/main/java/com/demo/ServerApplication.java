package com.demo;

import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.Logon;

import java.util.UUID;

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
        MarketDataIncrementalRefresh marketDataIncrementalRefresh = new MarketDataIncrementalRefresh();
        MarketDataSnapshotFullRefresh marketDataSnapshotFullRefresh = new MarketDataSnapshotFullRefresh();
        marketDataSnapshotFullRefresh.set(new MDReqID("123"));
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
               Session.sendToTarget(marketDataSnapshotFullRefresh, sessionId);
           }catch (SessionNotFound e){
               throw new RuntimeException(e);
           }

    }
}
