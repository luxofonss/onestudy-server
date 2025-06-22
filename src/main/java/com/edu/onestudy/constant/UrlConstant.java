package com.edu.onestudy.constant;

public class UrlConstant {

    private UrlConstant() {}

    public static final String HEALTH_CHECK_URL = "/actuator/health";

    public enum TransactionLogClientServiceApi {
        GET_TRANSACTION("internal/transaction-history/v1/transactions/"),
        GET_LIST_TRANSACTION("internal/transaction-history/v1/transaction-filters"),
        ;

        private String value;

        private TransactionLogClientServiceApi(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}