package com.demo.marketdata.dto;

import java.math.BigDecimal;

/**
 * @author Dani Mtz
 */
public class Message {
    private long msgID;
    private long msgDate;
    private String msgType;
    private String marketEvent;
    private int instrumentLine;
    private BigDecimal marketPrice;
    private Double marketAmount;
    private String amountBaseUnit;
    private String marketSide;
    private Long marketRegDate;
    private InstrumentInfo instrumentInfo;
    private String tenor;
    private Double term;
    private Long longTerm;
    private boolean error;
    private String formOfOperation;

    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(final long msgID) {
        this.msgID = msgID;
    }

    public long getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(final long msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(final String msgType) {
        this.msgType = msgType;
    }

    public String getMarketEvent() {
        return marketEvent;
    }

    public void setMarketEvent(final String marketEvent) {
        this.marketEvent = marketEvent;
    }

    public int getInstrumentLine() {
        return instrumentLine;
    }

    public void setInstrumentLine(final int instrumentLine) {
        this.instrumentLine = instrumentLine;
    }

    public Double getMarketAmount() {
        return marketAmount;
    }

    public void setMarketAmount(final Double marketAmount) {
        this.marketAmount = marketAmount;
    }

    public String getAmountBaseUnit() {
        return amountBaseUnit;
    }

    public void setAmountBaseUnit(final String amountBaseUnit) {
        this.amountBaseUnit = amountBaseUnit;
    }

    public String getMarketSide() {
        return marketSide;
    }

    public void setMarketSide(final String marketSide) {
        this.marketSide = marketSide;
    }

    public Long getMarketRegDate() {
        return marketRegDate;
    }

    public void setMarketRegDate(final Long marketRegDate) {
        this.marketRegDate = marketRegDate;
    }

    public InstrumentInfo getInstrumentInfo() {
        return instrumentInfo;
    }

    public void setInstrumentInfo(final InstrumentInfo instrumentInfo) {
        this.instrumentInfo = instrumentInfo;
    }

    public String getTenor() {
        return tenor;
    }

    public void setTenor(final String tenor) {
        this.tenor = tenor;
    }

    public Double getTerm() {
        return term;
    }

    public void setTerm(final Double term) {
        this.term = term;
    }

    public boolean isError() {
        return error;
    }

    public void setError(final boolean error) {
        this.error = error;
    }

    public String getFormOfOperation() {
        return formOfOperation;
    }

    public void setFormOfOperation(final String formOfOperation) {
        this.formOfOperation = formOfOperation;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(final BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getLongTerm() {
        return longTerm;
    }

    public void setLongTerm(final Long longTerm) {
        this.longTerm = longTerm;
    }
}
