package cn.evolvefield.onebot.sdk.event.notice.group

import cn.evolvefield.onebot.sdk.event.notice.NoticeEvent
import com.google.gson.annotations.SerializedName
import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.experimental.SuperBuilder

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
class GroupNameChangeNoticeEvent : NoticeEvent() {
    @SerializedName("name")
    var name = ""
    @SerializedName("group_id")
    var groupId = 0L
    @SerializedName("operator_id")
    var operatorId = 0L
}
