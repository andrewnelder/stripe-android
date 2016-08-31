package com.stripe.android.model.legacy;
import java.util.Date;

@Deprecated
public class Token {

    private Card legacyCard;
    private com.stripe.android.model.Token token;

    public Token(String id, boolean livemode, Date created, Boolean used, Card card) {
        this.token = new com.stripe.android.model.Token(
            id,
            livemode,
            created,
            used,
            null,
            null,
            null,
            null,
            null,
            card.getCard()
        );
        this.legacyCard = card;
    }

    public Date getCreated() {
        return this.token.getCreated();
    }

    public String getId() {
        return this.token.getId();
    }

    public boolean getLivemode() {
        return this.token.getLivemode();
    }

    public boolean getUsed() {
        return this.token.getUsed();
    }

    public Card getCard() {
        return this.legacyCard;
    }

}
