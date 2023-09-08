package com.openphonics.route.data

import io.ktor.resources.*

object Routing {
    const val DATA = "/data"
    const val LANGUAGES = "languages"
    const val UNITS = "units"
    const val SECTIONS = "sections"
    const val WORDS = "words"
    const val SENTENCES = "sentences"
    const val FLAGS = "flags"
    const val ID = "{id}"
    const val CREATE = "new"

}
@Resource(Routing.DATA)
class Data(){
    @Resource(Routing.LANGUAGES)
    class Language(val parent: Data = Data()){
        @Resource(Routing.ID)
        class Id(val parent: Language = Language(),  val id: Int)
        @Resource(Routing.CREATE)
        class Create(val parent: Language = Language())
    }
    @Resource(Routing.FLAGS)
    class Flag(val parent: Data = Data()){
        @Resource(Routing.ID)
        class Id(val parent: Flag = Flag(),  val id: String)
        @Resource(Routing.CREATE)
        class Create(val parent: Flag = Flag())
    }


    @Resource(Routing.UNITS)
    class Unit(val parent: Data = Data()){
        @Resource(Routing.ID)
        class Id(val parent: Unit = Unit(),  val id: Int)
        @Resource(Routing.CREATE)
        class Create(val parent: Unit = Unit())
    }
    @Resource(Routing.SECTIONS)
    class Section(val parent: Data = Data()){
        @Resource(Routing.ID)
        class Id(val parent: Section = Section(),  val id: Int)
        @Resource(Routing.CREATE)
        class Create(val parent: Section = Section())
    }

    @Resource(Routing.WORDS)
    class Word(val parent: Data = Data()){
        @Resource(Routing.ID)
        class Id(val parent: Word = Word(),  val id: Int)

        @Resource(Routing.CREATE)
        class Create(val parent: Word = Word())
    }

    @Resource(Routing.SENTENCES)
    class Sentence(val parent: Data = Data()){
        @Resource(Routing.ID)
        class Id(val parent: Sentence = Sentence(),  val id: Int)
        @Resource(Routing.CREATE)
        class Create(val parent: Sentence = Sentence())
    }
}