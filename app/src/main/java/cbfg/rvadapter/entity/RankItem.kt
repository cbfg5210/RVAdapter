package cbfg.rvadapter.entity

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/12 9:52
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/12 9:52
 * 修改内容：
 */
data class RankItem(val id: Int, var rank: Int) {
    /**
     * 重写以下方法,避免 List.contains 判断数据不准确
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other !is RankItem) false
        else this.id == other.id
    }

    /**
     * 重写以下方法,避免 Set 存放相同值的对象
     * 这里的重写并不严格，正式使用的话要注意唯一性，并且与 equals 方法结果一致
     */
    override fun hashCode(): Int = id
}
