package pelican

import pelican.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

import io.pelican.Account
import io.pelican.Permission
import io.pelican.Catalog
import io.pelican.Product
import io.pelican.ProductOption
import io.pelican.Variant

import io.pelican.Specification
import io.pelican.SpecificationOption
import io.pelican.ProductSpecification

import io.pelican.AdditionalPhoto

import io.pelican.ShoppingCart
import io.pelican.ShoppingCartItem
import io.pelican.ShoppingCartItemOption

import io.pelican.Transaction
import io.pelican.Page
import io.pelican.Upload
import io.pelican.Layout

import io.pelican.log.CatalogViewLog
import io.pelican.log.PageViewLog
import io.pelican.log.ProductViewLog
import io.pelican.log.SearchLog
import io.pelican.log.LoginLog

import groovy.json.*
import groovy.json.JsonOutput
import grails.converters.JSON
import groovy.json.JsonOutput

import java.io.InputStream
import java.io.ByteArrayInputStream

import io.pelican.ExportDataService

@Mixin(BaseController)
class ExportController {
	
	def exportDataService
	def missingUuidHelperService
	
	@Secured(['permitAll'])
	def shopping_carts(){
		def shoppingCarts = ShoppingCart.list()
		render shoppingCarts as JSON
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def resolve_missing_uuids(){
		println "resolve missing uuids..."
		missingUuidHelperService.correctMissingUuids()
		def json = [:]
		render json as JSON
	}
	
 	@Secured(['ROLE_ADMIN'])
	def view_export(){
		//TODO:add numbers of data to be exported
	}

 	@Secured(['ROLE_ADMIN'])
	def export_data(){

		def json = exportDataService.export(params)
		InputStream is = new ByteArrayInputStream(json.getBytes());

		render(file: is, fileName: "pelican-data.json")	
	}
	
}