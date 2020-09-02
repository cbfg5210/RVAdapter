package cbfg.rvadapter.demo.state

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/9/2 11:28
 * 功能描述:
 */
data class RVState(val state: State, val imgSrc: Int, val tip: String) {
    enum class State {
        LOADING,
        EMPTY,
        ERROR
    }
}