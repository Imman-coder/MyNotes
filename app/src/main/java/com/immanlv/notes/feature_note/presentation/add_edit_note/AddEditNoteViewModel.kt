package com.immanlv.notes.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immanlv.notes.feature_note.domain.model.Note
import com.immanlv.notes.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.InvalidClassException
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel
@Inject
    constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Title here"
    ))
    val noteTitle : State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter note content"
    ))
    val noteContent : State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor : State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId->
            if (noteId != -1){
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.let { note ->
                        _noteTitle.value = _noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }

                }
            }
        }
    }

    fun onEvent(event:AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = _noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = _noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(Note(
                            title = _noteTitle.value.text,
                            content = _noteContent.value.text,
                            timestamp = System.currentTimeMillis(),
                            color = _noteColor.value,
                            id = currentNoteId
                        ))
                        _eventFlow.emit(UiEvent.SaveNote)

                    } catch (e:InvalidClassException){
                        _eventFlow.emit(UiEvent.ShowSnackbar(
                            e.message ?: "Couldn't Save Note"
                        ))
                    }
                }

            }
        }
    }



    sealed class UiEvent(){
        data class ShowSnackbar(val Message:String): UiEvent()
        object SaveNote: UiEvent()
    }

}