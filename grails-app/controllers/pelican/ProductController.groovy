package pelican

import org.springframework.dao.DataIntegrityViolationException
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import pelican.common.BaseController
import java.awt.Graphics2D
import grails.util.Environment
import grails.converters.*

import io.pelican.Account
import io.pelican.log.ProductViewLog
import io.pelican.log.SearchLog

import io.pelican.Product
import io.pelican.Catalog
import io.pelican.AdditionalPhoto
import io.pelican.ProductOption
import io.pelican.ProductSpecification 
import io.pelican.Variant
import io.pelican.Layout


import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class ProductController {

    static allowedMethods = [ save: "POST", update: "POST", delete: "POST", save_option: 'POST' ]
	
    def applicationService
	def currencyService
	

	def index() {
    	redirect(controller: 'store', action: "index")
    }
	
 	@Secured(['permitAll'])
	def search(){		
		if(params.query && params.query.length() >= 4){
			def max = 12
			def offset = params.offset ? params.offset : 0
			
			def productNameCriteria = Product.createCriteria()
			def products = productNameCriteria.list(max: max, offset: offset, sort: "name", order: "desc"){
				or {
					ilike("name", "%${params.query}%")
					ilike("description", "%${params.query}%")
				}
			}
				
			def searchLog = new SearchLog()
			searchLog.query = params.query.toLowerCase()
			
			def accountInstance
			if(principal?.username){
				accountInstance = Account.findByUsername(principal?.username)
				searchLog.account = accountInstance
			}
			searchLog.save(flush:true)
			
			if(accountInstance){
				accountInstance.searches = SearchLog.countByAccount(accountInstance)
				accountInstance.save(flush:true)
			}			
			
			[products : products, offset : offset, max : max, query : params.query ]
		}else{
			flash.message = "Search query must be at least 4 characters"
			[products : [], offset : 0, max : 10, query : "" ]
		}
	}
	
	
	
	
	@Secured(['permitAll'])
	def details(Long id){    
		def productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = "Product not found..."
            redirect(controller: 'store', action: "index")
            return
        }
		
		def baseProductOptions = ProductOption.findAllByProduct(productInstance)
		def productOptions = []
		
		baseProductOptions.each(){ productOption ->
			
			def option = [:]
			option.id = productOption.id
			option.name = productOption.name
			
			def variants = Variant.findAllByProductOption(productOption)
			option.variants = variants
			
			productOptions.add(option)
			
		}
		
		def productViewLog = new ProductViewLog()
		productViewLog.product = productInstance
		
		def accountInstance
		if(principal?.username){
			accountInstance = Account.findByUsername(principal?.username)
			productViewLog.account = accountInstance
		}
		productViewLog.save(flush:true)
		
		if(accountInstance){
			accountInstance.productViews = ProductViewLog.countByAccount(accountInstance)
			accountInstance.save(flush:true)
		}


		def url = request.getRequestURL()
		
		def split = url.toString().split("/${applicationService.getContextName()}/")
		def httpSection = split[0]
		def urlPre = "${httpSection}/${applicationService.getContextName()}/"
		
		def shareUrl = urlPre + "product/details/${productInstance.id}"
		def shareImageUrl = productInstance.imageUrl ? (urlPre + productInstance?.imageUrl) : urlPre + "images/app/no-image.jpg"
		def title = productInstance?.name
		def description = productInstance?.description ? productInstance.description : productInstance?.name + " Product Details"


		[ productInstance: productInstance, productOptions: productOptions, 
		title: title, description: description, shareUrl: shareUrl, shareImageUrl: shareImageUrl ]
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
    def list(Integer max) {
		authenticatedAdmin { adminAccount ->
			params.max = Math.min(max ?: 10, 100)
			params.sort = params.sort ? params.sort : "id"
			params.order = params.order ? params.order : "asc"
			
    		[productInstanceList: Product.list(params), productInstanceTotal: Product.count(), catalogCount: Catalog.count()]
		}
    }



    @Secured(['ROLE_ADMIN'])
    def outofstock(Integer max) {
		authenticatedAdmin { adminAccount ->
			params.max = Math.min(max ?: 10, 100)
    		request.productInstanceList = Product.findAllByQuantity(0)
			request.productInstanceTotal = Product.countByQuantity(0)
			render(view : 'list')
		}
    }
	

    @Secured(['ROLE_ADMIN'])
	def admin_search(){
		authenticatedAdmin { adminAccount ->

			if(params.query && params.query.length() >= 3){
				/**def p1 = Product.findAll("from Product as p where UPPER(p.name) like UPPER('%${params.query}%') AND p.disabled = false")
				def p2 = Product.findAll("from Product as p where UPPER(p.description) like UPPER('%${params.query}%') AND p.disabled = false")
				
				
				p1.collect( { productA ->
				    def productB = p2.find { it.name == productA.name }
					p2.remove(productB)
				})
				**/
				def max = params.max ? params.max : 10
				def offset = params.offset ? params.offset : 0
				
				def productNameCriteria = Product.createCriteria()
				def productInstanceList = productNameCriteria.list(max: max, offset: offset, sort: "id", order: "asc"){
					or {
						ilike("name", "%${params.query}%")
						ilike("description", "%${params.query}%")
					}
				}
				
				def productNameCriteriaTotal = Product.createCriteria()
				def productInstanceListTotal = productNameCriteriaTotal.list(){
					or {
						ilike("name", "%${params.query}%")
						ilike("description", "%${params.query}%")
					}
				}
        	
				//def productInstanceList = p1 + p2
				request.productInstanceList = productInstanceList
				request.productInstanceTotal = productInstanceListTotal.size()
				request.query = params.query
				
				render(view:'list')
			}else{
				flash.message = "Search term must be at least 3 characters long"
				redirect(action:'list')
			}
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
    def create() {
		authenticatedAdmin { adminAccount ->
			
			def layouts = Layout.list()
			
			if(Catalog.count() == 0){
				flash.message = "You must create at least one Catalog before creating Products"
				redirect(controller:'product', action: 'list')
				return
			}
			
			def productInstance = new Product(params)
			
			def catalogIdsArray = []
			if(productInstance?.catalogs){
				catalogIdsArray = productInstance?.catalogs.collect { it.id }
			}
			def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)
			
			[ productInstance: productInstance, catalogIdSelectionList: catalogIdSelectionList, layouts: layouts ]
    	}
	}	
	
	
	def resizeImage(originalImage, type, height, width){
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
 
		return resizedImage;
	}


	@Secured(['ROLE_ADMIN'])
    def save() {
		authenticatedAdmin { adminAccount ->
		    
			try{
				
				resolveSalesPrice(params)
				
				def productInstance = new Product(params)
				
				productInstance.price = new BigDecimal(params.price)
				if(params.salesPrice && params.salesPrice != "-"){
					productInstance.salesPrice = new BigDecimal(params.salesPrice)
				}
				
//				println "product sale price : " + productInstance.salesPrice
            	
				def catalogIdsArray = []
				def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)
				
				
	    		if(!productInstance.validate()){
					println "**************************"
					println "***      INVALID       ***"
					println "**************************"
					
					flash.message = "Information is invalid, please make sure name is unique"
					render(view:"create",  model: [productInstance: productInstance, catalogIdSelectionList: catalogIdSelectionList])
					return
				}	
					
				def imageFile = request.getFile('image')
				
				BufferedImage originalImage = null;
				def fullFileName = imageFile.getOriginalFilename()
				
				String[] nameSplit = fullFileName.toString().split("\\.")
				def fileName = nameSplit[0]
				
				fileName = fileName.replaceAll("[^\\w\\s]","")
				fileName = fileName.replaceAll(" ", "_")
				
				def productName = productInstance.name
				
				productName = productName.replaceAll("[^\\w\\s]",""); 
				productName = productName.replaceAll(" ", "_")
		    	
				try {
	        	
					
					originalImage = ImageIO.read(imageFile.getInputStream());
		 		   	
					if(originalImage){
					
		 		    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            	
						
						//def baseUrl = "images/products/${productName}/"
						def baseUrl = "images/"
						
						def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
						absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
						//def baseDirectory = "${absolutePath}products/${productName}/"
						def baseDirectory = "${absolutePath}"
						
						
						new File(baseDirectory).mkdirs();
						
						productInstance.detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
 						BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
					
						def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
						ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
						
						
						def imageUrl = "${baseUrl}${fileName}.jpg"
						productInstance.imageUrl = imageUrl
				
						def imageLocation = "${baseDirectory}${fileName}.jpg"
						ImageIO.write(originalImage, "jpg",new File(imageLocation));
					
					}
					
					
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
				
		    	if (!productInstance.save(flush: true)) {
		    	    render(view: "create", model: [productInstance: productInstance, catalogIdSelectionList: catalogIdSelectionList])
		    	    return
		    	}
		    	
		    	
				if(!params.catalogIds){
					flash.error = "<strong>No Catalogs Defined</strong><br/> You must select a catalog in order to make the product visible from a catalog menu. <br/>Please specify at least <strong>1 catalog</strong> before continuing."
    	    	    redirect(action: "edit", id: productInstance.id )
    	    	    return
				}
				
				def catalogSelectedIdsArray = params.catalogIds.split(',').collect{it as int}
				
				if(!catalogSelectedIdsArray){
					flash.error = "Something went wrong while processing update. Please try again."
    	    	    redirect(action: "edit", id: productInstance.id )
					return
				}    	  
				
				productInstance.catalogs = null
				catalogSelectedIdsArray.each{ catalogId ->
					def catalog = Catalog.get(catalogId)
					if(catalog){
						productInstance.addToCatalogs(catalog)
						productInstance.save(flush:true)
					}
				}
				
		    	
		    	flash.message = "Successfully created product"
		    	redirect(action: "edit", id: productInstance.id)	
			
			}catch(Exception e){
				e.printStackTrace()
				flash.message = "Something went wrong while trying to save your product..."
				redirect(action:"create", model: [productInstance: productInstance])
			}	
		}	
    }

	


	@Secured(['ROLE_ADMIN'])
    def edit(Long id) {
		authenticatedAdminProduct { adminAccount, productInstance ->
			
			def layouts = Layout.list()
			
			if(Catalog.count() == 0){
				flash.error = "Product will not display in store front until Catalogs have been created and products have been added to Catalogs."
			}
			
			def catalogIdsArray = []
			if(productInstance?.catalogs){
				catalogIdsArray = productInstance?.catalogs.collect { it.id }
			}
			def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)
    	    [ productInstance: productInstance, catalogIdsArray: catalogIdsArray, catalogIdSelectionList: catalogIdSelectionList, layouts: layouts ]
		}
    }
	
	
	

	def getCatalogIdSelectionList(catalogIdsArray){	
		def catalogMenuString = "<ul class=\"catalog_list admin-catalog-selection\">"
		def toplevelCatalogs = Catalog.findAllByToplevel(true)
		toplevelCatalogs.each{ catalog ->
			def checked = ""
			if(catalogIdsArray.contains(catalog.id)){
				checked = "checked"
			}
			catalogMenuString += "<li><input type=\"checkbox\" id=\"checkbox_${catalog.id}\" class=\"catalog_checkbox\" data-id=\"${catalog.id}\" data-name=\"${catalog.name}\" ${checked}>&nbsp;${catalog.name}"
			if(catalog.subcatalogs){
				def subcatalogMenuString = getAllSubcatalogLists(catalog, catalogIdsArray)
				catalogMenuString += subcatalogMenuString
			}
			catalogMenuString += "</li>"
		}
		catalogMenuString += "</ul>"
	}
	
	
	def getAllSubcatalogLists(catalog, catalogIdsArray){
		def subcatalogsMenu = "<ul class=\"catalog_list admin-subcatalog-selection\">"
		catalog.subcatalogs.sort { it.id }
		catalog.subcatalogs.each{ subcatalog ->
			def checked = ""
			if(catalogIdsArray.contains(subcatalog.id)){
				checked = "checked"
			}
			subcatalogsMenu += "<li><input type=\"checkbox\" id=\"checkbox_${subcatalog.id}\" class=\"catalog_checkbox\" data-id=\"${subcatalog.id}\" data-name=\"${subcatalog.name}\" ${checked}>&nbsp;${subcatalog.name}"
			if(subcatalog.subcatalogs){
				subcatalogsMenu += getAllSubcatalogLists(subcatalog, catalogIdsArray)
			}
			subcatalogsMenu += "</li>"
		}
		subcatalogsMenu += "</ul>"
		
		return subcatalogsMenu
	}
	
	
	
	


	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
		authenticatedAdminProduct { adminAccount, productInstance ->
			
			try{

				def productCatalogIdsArray = []
				if(productInstance?.catalogs){
					productCatalogIdsArray = productInstance?.catalogs.collect { it.id }
				}
				def catalogIdSelectionList = getCatalogIdSelectionList(productCatalogIdsArray)
				
				
				if(!params.catalogIds){
					flash.error = "<strong>No Catalogs Defined</strong><br/> You must select a catalog in order to make the product visible from a catalog menu. <br/>Please specify at least <strong>1 catalog</strong> before continuing."
    	    	    render(view: "edit", model: [productInstance: productInstance, catalogIdSelectionList: catalogIdSelectionList ])
    	    	    return
				}
				
				def catalogIdsArray = params.catalogIds.split(',').collect{it as int}
				
				if(!catalogIdsArray){
					flash.error = "Something went wrong while processing update. Please try again."
					render(view: "edit", model: [productInstance: productInstance])
					return
				}    	  
            	
            	
				productInstance.catalogs = null
				catalogIdsArray.each{ catalogId ->
					def catalog = Catalog.get(catalogId)
					if(catalog){
						productInstance.addToCatalogs(catalog)
						productInstance.save(flush:true)
					}
				}
            	
				
				def imageFile = request.getFile('image')
				BufferedImage originalImage = null;
				def fullFileName = imageFile.getOriginalFilename()
				
				String[] nameSplit = fullFileName.toString().split("\\.")
				def fileName = nameSplit[0]
				
				fileName = fileName.replaceAll("[^\\w\\s]","")
				fileName = fileName.replaceAll(" ", "_")
				
				def productName = productInstance.name
				
				productName = productName.replaceAll("[^\\w\\s]",""); 
				productName = productName.replaceAll(" ", "_")
				
				
				try {
	        	
					originalImage = ImageIO.read(imageFile.getInputStream());
		 		   	
					if(originalImage){
					
		 		    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            	
						//def baseUrl = "images/products/${productName}/"
						def baseUrl = "images/"
						
						def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
						absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
						//def baseDirectory = "${absolutePath}products/${productName}/"
						def baseDirectory = "${absolutePath}"
						
						
						new File(baseDirectory).mkdirs();
						
						productInstance.detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
 						BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
					
						def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
						ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
						
						
						def imageUrl = "${baseUrl}${fileName}.jpg"
						productInstance.imageUrl = imageUrl
				
						def imageLocation = "${baseDirectory}${fileName}.jpg"
						ImageIO.write(originalImage, "jpg",new File(imageLocation));
					
					}
					
					
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
				
				resolveSalesPrice(params)
            	
				productInstance.properties = params
				productInstance.price = new BigDecimal(params.price)
				
				if(params.salesPrice && params.salesPrice != "-"){
					productInstance.salesPrice = new BigDecimal(params.salesPrice)
				}
		    	
				
    	    	if (!productInstance.save(flush: true)) {
					flash.message = "Something went wrong while trying to update. Please try again."
    	    	    render(view: "edit", model: [productInstance: productInstance])
    	    	    return
    	    	}
    	    	
    	    	flash.message = "Successfully updated product..."
    	    	redirect(action: "edit", id: productInstance.id)
				
			}catch(Exception e){
				e.printStackTrace()
				flash.message = "Something went wrong..."
				redirect(action:"edit", id: productInstance.id)
			}
    	}
	}




	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
		authenticatedAdminProduct { adminAccount, productInstance ->
       		try {
				//Delete all ProductViewLogs
				ProductViewLog.executeUpdate("delete ProductViewLog p where p.product = :product", [product : productInstance])
                
                def productSpecifications = ProductSpecification.findAllByProduct(productInstance)
                productSpecifications.each { productSpecification ->
                    productInstance.removeFromProductSpecifications(productSpecification)
                    productSpecification.delete(flush:true)
                }
                
       		    productInstance.delete(flush: true)
       		    flash.message = "Successfull deleted product"
       		    redirect(action: "list")
       		}catch (DataIntegrityViolationException e) {
       		    flash.message = "Something went wrong when trying to delete.  The item you are trying to delete might exist in a Order or shopping cart.  Please try again or disable product"
       		    redirect(action: "edit", id: id)
        	}
		}
    }
	
	
	
	
	
	@Secured(['ROLE_ADMIN'])
	def remove_additional_photo(Long id){
		authenticatedAdmin{ adminAccount ->
			def additionalPhoto = AdditionalPhoto.get(id)
			
			if(additionalPhoto){
				def productInstance = additionalPhoto.product
				additionalPhoto.delete(flush:true)
				flash.message = "Successfully removed photo from product"
				redirect(action:"additional_photos", id: productInstance.id)
			}
		}
	}
	

	

	@Secured(['ROLE_ADMIN'])
	def add_additional_photo(){
		authenticatedAdminProduct { adminAccount, productInstance ->
		
			try {

				def imageFile = request.getFile('image')
				
				def fullFileName = imageFile.getOriginalFilename()
				
				String[] nameSplit = fullFileName.toString().split("\\.")
				def fileName = nameSplit[0]
				
				fileName = fileName.replaceAll("[^\\w\\s]","")
				fileName = fileName.replaceAll(" ", "_")
				
				BufferedImage originalImage = null;
				
				def productName =  productInstance.name
				productName = productName.replaceAll("[^\\w\\s]",""); 
				productName = productName.replaceAll(" ", "_")
				
				
				originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
				if(originalImage){
				
		 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					//def baseUrl = "images/products/${productName}/"
					def baseUrl = "images/"
					
					def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
					absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
					//def baseDirectory = "${absolutePath}products/${productName}/"
					def baseDirectory = "${absolutePath}"
					
					
					new File(baseDirectory).mkdirs();
					
					def detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
					BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
				
					def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
					ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
					
					
					def imageUrl = "${baseUrl}${fileName}.jpg"
					def imageLocation = "${baseDirectory}${fileName}.jpg"
					ImageIO.write(originalImage, "jpg", new File(imageLocation));

	    			def additionalPhoto = new AdditionalPhoto();
					additionalPhoto.name = fileName
					additionalPhoto.imageUrl = imageUrl
					additionalPhoto.detailsImageUrl = detailsImageUrl
					additionalPhoto.product = productInstance
					additionalPhoto.save(flush:true)
					
					flash.message = "Successfully added photo"
	       	 		redirect(action: "additional_photos", id: productInstance.id)
					
				}else{
					flash.message = "please provide product image & image name"
	       	 		redirect(action: "additional_photos", id: productInstance.id)
				}
				
		    } catch (IOException e) {
		    	e.printStackTrace();
				flash.message = "Something went wrong, please try again"
	       	 	redirect(action: "additional_photos", id: productInstance.id)
		    }
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def additional_photos(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			[productInstance : productInstance]
		}	
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def product_options(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			def productOptions = ProductOption.findAllByProduct(productInstance)
			[productInstance : productInstance, productOptions: productOptions]
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def add_product_option(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			[productInstance : productInstance]
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def save_option(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			if(params.name){
				def productOption = new ProductOption()
				productOption.product = productInstance
				productOption.name = params.name
				productOption.save(flush:true)
				
				productInstance.addToProductOptions(productOption)
				productInstance.save(flush:true)

				flash.message = "Successfully created product option.  Now add variants"
				redirect(controller : 'productOption', action: 'edit', id : productOption.id )
				
			}else{
				flash.message = "Name cannot be blank, please specify product option name"
				request.productInstance = productInstance
				render(view : 'add_product_option')
			}
		}
	}
	
	def resolveSalesPrice(params){
		if(params.salesPrice == '-')params.salesPrice = null
	}
	
}
