package cbfg.rvadapter

import cbfg.rvadapter.entity.Person

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 16:11
 * 功能描述:
 */
object DataHelper {
    fun getPeople(): List<Person> {
        return arrayListOf(
            Person(R.mipmap.ic_launcher, "aaa"),
            Person(android.R.color.holo_blue_dark, "bbb"),
            Person(android.R.color.darker_gray, "ccc"),
            Person(android.R.color.holo_purple, "ddd"),
            Person(android.R.color.holo_green_dark, "eee")
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