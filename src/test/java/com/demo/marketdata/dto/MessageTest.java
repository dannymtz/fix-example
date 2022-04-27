package com.demo.marketdata.dto;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quickfix.field.*;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static junit.framework.Assert.assertEquals;

/**
 * @author Dani Mtz
 */
class MessageTest {
    String jsonMessage="";
    private Message message;
    @BeforeEach
    void setUp() {
        jsonMessage ="{\n" +
                "  \"msgID\": 58309,\n" +
                "  \"msgDate\": 1644874414898,\n" +
                "  \"msgType\": \"MarketMessage\",\n" +
                "  \"marketEvent\": \"New\",\n" +
                "  \"instrumentLine\": 47,\n" +
                "  \"marketPrice\": 6.990000,\n" +
                "  \"marketAmount\": 10,\n" +
                "  \"amountBaseUnit\": \"MM\",\n" +
                "  \"marketSide\": \"BID\",\n" +
                "  \"marketRegDate\": 1644874414858,\n" +
                "  \"instrumentInfo\": {\n" +
                "    \"market\": \"Mexico\",\n" +
                "    \"asset\": \"FI\",\n" +
                "    \"product\": \"FI-MXN\",\n" +
                "    \"instrumentType\": \"02 Bonos TF Moneda Local\",\n" +
                "    \"instrument\": \"MXN Bonos M\",\n" +
                "    \"issue\": \"310529\",\n" +
                "    \"tipoValor\": \"NoInfo\",\n" +
                "    \"sector\": \"01 Mexico\",\n" +
                "    \"isin\": \"MX0MGO0000P2\",\n" +
                "    \"commonInstID\": \"FI|12BM310529MXNMX0MGO0000P2\",\n" +
                "    \"issuerId\": \"MX_GOV_BONDS\",\n" +
                "    \"parValue\": 100,\n" +
                "    \"issuer\": \"Bonos Mexico\"\n" +
                "  },\n" +
                "  \"tenor\": \"48\",\n" +
                "  \"term\": 3389,\n" +
                "  \"longTerm\": 0,\n" +
                "  \"error\": false,\n" +
                "  \"formOfOperation\": \"D\"\n" +
                "}";
        Gson json = new Gson();
        message = json.fromJson(jsonMessage,Message.class);

    }
    @Test
    void dtoJsonMessage() throws IOException {
        assertEquals(58309,message.getMsgID());
        assertEquals(Long.parseLong("1644874414898"), message.getMsgDate());
        assertEquals("MarketMessage", message.getMsgType());
        assertEquals("New", message.getMarketEvent());
        assertEquals(47,message.getInstrumentLine());
        assertEquals(0, message.getMarketPrice().compareTo(new BigDecimal("6.990000")));
        assertEquals(10.0, message.getMarketAmount());
        assertEquals("MM", message.getAmountBaseUnit());
        assertEquals("BID",message.getMarketSide());
        assertEquals(Long.valueOf("1644874414858"),message.getMarketRegDate());
        assertEquals("48",message.getTenor());
        assertEquals(Double.valueOf(3389), message.getTerm());
        assertEquals(Long.valueOf("0"),message.getLongTerm());
        assertEquals(false,message.isError());
        assertEquals("D",message.getFormOfOperation());
        assertEquals("Mexico",message.getInstrumentInfo().getMarket());
        assertEquals("FI",message.getInstrumentInfo().getAsset());
        assertEquals("FI-MXN",message.getInstrumentInfo().getProduct());
        assertEquals("02 Bonos TF Moneda Local", message.getInstrumentInfo().getInstrumentType());
        assertEquals("MXN Bonos M", message.getInstrumentInfo().getInstrument());
        assertEquals("310529",message.getInstrumentInfo().getIssue());
        assertEquals("NoInfo",message.getInstrumentInfo().getTipoValor());
        assertEquals("01 Mexico",message.getInstrumentInfo().getSector());
        assertEquals("MX0MGO0000P2",message.getInstrumentInfo().getIsin());
        assertEquals("FI|12BM310529MXNMX0MGO0000P2", message.getInstrumentInfo().getCommonInstID());
        assertEquals("MX_GOV_BONDS",message.getInstrumentInfo().getIssuerId());
        assertEquals(100,message.getInstrumentInfo().getParValue());
        assertEquals("Bonos Mexico",message.getInstrumentInfo().getIssuer());
    }

    @DisplayName("Respuesta inicial es Snapshot/Full Refresh W")
    @Test
    void converJsonToFix(){
        MarketDataSnapshotFullRefresh fullRefresh = new MarketDataSnapshotFullRefresh();
        fullRefresh.set(new MDReqID(String.valueOf(message.getMsgID())));
        fullRefresh.set(new Symbol(message.getInstrumentInfo().getIsin()));
        MarketDataSnapshotFullRefresh.NoMDEntries noMDEntries = new MarketDataSnapshotFullRefresh.NoMDEntries();
        if(message.getMarketSide().equals("BID")){
        noMDEntries.set(new MDEntryType(MDEntryType.BID));}
        else if(message.getMarketSide().equals("OFFER")){
            noMDEntries.set(new MDEntryType(MDEntryType.OFFER));}
        noMDEntries.set(new MDEntryPx(Double.parseDouble(message.getMarketPrice().toString())));
        noMDEntries.set(new MDEntrySize(message.getMarketAmount()));
        LocalDate date = Instant.ofEpochMilli(message.getMsgDate()).atZone(ZoneId.of("UTC")).toLocalDate();
        noMDEntries.set(new MDEntryDate(date));
        noMDEntries.set(new MDEntryTime(Instant.ofEpochMilli(message.getMsgDate()).atZone(ZoneId.of("UTC")).toLocalTime()));
        fullRefresh.addGroup(noMDEntries);
        System.out.println(fullRefresh);
    }

    @DisplayName("Incremental Refresh")
    @Test
    void convertJsonIncrementalRefresh(){
        MarketDataIncrementalRefresh incrementalRefresh = new MarketDataIncrementalRefresh();
        incrementalRefresh.set(new MDReqID(String.valueOf(message.getMsgID())));
        MarketDataIncrementalRefresh.NoMDEntries noMDEntries = new MarketDataIncrementalRefresh.NoMDEntries();
        switch (message.getMarketEvent()) {
            case "New":
                noMDEntries.set(new MDUpdateAction(MDUpdateAction.NEW));
                break;
            case "Executed":
                noMDEntries.set(new MDUpdateAction(MDUpdateAction.CHANGE));
                break;
            case "Canceled":
                noMDEntries.set(new MDUpdateAction(MDUpdateAction.DELETE));
                break;
        }
        noMDEntries.set(new MDEntryType(MDEntryType.BID));
        noMDEntries.set(new Symbol(message.getInstrumentInfo().getIsin()));
        noMDEntries.set(new MDEntryPx(message.getMarketPrice().doubleValue()));
        noMDEntries.set(new MDEntrySize(message.getMarketAmount()));
        noMDEntries.set(new MDEntryTime(LocalTime.ofNanoOfDay(message.getMsgDate())));
        noMDEntries.set(new ExpireTime());
        incrementalRefresh.addGroup(noMDEntries);
        System.out.println(incrementalRefresh);
    }


}