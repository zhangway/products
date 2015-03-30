package models

case class Product(
  ean: Long,
  name: String,
  description: String)

object Product {
  var products = Set (Product(5010255079763L, "Paperclips Large", "Large Plain pack"),
    Product(5010255079753L, "Paperclips small", "Small Plain pack"))
    
  def findAll = products.toList.sortBy(_.ean)
  
  def findByEan(ean: Long) = products.find(_.ean == ean)
  
  def add(product: Product) {
    products = products + product
  }
}
