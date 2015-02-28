package models

case class Product(ean: Long,
                   name: String,
                   description: Option[String])
                   
                   
object Product {
  var products = Set ( Product (5010255079763L, "Paperclips Large", Some("Large Plain Pack of 1000")),
                       Product (5018206244666L, "Giant Paperclips", Some("Giant Plain 51mm 100 pack")),
                       Product (5018306332812L, "Paperclip Giant Plain",Some("Giant Plain Pack of 10000")),
                       Product(5018306312913L, "No Tear Paper Clip",Some("No Tear Extra Large Pack of 1000")),
                       Product(5018206244611L, "Zebra Paperclips",Some("Zebra Length 28mm Assorted 150 Pack"))
                     )
                     
  def findAll = products.toList.sortBy(_.ean)
  def findByEan(ean: Long) = products.find(_.ean == ean)
  def add(product: Product) {
    products = products + product
  }
}
