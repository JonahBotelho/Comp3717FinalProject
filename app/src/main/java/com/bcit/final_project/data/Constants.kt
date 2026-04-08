package com.bcit.final_project.data

const val API_KEY = "1"

const val BASE_URL = "https://www.themealdb.com/api/json/v1"
const val SEARCH_BY_ID = "${BASE_URL}/${API_KEY}/lookup.php?i="
const val SEARCH_BY_NAME = "${BASE_URL}/${API_KEY}/search.php?s="
const val RANDOM_RECIPE = "${BASE_URL}/${API_KEY}/random.php"
