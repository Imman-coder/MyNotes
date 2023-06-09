package com.immanlv.notes.feature_note.domain.util

sealed class NoteOrder(val orderType: OrderType){
    class Title(orderType: OrderType):NoteOrder(orderType)
    class Date(orderType: OrderType):NoteOrder(orderType)
    class Color(orderType: OrderType):NoteOrder(orderType)

    //used copy function instead of when
    fun copy(orderType: OrderType):NoteOrder{
        return this.copy(orderType)
    }
}