package com.openphonics.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LanguageProgress(
    val progressId: String = "",
    val started: Long = 0,
    val updated: Long = 0,
    val streak: Int = 0,
    val xp: Int = 0,
    val nativeId: String = "",
    val languageId: String = "",
    val languageName: String = "",
    val flag: String = "",
    val units: List<UnitProgress> = emptyList(),
    val hasData: Boolean = false

) {
    companion object {
        fun create(languageProgress: com.openphonics.data.model.progress.LanguageProgress): LanguageProgress = LanguageProgress(
            languageProgress.progressId,
            languageProgress.started,
            languageProgress.updated,
            languageProgress.streak,
            languageProgress.xp,
            languageProgress.nativeId,
            languageProgress.languageId,
            languageProgress.languageName,
            languageProgress.flag,
            languageProgress.units.map {
                UnitProgress.create(it)
            },
            languageProgress.hasData,
        )
    }
}
@Serializable
data class UnitProgress(
    val title: String,
    val order: Int,
    val sections: List<SectionProgress>,
    val hasData: Boolean,
    val progressId: String
)  {
    companion object {
        fun create(unitProgress: com.openphonics.data.model.progress.UnitProgress) = UnitProgress(
            unitProgress.title,
            unitProgress.order,
            unitProgress.sections.map {
                SectionProgress.create(it)
            },
            unitProgress.hasData,
            unitProgress.progressId
        )
    }
}

@Serializable
data class SectionProgress(
    val currentLesson: Int,
    val isLegendary: Boolean,
    val progressId: String,
    val learnedWords: List<Word>,
    val title: String,
    val order: Int,
    val lessonCount: Int,
    val words: List<Word>,
    val sentences: List<Sentence>,
    val hasData: Boolean,
) {
    companion object {
        fun create(sectionProgress: com.openphonics.data.model.progress.SectionProgress) = SectionProgress(
            sectionProgress.currentLesson,
            sectionProgress.isLegendary,
            sectionProgress.progressId,
            sectionProgress.learnedWords.map {
                Word.create(it)
            },
            sectionProgress.title,
            sectionProgress.order,
            sectionProgress.lessonCount,
            sectionProgress.words.map {
                Word.create(it)
            },
            sectionProgress.sentences.map {
                Sentence.create(it)
            },
            sectionProgress.hasData
        )
    }
}
@Serializable
data class LanguageProgressResponse(
    override val status: State,
    override val message: String,
    val progress: List<LanguageProgress> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = LanguageProgressResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(languageProgress: List<LanguageProgress>) = LanguageProgressResponse(
            State.SUCCESS,
            "Task successful",
            languageProgress
        )
        fun success(languageProgress: LanguageProgress) = LanguageProgressResponse(
            State.SUCCESS,
            "Task successful",
            listOf(languageProgress)
        )

        fun failed(message: String) = LanguageProgressResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = LanguageProgressResponse(
            State.NOT_FOUND,
            message
        )
    }
}
@Serializable
data class UnitProgressResponse(
    override val status: State,
    override val message: String,
    val progress: List<UnitProgress> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = UnitProgressResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(unitProgress: List<UnitProgress>) = UnitProgressResponse(
            State.SUCCESS,
            "Task successful",
            unitProgress
        )
        fun success(unitProgress: UnitProgress) = UnitProgressResponse(
            State.SUCCESS,
            "Task successful",
            listOf(unitProgress)
        )

        fun failed(message: String) = UnitProgressResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = UnitProgressResponse(
            State.NOT_FOUND,
            message
        )
    }
}


@Serializable
data class SectionProgressResponse(
    override val status: State,
    override val message: String,
    val progress: List<SectionProgress> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = UnitProgressResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(unitProgress: List<UnitProgress>) = UnitProgressResponse(
            State.SUCCESS,
            "Task successful",
            unitProgress
        )
        fun success(unitProgress: UnitProgress) = UnitProgressResponse(
            State.SUCCESS,
            "Task successful",
            listOf(unitProgress)
        )

        fun failed(message: String) = UnitProgressResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = UnitProgressResponse(
            State.NOT_FOUND,
            message
        )
    }
}



