package com.freebie.frieebiemobile.ui.booklet.data.mapper

import com.freebie.frieebiemobile.ui.feed.domain.BookletModel
import com.freebie.protos.BookletModelProtos
import javax.inject.Inject

class BookletDataMapper @Inject constructor() {
    fun map(proto: BookletModelProtos.Booklet) : BookletModel {
        return BookletModel(
            id = proto.encryptedId,
            avatar = proto.imageUrl,
            name = proto.name
        )
    }
}