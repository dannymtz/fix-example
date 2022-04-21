package com.demo;

import quickfix.ApplicationAdapter;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.NoMDEntries;
import quickfix.field.Password;
import quickfix.field.SecurityType;
import quickfix.field.Username;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataRequest;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.Logon;

/**
 * @author Dani Mtz
 */
public class ClientMarketData extends ApplicationAdapter {
    private boolean seEjecutoOrdenCorrectamente = false;
    private  boolean estaLogueado = false;
    private final String usuario;
    private final String password;
    private final MarketDataRequest marketDataRequest;

    public ClientMarketData(final MarketDataRequest marketDataRequest, final String usuario, final String password) {
        this.marketDataRequest = marketDataRequest;
        this.usuario = usuario;
        this.password = password;
    }

    @Override
    public void fromApp(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        if(message instanceof MarketDataSnapshotFullRefresh){
            MarketDataSnapshotFullRefresh marketDataSnapshotFullRefresh = (MarketDataSnapshotFullRefresh) message;
            if(esSecurityType(marketDataSnapshotFullRefresh)){
                this.seEjecutoOrdenCorrectamente = true;
            }
        } else if (message instanceof MarketDataIncrementalRefresh){
            MarketDataIncrementalRefresh marketDataIncrementalRefresh = (MarketDataIncrementalRefresh) message;
            if(esSecurityType(marketDataIncrementalRefresh)){
                this.seEjecutoOrdenCorrectamente = true;
            }
        }
    }

    private boolean esSecurityType(final MarketDataSnapshotFullRefresh marketDataSnapshotFullRefresh) throws FieldNotFound {
        return marketDataSnapshotFullRefresh.getSecurityType().getValue().equals(SecurityType.FOREIGN_EXCHANGE_CONTRACT);
    }
    private boolean esSecurityType(final MarketDataIncrementalRefresh marketDataIncrementalRefresh) throws FieldNotFound {
        NoMDEntries noMDEntries = new NoMDEntries();
        marketDataIncrementalRefresh.get(noMDEntries);
        MarketDataSnapshotFullRefresh.NoMDEntries group = new MarketDataSnapshotFullRefresh.NoMDEntries();
        marketDataIncrementalRefresh.getGroup(1, group);
        return false;
    }

    @Override
    public void onLogon(final SessionID sessionId) {
        this.estaLogueado = true;
        try {
            Session.sendToTarget(this.marketDataRequest, sessionId);
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

    public boolean seEjecutoOrdenCorrectamente() {
        return seEjecutoOrdenCorrectamente;
    }

    public boolean estaLogueado() {
        return estaLogueado;
    }
}
