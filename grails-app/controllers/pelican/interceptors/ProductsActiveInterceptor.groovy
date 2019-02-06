package pelican.interceptors

import pelican.common.ControllerConstants

class ProductsActiveInterceptor {

	ProductsActiveInterceptor(){
		match(controller:"product", action: ~/(list|create|edit|show|additional_photos|add_product_option|product_options|admin_search)/)
	}

    boolean before() { 
    	request.productsActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
