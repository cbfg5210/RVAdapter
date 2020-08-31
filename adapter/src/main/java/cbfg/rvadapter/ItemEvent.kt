package cbfg.rvadapter

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/31 17:15
 * 功能描述: 某类型数据 item 属性变化事件
 */
data class ItemEvent(val clazz: Class<*>, val obj: Any)