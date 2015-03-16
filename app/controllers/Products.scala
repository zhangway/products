package controllers
import play.api.mvc.{Action, Controller}
import models.Product
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.mvc.Flash

object Products extends Controller {
  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying(
        "validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> optional(text)
    )(Product.apply)(Product.unapply)
  )
  
  def newProduct = Action { implicit request =>
    val form = if (request.flash.get("error").isDefined)
      productForm.bind(request.flash.data)
    else
      productForm
    Ok(views.html.products.editProduct(form))
  }
  def save = Action { implicit request =>
    val newProductForm = productForm.bindFromRequest()
    newProductForm.fold(
      hasErrors = { form =>
        BadRequest(views.html.products.editProduct(form))
       //Redirect(routes.Products.newProduct()).
         //flashing(Flash(form.data) +
           //("error" -> Messages("validation.errors")))
      },
      success = { newProduct =>
        Product.add(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).
          flashing("success" -> message)
      } 
    )
  }
  def list = Action { implicit request =>
    val products = Product.findAll
    Ok(views.html.products.list(products))
  }
  
  def show(ean: Long) = Action { implicit request =>
    Product.findByEan(ean).map { product => 
      Ok(views.html.products.details(product))}.getOrElse(NotImplemented)  
  }
  
  def hello = Action { request =>
    NotImplemented
  }

}
