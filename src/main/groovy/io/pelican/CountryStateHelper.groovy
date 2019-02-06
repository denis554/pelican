package io.pelican

public class CountryStateHelper{

	/**
		TODO: add country/state maintenance
		"expanded" relates to brain tree, brain tree does not support India or Brazil at all yet.
	**/
	def countryStates = [ 
		[ 
			"name" : "United States",
			"states" : [ "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", 
			"Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", 
			"Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", 
			"New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", 
			"Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", 
			"West Virginia", "Wisconsin", "Wyoming"]
		],
		[
			"name": "United Kingdom",
			"states" : []
			/**"states":["Northern Ireland", "Scotland", "Wales"]**/
		],
		[
			"name": "New Zealand",
			"states" : []
		],
		[
			"name" : "Canada",
			"states": [ "Nunavut", "Alberta", "Saskatchewan", "Yukon", "Manitoba", 
			"British Columbia", "Ontario", "Quebec", "Prince Edward Island", 
			"Newfoundland and Labrador", "Northwest Territories", "Nova Scotia", 
			"New Brunswick"]
		],
		[
			"name" : "Netherlands",
			"states" : []
		],
		[
			"name" : "Germany",
			"states" : []
			/**"states": ["Baden-Württemberg", "Bayern", "Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen", "Niedersachsen", 
			"Mecklenburg-Vorpommern", "Nordrhein-Westfalen (NRW)", "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", 
			"Schleswig-Holstein", "Thüringen"]**/			
		],
		[
			"name": "France",
			"states" : []
			/**"states": ["Auvergne - Rhône-Alpes", "Bretagne (Brittany)", "Bourgogne - Franche-Comté", "Corse", 
			"Centre - Val de Loire", "Grand Est", "Hauts de France", "Ile de France", "Nouvelle Aquitaine", "Normandie", 
			"Occitanie (Midi-Pyrénées, Languedoc)", "Pays de la Loire", "Provence - Cote d'Azur"]**/
		],
		[
			"name" : "Ireland",
			"states" : []
		],
		[
			"name": "Hong Kong",
			"states" : []
		],		
		[
			"name": "Brazil",
			"states": []
			/**"states": ["Acre", "Alagoas", "Amapá", "Amazonas", "Bahia", "Ceará", "Distrito Federal", "Espírito Santo", 
			"Goiás", "Maranhão", "Mato GrossoMato Grosso do Sul", "Minas Gerais", "Pará", "Paraíba", "Paraná", "Pernambuco", 
			"Piaui", "Rio de Janeiro", "Rio Grande do Norte", "Rio Grande do Sul", "Rondônia", "Roraima", 
			"Santa Catarina", "São Paulo", "Sergipe", "Tocantins"]**/
		],
		[
			"name": "India",
			"states": []
			/**"states": ["Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", 
			"Haryana", "Himachal Pradesh", "Jammu & Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", 
			"Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", 
			"Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal"]**/
		],
		[
			"name" : "Greece",
			"expanded" : true,
			"states": []
		]		
	]

}