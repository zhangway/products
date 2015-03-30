package models

import org.joda.time.LocalDate

case class Person(id: Long, name: String, age: Int, birthday: LocalDate)

object Person {
  var persons = Set (
                      Person(1L, "Wei Zhang", 32, new LocalDate(1983,6,13)),
                      Person(2L, "Test", 30, new LocalDate(1983,4,20))
                    )
    
  def findAll = persons.toList.sortBy(_.id)
  
  def findById(id: Long) = persons.find(_.id == id)
  
  def add(p: Person) {
    persons = persons + p
  }
}