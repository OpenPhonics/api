package com.openphonics.route.progress

import io.ktor.resources.*


object Routing {
    const val PROGRESS = "/progress"
    const val LANGUAGES = "languages"
    const val UNITS = "units"
    const val SECTIONS = "sections"
    const val STREAK = "streak"
    const val ID = "{id}"
    const val CREATE = "new"
}
@Resource(Routing.PROGRESS)
class Progress(){
    @Resource(Routing.LANGUAGES)
    class LanguageProgress(val parent: Progress = Progress()){
        @Resource(Routing.ID)
        class Id(val parent: LanguageProgress = LanguageProgress(), val id: String){
            @Resource(Routing.STREAK)
            class Streak(val parent: Id)
        }

        @Resource(Routing.CREATE)
        class Create(val parent: LanguageProgress = LanguageProgress())
    }
    @Resource(Routing.UNITS)
    class UnitProgress(val parent: Progress = Progress()){
        @Resource(Routing.ID)
        class Id(val parent: UnitProgress = UnitProgress(), val id: String)
    }
    @Resource(Routing.SECTIONS)
    class SectionProgress(val parent: Progress = Progress()){
        @Resource(Routing.ID)
        class Id(val parent: SectionProgress = SectionProgress(), val id: String)
    }
}