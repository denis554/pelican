package io.pelican.log

import io.pelican.Product
import io.pelican.Account


class ProductViewLog {
	
	ProductViewLog(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	Product product
	Account account
	
	Date dateCreated
	Date lastUpdated
	
	static constraints = {
		uuid(nullable:true)
		product(nullable:false)
		account(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_VIEW_LOG_PK_SEQ']
    }
	
}
