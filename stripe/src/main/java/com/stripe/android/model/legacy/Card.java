package com.stripe.android.model.legacy;

import com.stripe.android.validators.CardParamsValidator;
import android.util.Log;

public class Card {

    private String LOGGER_TAG = "Stripe :: LegacyCard";

    private enum CardState {
        DEFAULT, CARD_PARAMETERS, TOKENIZED_CARD
    }

    private com.stripe.android.model.Card card;
    private com.stripe.android.model.CardParams cardParams;

    protected com.stripe.android.model.Card getCard() { return this.card; }
    protected com.stripe.android.model.CardParams getCardParams() { return this.cardParams; }

    private CardState cardState = CardState.DEFAULT;

    public static final String AMERICAN_EXPRESS = CardParamsValidator.AMERICAN_EXPRESS;
    public static final String DISCOVER = CardParamsValidator.DISCOVER;
    public static final String JCB = CardParamsValidator.JCB;
    public static final String DINERS_CLUB = CardParamsValidator.DINERS_CLUB;
    public static final String VISA = CardParamsValidator.VISA;
    public static final String MASTERCARD = CardParamsValidator.MASTERCARD;
    public static final String UNKNOWN = CardParamsValidator.UNKNOWN;

    public static final String[] PREFIXES_AMERICAN_EXPRESS = CardParamsValidator.PREFIXES_AMERICAN_EXPRESS;
    public static final String[] PREFIXES_DISCOVER = CardParamsValidator.PREFIXES_DISCOVER;
    public static final String[] PREFIXES_JCB = CardParamsValidator.PREFIXES_JCB;
    public static final String[] PREFIXES_DINERS_CLUB = CardParamsValidator.PREFIXES_DINERS_CLUB;
    public static final String[] PREFIXES_VISA = CardParamsValidator.PREFIXES_VISA;
    public static final String[] PREFIXES_MASTERCARD = CardParamsValidator.PREFIXES_MASTERCARD;

    public static final int MAX_LENGTH_STANDARD = CardParamsValidator.MAX_LENGTH_STANDARD;
    public static final int MAX_LENGTH_AMERICAN_EXPRESS = CardParamsValidator.MAX_LENGTH_AMERICAN_EXPRESS;
    public static final int MAX_LENGTH_DINERS_CLUB = CardParamsValidator.MAX_LENGTH_DINERS_CLUB;

    public static class Builder {

        private final String number;
        private final String cvc;
        private final Integer expMonth;
        private final Integer expYear;
        private String name;
        private String addressLine1;
        private String addressLine2;
        private String addressCity;
        private String addressZip;
        private String country;
        private String currency;

        // Only available on Card objects post-tokenization
        private String addressState;
        private String addressCountry;
        private String last4;
        private String type_or_brand;
        private String fingerprint;

        public Builder(String number, Integer expMonth, Integer expYear, String cvc) {
            this.number = number;
            this.expMonth = expMonth;
            this.expYear = expYear;
            this.cvc = cvc;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addressLine1(String address) {
            this.addressLine1 = address;
            return this;
        }

        public Builder addressLine2(String address) {
            this.addressLine2 = address;
            return this;
        }

        public Builder addressCity(String city) {
            this.addressCity = city;
            return this;
        }

        public Builder addressState(String state) {
            this.addressState = state;
            return this;
        }

        public Builder addressZip(String zip) {
            this.addressZip = zip;
            return this;
        }

        public Builder addressCountry(String country) {
            this.addressCountry = country;
            return this;
        }

        public Builder last4(String last4) {
            this.last4 = last4;
            return this;
        }

        public Builder type(String type) {
            this.type_or_brand = type;
            return this;
        }

        public Builder brand(String type) {
            this.type_or_brand = type;
            return this;
        }

        public Builder fingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder currency(String currency)
        {
            this.currency = currency;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }

    private Card(Builder builder) {
        this(
            builder.number,
            builder.expMonth,
            builder.expYear,
            builder.cvc,
            builder.name,
            builder.addressLine1,
            builder.addressLine2,
            builder.addressCity,
            builder.addressState,
            builder.addressZip,
            builder.addressCountry,
            builder.last4,
            builder.type_or_brand,
            builder.fingerprint,
            builder.country,
            builder.currency
        );
    }

    public Card(String number, Integer expMonth, Integer expYear, String cvc, String name, String addressLine1, String addressLine2, String addressCity,
                String addressState, String addressZip, String addressCountry, String last4, String type_or_brand, String fingerprint, String country, String currency) {
        if (last4 != null || fingerprint != null) {
            this.card = new com.stripe.android.model.Card(
                null,  // id
                null,  // status
                expMonth,
                expYear,
                last4,
                null,  // dynamicLast4
                country,
                type_or_brand,
                name,
                addressLine1,
                addressLine2,
                addressZip,
                addressCity,
                addressState,
                addressCountry,
                addressZip,
                null,  // addressLine1Check
                null,  // cvcCheck
                fingerprint,
                type_or_brand,
                null,  // funding
                currency,  // currency
                null   // tokenizationMethod
            );
            this.cardState = CardState.TOKENIZED_CARD;
        } else {
            this.cardParams = new com.stripe.android.model.CardParams(
                number,
                expMonth,
                expYear,
                cvc,
                currency,
                name,
                addressLine1,
                addressLine2,
                addressCity,
                addressState,
                addressZip,
                country
            );
            this.cardState = CardState.CARD_PARAMETERS;
        }
    }

    // NOTE: This was added because apparently we didn't support currency?
    public Card(String number, Integer expMonth, Integer expYear, String cvc, String name, String addressLine1, String addressLine2, String addressCity,
                String addressState, String addressZip, String addressCountry, String last4, String type, String fingerprint, String country) {
        this(number, expMonth, expYear, cvc, name, addressLine1, addressLine2, addressCity, addressState, addressZip, addressCountry, last4, type, fingerprint, country, null);
    }

    // NOTE: This is from a previous currency fix that didn't do anything.
    public Card(String number, Integer expMonth, Integer expYear, String cvc, String name, String addressLine1, String addressLine2, String addressCity,
                String addressState, String addressZip, String addressCountry, String currency) {
        this(number, expMonth, expYear, cvc, name, addressLine1, addressLine2, addressCity, addressState, addressZip, addressCountry, null, null, null, null, currency);
    }

    public Card(String number, Integer expMonth, Integer expYear, String cvc) {
        this(number, expMonth, expYear, cvc, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public boolean validateCard() { return this.cardParams.validateCardParams(); }

    public boolean validateNumber() { return this.cardParams.validateNumber(); }

    public boolean validateExpiryDate() { return this.cardParams.validateExpiryDate(); }

    public boolean validateExpMonth() { return this.cardParams.validateExpMonth(); }

    public boolean validateExpYear() { return this.cardParams.validateExpYear(); }

    public boolean validateCVC() { return this.cardParams.validateCVC(); }

    private void checkState(CardState desiredState) {
        if (cardState == CardState.DEFAULT && desiredState != CardState.DEFAULT) {
            Log.w(LOGGER_TAG, "The card has not yet been initialized.");
        }
        if (cardState == CardState.CARD_PARAMETERS && desiredState != CardState.CARD_PARAMETERS) {
            Log.w(LOGGER_TAG, "The card has not been tokenized yet.");
        }
        if (cardState == CardState.TOKENIZED_CARD && desiredState != CardState.TOKENIZED_CARD) {
            Log.w(LOGGER_TAG, "The card has already been tokenized.");
        }
    }

    public String getNumber() {
        return cardState == CardState.CARD_PARAMETERS ? this.cardParams.getNumber() : null;
    }

    public void setNumber(String number) {
        checkState(CardState.CARD_PARAMETERS);
        if (this.cardParams != null)
            this.cardParams.setNumber(number);
    }

    public String getCVC() {
        return cardState == CardState.CARD_PARAMETERS ? this.cardParams.getCvc() : null;
    }

    public void setCVC(String cvc) {
        checkState(CardState.CARD_PARAMETERS);
        if (this.cardParams != null)
            this.cardParams.setNumber(cvc);
    }

    public Integer getExpMonth() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getExpMonth(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getExpMonth(); }
        return null;
    }

    public void setExpMonth(Integer expMonth) {
        checkState(CardState.CARD_PARAMETERS);
        if (this.cardParams != null)
            this.cardParams.setExpMonth(expMonth);
    }

    public Integer getExpYear() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getExpYear(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getExpYear(); }
        return null;
    }

    public void setExpYear(Integer expYear) {
        checkState(CardState.CARD_PARAMETERS);
        if (this.cardParams != null)
            this.cardParams.setExpYear(expYear);
    }

    public String getName() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getName(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getName(); }
        return null;
    }
    public void setName(String name) {
        checkState(CardState.CARD_PARAMETERS);
        this.cardParams.setName(name);
    }

    public String getAddressLine1() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getAddressLine1(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getAddressLine1(); }
        return null;
    }

    public void setAddressLine1(String addressLine1) {
        checkState(CardState.CARD_PARAMETERS);
        if (cardState == CardState.CARD_PARAMETERS)
            this.cardParams.setAddressLine1(addressLine1);
    }

    public String getAddressLine2() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getAddressLine2(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getAddressLine2(); }
        return null;
    }

    public void setAddressLine2(String addressLine2) {
        checkState(CardState.CARD_PARAMETERS);
        if (cardState == CardState.CARD_PARAMETERS)
            this.cardParams.setAddressLine2(addressLine2);
    }

    public String getAddressCity() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getAddressCity(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getAddressCity(); }
        return null;
    }

    public void setAddressCity(String addressCity) {
        checkState(CardState.CARD_PARAMETERS);
        if (cardState == CardState.CARD_PARAMETERS)
            this.cardParams.setAddressCity(addressCity);
    }

    public String getAddressZip() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getAddressZip(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getAddressZip(); }
        return null;
    }

    public void setAddressZip(String addressZip) {
        checkState(CardState.CARD_PARAMETERS);
        if (cardState == CardState.CARD_PARAMETERS)
            this.cardParams.setAddressZip(addressZip);
    }

    public String getAddressState() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getAddressState(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getAddressState(); }
        return null;
    }

    public void setAddressState(String addressState) {
        checkState(CardState.CARD_PARAMETERS);
        if (cardState == CardState.CARD_PARAMETERS)
            this.cardParams.setAddressState(addressState);
    }

    public String getAddressCountry() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getAddressCountry(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getAddressCountry(); }
        return null;
    }

    public void setAddressCountry(String addressCountry) {
        checkState(CardState.CARD_PARAMETERS);
        if (cardState == CardState.CARD_PARAMETERS)
            this.cardParams.setAddressCountry(addressCountry);
    }

    public String getCurrency() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getCurrency(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getCurrency(); }
        return null;
    }

    public void setCurrency(String currency) {
        checkState(CardState.CARD_PARAMETERS);
        if (cardState == CardState.CARD_PARAMETERS)
            this.cardParams.setCurrency(currency);
    }

    public String getLast4() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getLast4(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getLast4(); }
        return null;
    }

    public String getType() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getType(); }
        if (cardState == CardState.CARD_PARAMETERS) { return this.cardParams.getType(); }
        return null;
    }

    public String getFingerprint() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getFingerprint(); }
        return null;
    }

    public String getCountry() {
        if (cardState == CardState.TOKENIZED_CARD) { return this.card.getCountry(); }
        return null;
    }

}
