package cn.evolvefield.onebot.client.connection

//WS全局监听器
object WSGolab {
    val msgReceiveList = ArrayList<WSMsgReceive>()
}

/**
 * 如果返回true 则停止继续
 */
interface WSMsgReceive {
    fun onReceive(msg: String): Boolean
}