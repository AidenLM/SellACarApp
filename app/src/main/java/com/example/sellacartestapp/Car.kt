package com.example.sellacartestapp

import org.w3c.dom.Text

data class Car(
    val comment: String,
    val brand: String,
    val model: String,
    val year: String,
    val date: String,
    val location: String,
    val price: String,
    val kilometer: String,
    val downloadUrl: String,
    val email: String,
    val uploaderUid: String,
    val descriptionText: String
) {
}