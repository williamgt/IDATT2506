package no.ntnu.idatt2506.stud.williagt.register_friend

import java.io.Serializable

data class Person(var name: String, var birthday: String): Serializable {
    override fun toString(): String = "$name, $birthday"
}
