package org.ak.dto;

public class AgreementNumber {
    private String agreementNumber;

    public AgreementNumber() {
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AgreementNumber{");
        sb.append("agreementNumber='").append(agreementNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
