package kr.hanghae.deploy.domain.bookabledate

interface BookableDateRepository {
    fun findByDate(): List<BookableDate>
    fun findByDate(date: String): BookableDate?
}
