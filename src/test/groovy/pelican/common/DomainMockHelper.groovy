package pelican.common

import io.pelican.Account
import io.pelican.AdditionalPhoto
import io.pelican.Country
import io.pelican.Catalog
import io.pelican.Layout
import io.pelican.Page
import io.pelican.Permission
import io.pelican.Product
import io.pelican.ProductOption
import io.pelican.ProductSpecification
import io.pelican.Role
import io.pelican.ShoppingCart
import io.pelican.ShoppingCartItem
import io.pelican.ShoppingCartItemOption
import io.pelican.Specification
import io.pelican.SpecificationOption
import io.pelican.State
import io.pelican.Transaction
import io.pelican.Upload
import io.pelican.Variant
import io.pelican.log.CatalogViewLog
import io.pelican.log.PageViewLog
import io.pelican.log.ProductViewLog
import io.pelican.log.SearchLog
import io.pelican.log.LoginLog



class DomainMockHelper {

	public static final Account getMockAccount(){
		def account = new Account(name: 'Robert Fripp', username: "robert", password: "robert", email: "robert@mail.com")
		return account
	}
	
	
	public static final AdditionalPhoto getMockAdditionalPhoto(product){
		def additionalPhoto = new AdditionalPhoto(name: 'Composite Chips Photo 1', imageUrl : '/image.jpg', detailsImageUrl: '/details.jpg', product: product)
		return additionalPhoto
	}

	
	public static final Catalog getMockCatalog(layout){
		def catalog = new Catalog(name: 'Poker Chips', layout: layout, description: "Poker Chips")
		return catalog
	}
	

	public static final Country getMockCountry(){
		def country = new Country(name: "United States")
		return country
	}
	
	
	public static final Layout getMockLayout(){
		def layout = new Layout(name : "Default Layout", content: "[[CONTENT]]", defaultLayout:true)
		return layout
	}
	
	
	public static final Page getMockPage(layout){
        def page = new Page(title: "Lorem ipsum", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit", layout: layout)
		return page
	}
	
	
	public static final Permission getMockPermission(account){
        def permission = new Permission(account: account, permission: 'transaction:1')
		return permission
	}
	
	
	public static final Product getMockProduct(layout){
        def product = new Product(name: 'Composite Poker Chips', price: 123.00, quantity:10, layout: layout)
		return product
	}
	
	
	public static final Product getMockProduct(catalog, layout){
        def product = new Product(name: 'Composite Poker Chips', price: 123.00, quantity:10, layout: layout)
	    product.addToCatalogs(catalog)
		return product
	}
	
	
	public static final ProductOption getMockProductOption(product){
        def productOption = new ProductOption(name: 'Graphite center', position: 0, product: product)
		return productOption
	}
	
	
	public static final ProductSpecification getMockProductSpecification(product, specification, specificationOption){
        def productSpecification = new ProductSpecification(product: product, specification: specification, specificationOption: specificationOption)
		return productSpecification
	}
	
	
	public static final Role getMockRole(){
        def role = new Role(authority: 'ROLE_ADMIN')
		return role
	}
	
	
	public static final ShoppingCart getMockShoppingCart(account){
		def shoppingCart = new ShoppingCart(status: "ACTIVE", taxes: new BigDecimal(0), shipping: new BigDecimal(13), subtotal: new BigDecimal(100), total: new BigDecimal(113))
		return shoppingCart
	}
	
	
	public static final ShoppingCartItem getMockShoppingCartItem(product, shoppingCart){
		def shoppingCartItem = new ShoppingCartItem(quantity: 3, product: product, shoppingCart: shoppingCart, regularPrice: new BigDecimal(100), checkoutPrice: new BigDecimal(100))
		return shoppingCartItem
	}

	
	public static final ShoppingCartItemOption getMockShoppingCartItemOption(variant, shoppingCartItem){
		def shoppingCartItemOption = new ShoppingCartItemOption(checkoutPrice: new BigDecimal(3), variant: variant, shoppingCartItem: shoppingCartItem)
		return shoppingCartItemOption
	}


	public static final Specification getMockSpecification(){
        def specification = new Specification(name: "Type", filterName: "type", position: 0)
		return specification
	}
	
	
	public static final SpecificationOption getMockSpecificationOption(specification){
        def specificationOption = new SpecificationOption(name: "Composite", position: 0, specification: specification)
		return specificationOption
	}


	public static final State getMockState(country){
        def state = new State(name: "Nevada", country: country)
		return state
	}
	
	

	public static final Transaction getMockTransaction(account, shoppingCart, country, state){
        def transaction = new Transaction()
		transaction.total = new BigDecimal(113)
		transaction.subtotal = new BigDecimal(100)
		transaction.shipping = new BigDecimal(13)
		transaction.taxes = new BigDecimal(0)
	
		transaction.status = "OPEN"
		transaction.orderDate = new Date()
	
		transaction.postageId = "1234567890"
		transaction.postageUrl = "http://www.google.com"

		transaction.shipName = "Jacques Fresco"
		transaction.shipAddress1 = "324 Main Street"
		transaction.shipAddress2 = ""
		transaction.shipCity = "Venus"
		transaction.shipState = state
		transaction.shipCountry = country
		transaction.shipZip = "12345"
	
		transaction.gateway = "Braintree"
		transaction.chargeId = "1234567890"
		
		transaction.account = account
		transaction.shoppingCart = shoppingCart
		
		return transaction
	}

	public static final Upload getMockUpload(){
        def upload = new Upload(url: "files/file.md")
		return upload
	}
	

	public static final Variant getMockVariant(productOption){
        def variant = new Variant(name: "Composite", price: 2.30, imageUrl: "/image.jpg", position: 0, productOption: productOption)
		return variant
	}
	
	
	public static final CatalogViewLog getMockCatalogViewLog(account, catalog){
        def catalogViewLog = new CatalogViewLog(catalog: catalog, account: account)
		return catalogViewLog
	}
	
	
	public static final PageViewLog getMockPageViewLog(account, page){
        def pageViewLog = new PageViewLog(page: page, account: account)
		return pageViewLog
	}
	
	
	public static final ProductViewLog getMockProductViewLog(account, product){
        def productViewLog = new ProductViewLog(product: product, account: account)
		return productViewLog
	}
	
	
	public static final SearchLog getMockSearchLog(account){
        def searchLog = new SearchLog(account: account, query: "Meaning of life...")
		return searchLog
	}
	
	
	public static final LoginLog getMockLoginLog(account){
        def productViewLog = new LoginLog(account: account, ipAddress: "1.2.3.4", date: new Date())
		return productViewLog
	}
	
	
}



