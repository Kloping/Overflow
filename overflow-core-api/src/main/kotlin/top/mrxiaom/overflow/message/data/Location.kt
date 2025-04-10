@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
package top.mrxiaom.overflow.message.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.message.code.CodableMessage
import net.mamoe.mirai.message.data.AbstractPolymorphicMessageKey
import net.mamoe.mirai.message.data.ConstrainSingle
import net.mamoe.mirai.message.data.MessageContent
import net.mamoe.mirai.message.data.MessageKey
import net.mamoe.mirai.utils.safeCast

/**
 * 定位分享
 */
@Serializable
@SerialName(Location.SERIAL_NAME)
public data class Location(
    /**
     * 纬度
     */
    public val lat: Float,
    /**
     * 经度
     */
    public val lon: Float,
    /**
     * 定位消息标题 (可能为空)，通常为地名
     */
    public val title: String = "",

    /**
     * 定位消息内容 (可能为空)，通常为详细地址
     */
    public val content: String = ""
) : MessageContent, ConstrainSingle, CodableMessage {
    private val _contentValue: String by lazy(LazyThreadSafetyMode.NONE) {
        "[位置]$title $content(经度:$lon 纬度:$lat)"
    }
    public override fun toString(): String = "[overflow:location,lon=$lat,lat=$lon,title=$title,content=$content]"
    public override fun contentToString(): String = _contentValue

    override fun appendMiraiCodeTo(builder: StringBuilder) {
        builder.append("[mirai:location:").append(lat).append(",")
            .append(lon).append(",")
            .append(title).append(",")
            .append(content).append("]")
    }

    override val key: MessageKey<Location> get() = Key
    public companion object Key :
        AbstractPolymorphicMessageKey<MessageContent, Location>(MessageContent, { it.safeCast() }) {
        public const val SERIAL_NAME: String = "Location"
    }
}
