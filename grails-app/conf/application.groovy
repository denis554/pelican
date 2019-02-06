grails.controllers.upload.maxFileSize=5000000
grails.controllers.upload.maxRequestSize=5000000

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.shiro.active                      = true
grails.plugin.springsecurity.logout.postOnly                   = false
grails.plugin.springsecurity.userLookup.userDomainClassName    = 'io.pelican.Account'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'io.pelican.AccountRole'
grails.plugin.springsecurity.authority.className               = 'io.pelican.Role'
grails.plugin.springsecurity.shiro.permissionDomainClassName   = 'io.pelican.Permission'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/notFound',       access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/uploads/**',     access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/fonts/**',    access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/fonts',       filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]


//grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
//grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/admin'