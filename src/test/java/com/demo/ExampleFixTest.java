package com.demo;

import org.junit.jupiter.api.Test;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.MemoryStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.SocketInitiator;
import quickfix.field.ClOrdID;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Side;
import quickfix.field.TransactTime;
import quickfix.fix50.NewOrderSingle;


import java.time.LocalDateTime;

import static java.time.LocalTime.now;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Dani Mtz
 */
class ExampleFixTest {
    @Test
    void test() throws InterruptedException, ConfigError {
        iniciarServidor();
        String password = "password";
        String usuario = "usuario";
        NewOrderSingle newOrderSingle = new NewOrderSingle(new ClOrdID("12345"),
                new Side(Side.BUY),
                new TransactTime(LocalDateTime.now()),new OrdType(OrdType.MARKET));
        newOrderSingle.set(new OrderQty(1000));
        ClientApplication application = new ClientApplication(newOrderSingle,usuario,password);
        iniciarCliente(application);
        Thread.sleep(5000L);
        assertTrue(application.estaLogueado());
        assertTrue(application.seEjecutoOrdenCorrectamente());
    }

    private void iniciarCliente(final ClientApplication application) throws ConfigError {
        SessionSettings sessionSettings = new SessionSettings("client.cfg");
        SocketInitiator initiator = new SocketInitiator(application, new MemoryStoreFactory(), sessionSettings,
                new ScreenLogFactory(),new DefaultMessageFactory());
        initiator.start();
    }

    private void iniciarServidor() throws ConfigError {
        SessionSettings sessionSettings = new SessionSettings("server.cfg");
        ServerApplication application = new ServerApplication();
        SocketAcceptor acceptor = new SocketAcceptor(application,new MemoryStoreFactory(),sessionSettings,
        new ScreenLogFactory(), new DefaultMessageFactory());
        acceptor.start();
    }
}