function position(deck,cards,side,y_offset) {
    var i = 0;
    for (var index in cards) {
        var card = deck.cards[index];
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
    }
}

function setup($container){

    position(Deck(),[0,1,2], 'front',200);
    position(Deck(),[3,4,5,6], 'front',0);
    position(Deck(),[7,8,9], 'back',-200);
}