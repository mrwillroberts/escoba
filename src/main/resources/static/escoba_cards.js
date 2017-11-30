function position(deck,cards,side,y_offset) {
    var i = 0;
    jQuery.each(cards, function() {
        var card = deck.cards[this];
        //card.enableDragging();
        card.setSide(side);
        card.mount($container);

        card.animateTo({
            delay: 500 + i * 2, // wait 1 second + i * 2 ms
            duration: 500,
            ease: 'quartOut',
            x: (i-cards.length/2)*100,
            y: y_offset
        });
        //
//        card.$el.addEventListener("mousedown",function(){
//            card.$el.style["border-style"] = "solid";
//        });
        i++;
    });
}

function lookupSuitMultiplier(suit){
    switch(suit) {
        case "Coin":
            return 0;
        case "Sword":
            return 1;
        case "Cup":
            return 2;
        case "Club":
            return 3;
    }
}

function lookupCardIndices(cardArray) {
    var guiCardsIndices = []
    jQuery.each(cardArray, function() {
        guiCardsIndices.push(lookupSuitMultiplier(this.suit)*13+this.value);
    });
    return guiCardsIndices;
}

function setup($container){

    $.get("/api/game/1", function(data, status){
        position(Deck(),[7,8,9], 'back',-200);
        position(Deck(),lookupCardIndices(data.currentPlayerHand), 'front',200);
        position(Deck(),lookupCardIndices(data.tableCards), 'front',0);
    });

}