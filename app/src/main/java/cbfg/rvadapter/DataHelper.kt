package cbfg.rvadapter

import cbfg.rvadapter.entity.Header
import cbfg.rvadapter.entity.Person

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 16:11
 * 功能描述:
 */
object DataHelper {
    fun getClassifiedPeople(): List<Any> {
        return arrayListOf(
            Header("Header - 111"),
            Person(android.R.color.holo_orange_dark, "aaa"),
            Person(android.R.color.holo_blue_dark, "bbb"),
            Person(android.R.color.darker_gray, "ccc"),
            Person(android.R.color.holo_purple, "ddd"),
            Person(android.R.color.holo_green_dark, "eee"),
            Person(android.R.color.holo_orange_dark, "fff"),
            Person(android.R.color.holo_blue_dark, "ggg"),
            Header("Header - 222"),
            Person(android.R.color.darker_gray, "hhh"),
            Person(android.R.color.holo_purple, "iii"),
            Person(android.R.color.holo_green_dark, "jjj"),
            Person(android.R.color.holo_orange_dark, "kkk"),
            Person(android.R.color.holo_blue_dark, "lll"),
            Header("Header - 333"),
            Person(android.R.color.darker_gray, "mmm"),
            Person(android.R.color.holo_purple, "nnn"),
            Person(android.R.color.holo_green_dark, "ooo"),
            Person(android.R.color.holo_orange_dark, "ppp"),
            Person(android.R.color.holo_blue_dark, "qqq"),
            Header("Header - 444"),
            Person(android.R.color.darker_gray, "rrr"),
            Person(android.R.color.holo_purple, "sss"),
            Person(android.R.color.holo_green_dark, "ttt"),
            Person(android.R.color.holo_orange_dark, "uuu"),
            Person(android.R.color.holo_blue_dark, "vvv"),
            Header("Header - 555"),
            Person(android.R.color.darker_gray, "www"),
            Person(android.R.color.holo_purple, "xxx"),
            Person(android.R.color.holo_green_dark, "yyy"),
            Person(android.R.color.holo_green_dark, "zzz")
        )
    }

    fun getPeople(): List<Person> {
        return arrayListOf(
            Person(android.R.color.holo_orange_dark, "aaa"),
            Person(android.R.color.holo_blue_dark, "bbb"),
            Person(android.R.color.darker_gray, "ccc"),
            Person(android.R.color.holo_purple, "ddd"),
            Person(android.R.color.holo_green_dark, "eee"),
            Person(android.R.color.holo_orange_dark, "fff"),
            Person(android.R.color.holo_blue_dark, "ggg"),
            Person(android.R.color.darker_gray, "hhh"),
            Person(android.R.color.holo_purple, "iii"),
            Person(android.R.color.holo_green_dark, "jjj"),
            Person(android.R.color.holo_orange_dark, "kkk"),
            Person(android.R.color.holo_blue_dark, "lll"),
            Person(android.R.color.darker_gray, "mmm"),
            Person(android.R.color.holo_purple, "nnn"),
            Person(android.R.color.holo_green_dark, "ooo"),
            Person(android.R.color.holo_orange_dark, "ppp"),
            Person(android.R.color.holo_blue_dark, "qqq"),
            Person(android.R.color.darker_gray, "rrr"),
            Person(android.R.color.holo_purple, "sss"),
            Person(android.R.color.holo_green_dark, "ttt"),
            Person(android.R.color.holo_orange_dark, "uuu"),
            Person(android.R.color.holo_blue_dark, "vvv"),
            Person(android.R.color.darker_gray, "www"),
            Person(android.R.color.holo_purple, "xxx"),
            Person(android.R.color.holo_green_dark, "yyy"),
            Person(android.R.color.holo_green_dark, "zzz")
        )
    }

    fun getTextList(): List<String> {
        return arrayListOf(
            "aaa",
            "bbb",
            "ccc",
            "ddd",
            "eee",
            "fff",
            "ggg",
            "hhh",
            "iii",
            "jjj",
            "kkk",
            "lll",
            "mmm",
            "nnn",
            "ooo",
            "ppp",
            "qqq",
            "rrr",
            "sss",
            "ttt",
            "uuu",
            "vvv",
            "www",
            "xxx",
            "yyy",
            "zzz"
        )
    }
}