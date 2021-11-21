package com.example.string_matcher.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javax.persistence.*

@Entity
@Table(name = Countries.TABLE_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Countries(
//    @Column(unique = true)
    val country: String = "",
    @OneToMany(
        targetEntity = Cities::class,
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    val cities: Set<Cities> = emptySet()
) {
    companion object {
        const val TABLE_NAME = "countries"
    }

    @Id
    @GeneratedValue
    val id: Long = 0
}