package com.example.lpiem.hearthstonecollectorapp.Interface

import com.example.lpiem.hearthstonecollectorapp.Models.Deck

interface InterfaceCallBackDeck {
    public fun onWorkDeckDone(result: MutableList<Deck>)
}