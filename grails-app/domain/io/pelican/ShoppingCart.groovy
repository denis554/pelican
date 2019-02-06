package io.pelican

class ShoppingCart {
	
	ShoppingCart(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	String status
	BigDecimal taxes
	BigDecimal shipping
	BigDecimal subtotal
	BigDecimal total
		
	//TODO:remove for anonymous checkout?	
	Account account
		
  	Date dateCreated
	Date lastUpdated
	
	String shipmentId
	String shipmentDays
	String shipmentCarrier
	String shipmentService
	String shipmentRateId
	String shipmentCurrency
	
	
	static hasMany = [ shoppingCartItems : ShoppingCartItem ]
	
    static constraints = {	
		uuid(nullable:true)
		account(nullable:true)
		status(nullable:false)
		taxes(nullable:true)
		shipping(nullable:true)
		subtotal(nullable:true)
		total(nullable:true)
		shipmentId(nullable:true)
		shipmentDays(nullable:true)
		shipmentCarrier(nullable:true)
		shipmentService(nullable:true)
		shipmentRateId(nullable:true)
		shipmentCurrency(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_SHOPPING_CART_SEQ']
    }
	
}
