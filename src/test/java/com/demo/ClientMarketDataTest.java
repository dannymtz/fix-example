package com.demo;

import org.junit.jupiter.api.Test;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.MemoryStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.SocketInitiator;
import quickfix.field.*;
import quickfix.fix44.MarketDataRequest;


import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Dani Mtz
 */
class ClientMarketDataTest {
    @Test
    void iniciarServidor() throws ConfigError, InterruptedException {
        String password = "password";
        String usuario = "usuario";
        SessionSettings sessionSettings = new SessionSettings("server.cfg");
        ServerApplication serverApplication = new ServerApplication();
        SocketAcceptor acceptor = new SocketAcceptor(serverApplication,new MemoryStoreFactory(),sessionSettings,
                new ScreenLogFactory(), new DefaultMessageFactory());
        acceptor.start();

        MarketDataRequest marketDataRequest = new MarketDataRequest();
        marketDataRequest.set(new MDReqID("123"));
        marketDataRequest.set(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_UPDATES));
        marketDataRequest.set(new MarketDepth(0));
        marketDataRequest.set(new MDUpdateType(MDUpdateType.INCREMENTAL_REFRESH));
        marketDataRequest.set(new NoMDEntryTypes(2));
        MarketDataRequest.NoMDEntryTypes entryTypes = new MarketDataRequest.NoMDEntryTypes();
        entryTypes.set(new MDEntryType(MDEntryType.BID));
        marketDataRequest.addGroup(entryTypes);
        MarketDataRequest.NoRelatedSym noRelatedSym = new MarketDataRequest.NoRelatedSym();
        noRelatedSym.set(new Symbol("CCY1/CCY2"));
        noRelatedSym.set(new SecurityType(SecurityType.FOREIGN_EXCHANGE_CONTRACT));
        marketDataRequest.addGroup(noRelatedSym);
        ClientMarketData clientMarketData = new ClientMarketData(marketDataRequest,usuario,password);
        iniciarCliente(clientMarketData);
        Thread.sleep(5000L);
        assertTrue(clientMarketData.estaLogueado());
        assertTrue(clientMarketData.seEjecutoOrdenCorrectamente());

    }

    private void iniciarCliente(final ClientMarketData application) throws ConfigError {
        SessionSettings sessionSettings = new SessionSettings("client.cfg");
        SocketInitiator initiator = new SocketInitiator(application, new MemoryStoreFactory(), sessionSettings,
                new ScreenLogFactory(),new DefaultMessageFactory());
        initiator.start();
    }



}