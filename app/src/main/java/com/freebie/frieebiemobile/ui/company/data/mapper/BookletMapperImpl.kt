package com.freebie.frieebiemobile.ui.company.data.mapper

import com.freebie.frieebiemobile.ui.feed.domain.BookletByStatus
import com.freebie.frieebiemobile.ui.feed.domain.BookletModel
import com.freebie.frieebiemobile.ui.feed.domain.BookletStatus
import com.freebie.protos.BookletModelProtos
import javax.inject.Inject

class BookletMapperImpl @Inject constructor() {

    fun mapBooklet(bookletList: MutableList<BookletModelProtos.BookletByStatus>): List<BookletByStatus> {
        val result = mutableListOf<BookletByStatus>()
        bookletList.forEach { bookletByStatusProto ->
            val booklets = bookletByStatusProto.bookletList.map { bookletProto ->
                BookletModel(
                    id = bookletProto.encryptedId,
                    name = bookletProto.name,
                    avatar = bookletProto.imageUrl
                )
            }
            result.add(
                BookletByStatus(
                    mapStatus(bookletByStatusProto.status), booklets
                )
            )
        }
        return result
    }

    private fun mapStatus(status: BookletModelProtos.Status): BookletStatus {
        return when (status) {
            BookletModelProtos.Status.IN_REVIEW -> BookletStatus.IN_REVIEW
            BookletModelProtos.Status.ACTIVE -> BookletStatus.ACTIVE
            BookletModelProtos.Status.EXPIRED -> BookletStatus.EXPIRED
            BookletModelProtos.Status.UNRECOGNIZED -> BookletStatus.UNRECOGNIZED
        }
    }

}