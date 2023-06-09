package com.immanlv.notes.feature_note.presentation.notes

import com.immanlv.notes.feature_note.domain.model.Note
import com.immanlv.notes.feature_note.domain.util.NoteOrder
import com.immanlv.notes.feature_note.domain.util.OrderType

data class NotesState(
    val notes:List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
    )
