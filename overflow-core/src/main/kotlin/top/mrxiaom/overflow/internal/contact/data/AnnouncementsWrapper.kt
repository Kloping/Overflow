@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
package top.mrxiaom.overflow.internal.contact.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.contact.announcement.*
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.currentTimeSeconds
import top.mrxiaom.overflow.internal.contact.GroupWrapper
import top.mrxiaom.overflow.internal.utils.FastImageInfo
import top.mrxiaom.overflow.spi.FileService

internal class AnnouncementsWrapper(
    val impl: GroupWrapper,
    internal var list: List<OnlineAnnouncementWrapper>
) : Announcements {
    override fun asFlow(): Flow<OnlineAnnouncement> = list.asFlow()

    override suspend fun delete(fid: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(fid: String): OnlineAnnouncement? {
        TODO("Not yet implemented")
    }

    override suspend fun members(fid: String, confirmed: Boolean): List<NormalMember> {
        TODO("Not yet implemented")
    }

    override suspend fun remind(fid: String) {
        TODO("Not yet implemented")
    }

    override suspend fun publish(announcement: Announcement): OnlineAnnouncement {
        impl.bot.impl.sendGroupNotice(
            impl.id,
            announcement.content,
            announcement.parameters.image?.file
        )
        return OnlineAnnouncementWrapper(
            content = announcement.content,
            group = impl,
            senderId = impl.bot.id,
            parameters = announcement.parameters
        )
    }

    override suspend fun uploadImage(resource: ExternalResource): AnnouncementImage {
        val size = FastImageInfo(resource.inputStream())
        return AnnouncementImage.create("\n${FileService.instance!!.upload(resource)}\n", size?.height ?: 0, size?.width ?: 0)
    }

    suspend fun update() {
        list = impl.fetchAnnouncements().list
    }

    companion object {
        suspend fun GroupWrapper.fetchAnnouncements(): AnnouncementsWrapper {
            val list = bot.impl.getGroupNotice(id).data.map {
                val message = it.message ?: return@map null
                OnlineAnnouncementWrapper(
                    content = message.text,
                    group = this,
                    senderId = it.senderId,
                    publicationTime = it.publishTime,
                    parameters = AnnouncementParametersBuilder().apply {
                        for (image in message.images) {
                            image(AnnouncementImage.create(
                                image.id, image.height.toIntOrNull() ?: 0, image.width.toIntOrNull() ?: 0
                            ))
                        }
                    }.build()
                )
            }
            return AnnouncementsWrapper(this, list.filterNotNull())
        }
    }
}

internal class OnlineAnnouncementWrapper(
    override val content: String,
    override val group: Group,
    override val senderId: Long,
    override val parameters: AnnouncementParameters = AnnouncementParameters.DEFAULT,
    override val allConfirmed: Boolean = false,
    override val confirmedMembersCount: Int = 0,
    override val fid: String = "",
    override val publicationTime: Long = currentTimeSeconds(),
    override val sender: NormalMember? = group[senderId],
) : OnlineAnnouncement

internal val AnnouncementImage.file: String
    get() = if (url.contains("\n")) url.split("\n")[1] else url
