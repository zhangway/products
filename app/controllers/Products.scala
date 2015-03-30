package controllers
import play.api.mvc.{ Action, Controller, Flash }
import play.api.libs.json.Json
import models.Product
import play.api.data.Form
import play.api.data.Forms.{ mapping, longNumber, nonEmptyText }
import play.api.i18n.Messages

object Products extends Controller {

  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying(
        "validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText)(Product.apply)(Product.unapply)
      verifying ("New password doesn't match confirmed password", 
               f => {
                 println(f.name)
                 println(f.description)
                 f.name == f.description
               }
       ))

  def list = Action { implicit request =>
    val products = Product.findAll
    Ok(views.html.list(products))
  }

  def show(ean: Long) = Action { implicit request =>
    Product.findByEan(ean).map { p => Ok(views.html.details(p)) }.getOrElse(NotFound)
  }

  def newProduct = Action { implicit request =>
    val form = if (request.flash.get("error").isDefined) {
      productForm.bind(request.flash.data)
    } else {
      productForm.fill(Product(222, "hdddd", "dd"))
    }
    Ok(views.html.editProduct(form))

  }

  def save = Action { implicit request =>
    val newProductForm = productForm.bindFromRequest()
    newProductForm.fold(
      hasErrors = { form =>
        Redirect(routes.Products.newProduct()).
          flashing(Flash(form.data) +
            ("error" -> Messages("validation.errors")))
      },
      success = { newProduct =>
        Product.add(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).
          flashing("success" -> message)
      })
  }
  
  def table = Action { implicit request =>
    val products = Product.findAll.map(_.ean)
    val products2 = Product.findAll
    //Ok(Json.toJson(products))
    Ok(views.html.table(products2))
  }
}