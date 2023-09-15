package com.openphonics.model.response

import kotlinx.serialization.Serializable


@Serializable
data class Flag(
    val flag: String,
    val id: String
){
    companion object {
        fun create(flag: com.openphonics.data.model.data.Flag  ): Flag = Flag(
            flag.flag,
            flag.id
        )
    }
}
@Serializable
data class Language (
    val nativeId: String,
    val languageId: String,
    val languageName: String,
    val flag: String,
    val units: List<Unit>,
    val hasData: Boolean,
    val id: Int,
) {
    companion object {
        fun create(language: com.openphonics.data.model.data.Language): Language = Language(
            language.nativeId,
            language.languageId,
            language.languageName,
            language.flag,
            language.units.map {
               Unit.create(it)
            },
            language.hasData,
            language.id
        )
    }
}
@Serializable
data class Unit(
    val title: String,
    val order: Int,
    val sections: List<Section>,
    val hasData: Boolean,
    val language: Int,
    val id: Int
){
    companion object {
        fun create(unit: com.openphonics.data.model.data.Unit): Unit = Unit(
            unit.title,
            unit.order,
            unit.sections.map {
              Section.create(it)
            },
            unit.hasData,
            unit.language,
            unit.id
        )
    }
}
@Serializable
data class Section (
    val title: String,
    val order: Int,
    val lessonCount: Int,
    val words: List<Word>,
    val sentences: List<Sentence>,
    val hasData: Boolean,
    val unit: Int,
    val id: Int
) {
    companion object {
        fun create(section: com.openphonics.data.model.data.Section): Section = Section(
            section.title,
            section.order,
            section.lessonCount,
            section.words.map {
                Word.create(it)
            },
            section.sentences.map {
                Sentence.create(it)
            },
            section.hasData,
            section.unit,
            section.id
        )
    }
}

@Serializable
data class Word(
    val language: Int,
    val phonic: String,
    val sound: String,
    val translatedSound: String,
    val translatedWord: String,
    val word: String,
    val id: Int
) {
    companion object {
        fun create(word: com.openphonics.data.model.data.Word): Word = Word(
            word.language,
            word.phonic,
            word.sound,
            word.translatedSound,
            word.translatedWord,
            word.word,
            word.id
        )
    }
}

@Serializable
data class Sentence(
    val language: Int,
    val sentence: List<Word>,
    val id: Int
) {
    companion object {
        fun create(sentence: com.openphonics.data.model.data.Sentence) = Sentence(
            sentence.language,
            sentence.sentence.map {
                Word.create(it)
            },
            sentence.id
        )
    }
}
@Serializable
data class FlagResponse(
    override val status: State,
    override val message: String,
    val flag: List<Flag> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = FlagResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(flag: List<Flag>) = FlagResponse(
            State.SUCCESS,
            "Task successful",
            flag
        )
        fun success(flag: Flag) = FlagResponse(
            State.SUCCESS,
            "Task successful",
            listOf(flag)
        )

        fun failed(message: String) = FlagResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = FlagResponse(
            State.NOT_FOUND,
            message
        )
    }
}

@Serializable
data class LanguageResponse(
    override val status: State,
    override val message: String,
    val language: List<Language> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = LanguageResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(language: List<Language>) = LanguageResponse(
            State.SUCCESS,
            "Task successful",
            language
        )
        fun success(language: Language) = LanguageResponse(
            State.SUCCESS,
            "Task successful",
            listOf(language)
        )

        fun failed(message: String) = LanguageResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = LanguageResponse(
            State.NOT_FOUND,
            message
        )
    }
}
@Serializable
data class UnitResponse(
    override val status: State,
    override val message: String,
    val unit: List<Unit> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = UnitResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(unit: List<Unit>) = UnitResponse(
            State.SUCCESS,
            "Task successful",
            unit
        )
        fun success(unit: Unit) = UnitResponse(
            State.SUCCESS,
            "Task successful",
            listOf(unit)
        )

        fun failed(message: String) = UnitResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = UnitResponse(
            State.NOT_FOUND,
            message
        )
    }
}

@Serializable
data class SectionResponse(
    override val status: State,
    override val message: String,
    val section: List<Section> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = SectionResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(section: List<Section>) = SectionResponse(
            State.SUCCESS,
            "Task successful",
            section
        )
        fun success(section: Section) = SectionResponse(
            State.SUCCESS,
            "Task successful",
            listOf(section)
        )

        fun failed(message: String) = SectionResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = SectionResponse(
            State.NOT_FOUND,
            message
        )
    }
}

@Serializable
data class WordResponse(
    override val status: State,
    override val message: String,
    val word: List<Word> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = WordResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(word: List<Word>) = WordResponse(
            State.SUCCESS,
            "Task successful",
            word
        )
        fun success(word: Word) = WordResponse(
            State.SUCCESS,
            "Task successful",
            listOf(word)
        )

        fun failed(message: String) = WordResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = WordResponse(
            State.NOT_FOUND,
            message
        )
    }
}

@Serializable
data class SentenceResponse(
    override val status: State,
    override val message: String,
    val sentence: List<Sentence> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = SentenceResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(sentence: List<Sentence>) = SentenceResponse(
            State.SUCCESS,
            "Task successful",
            sentence
        )
        fun success(sentence: Sentence) = SentenceResponse(
            State.SUCCESS,
            "Task successful",
            listOf(sentence)
        )

        fun failed(message: String) = SentenceResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = SentenceResponse(
            State.NOT_FOUND,
            message
        )
    }
}





