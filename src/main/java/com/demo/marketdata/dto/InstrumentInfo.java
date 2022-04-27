package com.demo.marketdata.dto;

/**
 * @author Dani Mtz
 */
public class InstrumentInfo {
    private String market;
    private String asset;
    private String product;
    private String instrumentType;
    private String instrument;
    private String issue;
    private String tipoValor;
    private String sector;
    private String isin;
    private String commonInstID;
    private String issuerId;
    private long parValue;
    private String issuer;

    public String getMarket() {
        return market;
    }

    public void setMarket(final String market) {
        this.market = market;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(final String asset) {
        this.asset = asset;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(final String product) {
        this.product = product;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(final String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(final String instrument) {
        this.instrument = instrument;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(final String issue) {
        this.issue = issue;
    }

    public String getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(final String tipoValor) {
        this.tipoValor = tipoValor;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(final String sector) {
        this.sector = sector;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(final String isin) {
        this.isin = isin;
    }

    public String getCommonInstID() {
        return commonInstID;
    }

    public void setCommonInstID(final String commonInstID) {
        this.commonInstID = commonInstID;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(final String issuerId) {
        this.issuerId = issuerId;
    }

    public long getParValue() {
        return parValue;
    }

    public void setParValue(final long parValue) {
        this.parValue = parValue;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }
}
