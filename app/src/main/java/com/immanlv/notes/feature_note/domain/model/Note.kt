package com.immanlv.notes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.immanlv.notes.ui.theme.BabyBlue
import com.immanlv.notes.ui.theme.LightGreen
import com.immanlv.notes.ui.theme.RedOrange
import com.immanlv.notes.ui.theme.RedPink
import com.immanlv.notes.ui.theme.Violet

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id:Int? = null
){
    companion object{
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message:String):Exception(message)